import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class BankDashboard extends JFrame {
    private BankAccount account;
    private String fullName;
    private JLabel lblBalanceVal;
    private JPanel transactionsCard;

    public BankDashboard(BankAccount account, String fullName) {
        this.account = account;
        this.fullName = fullName;
        initDashboardUI();
    }

    private void initDashboardUI() {
        setTitle("NovaBank - Client Dashboard");
        setSize(920, 660); // Slightly taller to comfortably fit the recent transactions feed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background panel with light modern gray/blue tone
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // ==================== TOP HEADER ====================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblGreeting = new JLabel("Good day, " + fullName + "!");
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblGreeting.setForeground(new Color(33, 33, 33));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(new Color(211, 47, 47));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(211, 47, 47), 1),
                BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        headerPanel.add(lblGreeting, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ==================== CENTER CONTENT GRID ====================
        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        centerGrid.setOpaque(false);

        // ----- LEFT COLUMN: Virtual Debit Card & Recent Transactions -----
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setOpaque(false);

        JLabel lblMyAccounts = new JLabel("My Accounts");
        lblMyAccounts.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMyAccounts.setForeground(new Color(66, 66, 66));
        lblMyAccounts.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Visual Debit Card Panel (Purple Gradient style)
        JPanel debitCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(92, 107, 192), getWidth(), getHeight(), new Color(40, 53, 147));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        debitCard.setLayout(new BorderLayout());
        debitCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        debitCard.setPreferredSize(new Dimension(400, 170));
        debitCard.setBorder(new EmptyBorder(20, 25, 20, 25));
        debitCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCardBrand = new JLabel("NovaBank");
        lblCardBrand.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCardBrand.setForeground(Color.WHITE);

        JLabel lblCardNum = new JLabel(account.getAccountNumber());
        lblCardNum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCardNum.setForeground(new Color(224, 224, 224));

        JLabel lblCardHolder = new JLabel(fullName.toUpperCase());
        lblCardHolder.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCardHolder.setForeground(Color.WHITE);

        debitCard.add(lblCardBrand, BorderLayout.NORTH);
        debitCard.add(lblCardNum, BorderLayout.CENTER);
        debitCard.add(lblCardHolder, BorderLayout.SOUTH);

        // Recent Transactions Feed Widget (Bottom Left)
        JLabel lblRecentTitle = new JLabel("Recent Activity");
        lblRecentTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRecentTitle.setForeground(new Color(66, 66, 66));
        lblRecentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        transactionsCard = new JPanel();
        transactionsCard.setLayout(new BoxLayout(transactionsCard, BoxLayout.Y_AXIS));
        transactionsCard.setBackground(Color.WHITE);
        transactionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 185));
        transactionsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(12, 15, 12, 15)
        ));
        transactionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        populateTransactionsFeed();

        leftColumn.add(lblMyAccounts);
        leftColumn.add(Box.createVerticalStrut(10));
        leftColumn.add(debitCard);
        leftColumn.add(Box.createVerticalStrut(15));
        leftColumn.add(lblRecentTitle);
        leftColumn.add(Box.createVerticalStrut(10));
        leftColumn.add(transactionsCard);

        // ----- RIGHT COLUMN: Balance Card & Quick Actions -----
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setOpaque(false);

        JLabel lblOverview = new JLabel("Financial Overview");
        lblOverview.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOverview.setForeground(new Color(66, 66, 66));
        lblOverview.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Balance Display Card
        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(Color.WHITE);
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        balanceCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBalanceTitle = new JLabel("Available Balance");
        lblBalanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBalanceTitle.setForeground(new Color(117, 117, 117));

        lblBalanceVal = new JLabel(String.format("$%,.2f", account.getBalance()));
        lblBalanceVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBalanceVal.setForeground(new Color(46, 125, 50)); // Green accent

        balanceCard.add(lblBalanceTitle);
        balanceCard.add(Box.createVerticalStrut(5));
        balanceCard.add(lblBalanceVal);

        // Quick Actions Grid Card
        JPanel actionsCard = new JPanel(new GridLayout(2, 2, 10, 10));
        actionsCard.setBackground(Color.WHITE);
        actionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        actionsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        actionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDeposit = createActionButton("Deposit", new Color(21, 101, 192));
        JButton btnWithdraw = createActionButton("Withdraw", new Color(230, 81, 0));
        JButton btnTransfer = createActionButton("Transfer", new Color(74, 20, 140));
        JButton btnHistory = createActionButton("Passbook", new Color(55, 71, 79));

        // Action Listeners
        btnDeposit.addActionListener(e -> performDeposit());
        btnWithdraw.addActionListener(e -> performWithdraw());
        btnTransfer.addActionListener(e -> performTransfer());
        btnHistory.addActionListener(e -> showPassbook());

        actionsCard.add(btnDeposit);
        actionsCard.add(btnWithdraw);
        actionsCard.add(btnTransfer);
        actionsCard.add(btnHistory);

        rightColumn.add(lblOverview);
        rightColumn.add(Box.createVerticalStrut(12));
        rightColumn.add(balanceCard);
        rightColumn.add(Box.createVerticalStrut(15));
        rightColumn.add(actionsCard);

        centerGrid.add(leftColumn);
        centerGrid.add(rightColumn);
        mainPanel.add(centerGrid, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void populateTransactionsFeed() {
        transactionsCard.removeAll();
        List<Transaction> txList = account.getTransactions();

        if (txList == null || txList.isEmpty()) {
            JLabel lblNoTx = new JLabel("No recent transactions found.");
            lblNoTx.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblNoTx.setForeground(new Color(150, 150, 150));
            transactionsCard.add(lblNoTx);
        } else {
            int count = 0;
            for (Transaction tx : txList) {
                if (count >= 4) break; // Show up to 4 recent transactions

                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

                boolean isDeposit = tx.getType() == Transaction.Type.DEPOSIT;
                String sign = isDeposit ? "+" : "-";
                Color amtColor = isDeposit ? new Color(46, 125, 50) : new Color(211, 47, 47); // Green for deposit, Red for withdrawal

                JLabel lblType = new JLabel(isDeposit ? "Deposit" : "Withdrawal");
                lblType.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblType.setForeground(new Color(60, 60, 60));

                JLabel lblAmt = new JLabel(sign + String.format("$%,.2f", tx.getAmount()));
                lblAmt.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblAmt.setForeground(amtColor);

                row.add(lblType, BorderLayout.WEST);
                row.add(lblAmt, BorderLayout.EAST);

                transactionsCard.add(row);
                transactionsCard.add(Box.createVerticalStrut(6));
                count++;
            }
        }
        transactionsCard.revalidate();
        transactionsCard.repaint();
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateDashboardState() {
        lblBalanceVal.setText(String.format("$%,.2f", account.getBalance()));
        populateTransactionsFeed();
    }

    private void performDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount ($):", "Deposit Funds", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (account.deposit(amount)) {
                    updateDashboardState();
                    JOptionPane.showMessageDialog(this, "Successfully deposited $" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed to sync with database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performWithdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount ($):", "Withdraw Funds", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0 || amount > account.getBalance()) {
                    JOptionPane.showMessageDialog(this, "Invalid amount or insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (account.withdraw(amount)) {
                    updateDashboardState();
                    JOptionPane.showMessageDialog(this, "Successfully withdrew $" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed to sync with database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performTransfer() {
        String targetAcc = JOptionPane.showInputDialog(this, "Enter recipient account number (e.g., NB-3002):", "Fund Transfer", JOptionPane.QUESTION_MESSAGE);
        if (targetAcc != null && !targetAcc.trim().isEmpty()) {
            if (targetAcc.trim().equals(account.getAccountNumber())) {
                JOptionPane.showMessageDialog(this, "Cannot transfer funds to your own account.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String amtInput = JOptionPane.showInputDialog(this, "Enter transfer amount ($):", "Fund Transfer", JOptionPane.QUESTION_MESSAGE);
            if (amtInput != null && !amtInput.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(amtInput);
                    if (amount <= 0 || amount > account.getBalance()) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try (Connection con = Database.getConnection()) {
                        con.setAutoCommit(false);
                        String checkSql = "SELECT account_id FROM accounts WHERE account_number = ?";
                        PreparedStatement checkPs = con.prepareStatement(checkSql);
                        checkPs.setString(1, targetAcc.trim());
                        ResultSet rs = checkPs.executeQuery();

                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(this, "Recipient account not found.", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int targetId = rs.getInt("account_id");

                        // Deduct from sender and log withdrawal transaction
                        if (!account.withdraw(amount)) {
                            con.rollback();
                            JOptionPane.showMessageDialog(this, "Transfer failed during deduction.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Add to recipient account
                        PreparedStatement addPs = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
                        addPs.setDouble(1, amount);
                        addPs.setInt(2, targetId);
                        addPs.executeUpdate();

                        // Log deposit transaction for recipient
                        PreparedStatement txPs = con.prepareStatement("INSERT INTO transactions (account_id, type, amount) VALUES (?, 'DEPOSIT', ?)");
                        txPs.setInt(1, targetId);
                        txPs.setDouble(2, amount);
                        txPs.executeUpdate();

                        con.commit();
                        updateDashboardState();
                        JOptionPane.showMessageDialog(this, "Successfully transferred $" + amount + " to " + targetAcc, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error during transfer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showPassbook() {
        JOptionPane.showMessageDialog(this, "Account Number: " + account.getAccountNumber() +
                "\nTotal Deposits: $" + account.getTotalDeposits() +
                "\nTotal Withdrawals: $" + account.getTotalWithdrawals() +
                "\nCurrent Balance: $" + account.getBalance() +
                "\nStatus: Active (Encrypted Session)", "Passbook / Account Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}