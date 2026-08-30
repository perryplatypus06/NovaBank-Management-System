import java.sql.Timestamp;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAW } // Type-safe transaction types

    private final Type type;
    private final double amount;
    private final Timestamp timestamp;

    public Transaction(Type type, double amount) {
        this(type, amount, new Timestamp(System.currentTimeMillis()));
    }

    public Transaction(Type type, double amount, Timestamp timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public Timestamp getTimestamp() { return timestamp; }
}