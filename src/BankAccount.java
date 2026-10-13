import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private final int accountId;
    private final String accountNumber;
    private double balance;
    private double totalDeposits;
    private double totalWithdrawals;
    private final List<Transaction> transactions;

    public BankAccount(int accountId, String accountNumber, double balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        loadTransactionsFromDB();
    }

    public void loadTransactionsFromDB() {
        try (Connection con = Database.getConnection()) {
            PreparedStatement txPs = con.prepareStatement(
                    "SELECT type, amount, created_at FROM transactions WHERE account_id = ? ORDER BY created_at DESC"
            );
            txPs.setInt(1, accountId);
            ResultSet txRs = txPs.executeQuery();

            transactions.clear();
            totalDeposits = 0;
            totalWithdrawals = 0;

            while (txRs.next()) {
                Transaction.Type type = Transaction.Type.valueOf(txRs.getString("type"));
                double amt = txRs.getDouble("amount");
                Timestamp ts = txRs.getTimestamp("created_at");

                transactions.add(new Transaction(type, amt, ts));
                if (type == Transaction.Type.DEPOSIT) {
                    totalDeposits += amt;
                } else {
                    totalWithdrawals += amt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean deposit(double amount) {
        if (amount <= 0) return false;

        this.balance += amount;
        this.totalDeposits += amount;
        this.transactions.add(0, new Transaction(Transaction.Type.DEPOSIT, amount));

        return syncTransactionToDB("DEPOSIT", amount);
    }

    public synchronized boolean withdraw(double amount) {
        if (amount <= 0 || amount > this.balance) return false;

        this.balance -= amount;
        this.totalWithdrawals += amount;
        this.transactions.add(0, new Transaction(Transaction.Type.WITHDRAW, amount));

        return syncTransactionToDB("WITHDRAW", amount);
    }

    private boolean syncTransactionToDB(String typeStr, double amount) {
        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);

            PreparedStatement updateBalance = con.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?");
            updateBalance.setDouble(1, this.balance);
            updateBalance.setInt(2, this.accountId);
            updateBalance.executeUpdate();

            PreparedStatement insertTx = con.prepareStatement("INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)");
            insertTx.setInt(1, this.accountId);
            insertTx.setString(2, typeStr);
            insertTx.setDouble(3, amount);
            insertTx.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTotalDeposits() {
        return totalDeposits;
    }

    public double getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}