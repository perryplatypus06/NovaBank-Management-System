import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class BankDashboard extends JFrame {
    private final BankAccount account;
    private final String userName;
    private boolean isDark = false;

    private JLabel lblBalance, lblDeposits, lblWithdrawals, lblTxCount, lblStatus;
    private JTextField txtAmount;
    private JTable txTable;
    private DefaultTableModel tableModel;

    private JPanel mainPanel, headerPanel, statsPanel, centerPanel, actionCard, historyCard;
    private JLabel titleLabel;
    private JButton btnTheme, btnLogout, btnDeposit, btnWithdraw;

    public BankDashboard(BankAccount account, String userName) {
        this.account = account;
        this.userName = userName;
        initUI();
        updateStats();
    }

    private void initUI() {
        setTitle("NovaBank Enterprise - Logged in as: " + userName);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. HEADER SECTION ---
        headerPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Welcome back, " + userName + " (" + account.getAccountNumber() + ")");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel headerControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerControls.setOpaque(false);

        btnTheme = createStyledButton("Toggle Theme", new Color(70, 130, 180));
        btnTheme.addActionListener(e -> {
            isDark = !isDark;
            applyTheme();
        });

        btnLogout = createStyledButton("Logout", new Color(198, 40, 40));
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        headerControls.add(btnTheme);
        headerControls.add(btnLogout);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(headerControls, BorderLayout.EAST);

        // --- 2. STATS OVERVIEW CARDS ---
        statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setPreferredSize(new Dimension(0, 90));

        lblBalance = createCard(statsPanel, "ACCOUNT BALANCE", "₹0.00", new Color(46, 125, 50));
        lblDeposits = createCard(statsPanel, "TOTAL DEPOSITED", "₹0.00", new Color(21, 101, 192));
        lblWithdrawals = createCard(statsPanel, "TOTAL WITHDRAWN", "₹0.00", new Color(198, 40, 40));
        lblTxCount = createCard(statsPanel, "TRANSACTIONS", "0", new Color(106, 27, 154));

        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(statsPanel, BorderLayout.SOUTH);
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // --- 3. CENTER WORKSPACE ---
        centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));

        actionCard = new JPanel();
        actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));
        actionCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(" Quick Operations "),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblAmountHeading = new JLabel("Enter Amount (₹):");
        lblAmountHeading.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnDeposit = createStyledButton("Deposit Funds", new Color(46, 125, 50));
        btnWithdraw = createStyledButton("Withdraw Funds", new Color(198, 40, 40));

        btnDeposit.addActionListener(e -> doDeposit());
        btnWithdraw.addActionListener(e -> doWithdraw());

        lblStatus = new JLabel("Session Active");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        actionCard.add(lblAmountHeading);
        actionCard.add(Box.createVerticalStrut(8));
        actionCard.add(txtAmount);
        actionCard.add(Box.createVerticalStrut(15));
        actionCard.add(btnDeposit);
        actionCard.add(Box.createVerticalStrut(10));
        actionCard.add(btnWithdraw);
        actionCard.add(Box.createVerticalStrut(20));
        actionCard.add(lblStatus);

        historyCard = new JPanel(new BorderLayout());
        historyCard.setBorder(BorderFactory.createTitledBorder(" Recent Statement History "));

        String[] columns = {"Type", "Amount", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        txTable = new JTable(tableModel);
        txTable.setRowHeight(28);
        txTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        txTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < txTable.getColumnCount(); i++) {
            txTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(txTable);
        historyCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(actionCard);
        centerPanel.add(historyCard);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        applyTheme();
    }

    private JLabel createCard(JPanel parent, String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(8, 12, 8, 12)
                )
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLbl.setForeground(Color.GRAY);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);

        parent.add(card);
        return valLbl;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return btn;
    }

    private void doDeposit() {
        try {
            double amt = Double.parseDouble(txtAmount.getText().trim());
            if (account.deposit(amt)) {
                showStatus("Successfully deposited ₹" + String.format("%.2f", amt), true);
                txtAmount.setText("");
                updateStats();
            } else {
                showStatus("Deposit failed: Enter a positive value.", false);
            }
        } catch (NumberFormatException ex) {
            showStatus("Error: Enter a valid numeric amount.", false);
        }
    }

    private void doWithdraw() {
        try {
            double amt = Double.parseDouble(txtAmount.getText().trim());
            if (account.withdraw(amt)) {
                showStatus("Successfully withdrew ₹" + String.format("%.2f", amt), true);
                txtAmount.setText("");
                updateStats();
            } else {
                showStatus("Withdrawal failed: Insufficient funds or bad amount.", false);
            }
        } catch (NumberFormatException ex) {
            showStatus("Error: Enter a valid numeric amount.", false);
        }
    }

    private void updateStats() {
        lblBalance.setText(String.format("₹%.2f", account.getBalance()));
        lblDeposits.setText(String.format("₹%.2f", account.getTotalDeposits()));
        lblWithdrawals.setText(String.format("₹%.2f", account.getTotalWithdrawals()));
        lblTxCount.setText(String.valueOf(account.getTransactions().size()));
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Transaction tx : account.getTransactions()) {
            tableModel.addRow(new Object[]{
                    tx.getType().toString(),
                    String.format("₹%.2f", tx.getAmount()),
                    sdf.format(tx.getTimestamp())
            });
        }
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setForeground(success ? new Color(46, 125, 50) : new Color(198, 40, 40));
    }

    private void applyTheme() {
        Color bg = isDark ? new Color(34, 34, 34) : new Color(245, 247, 250);
        Color cardBg = isDark ? new Color(45, 45, 45) : Color.WHITE;
        Color fg = isDark ? Color.WHITE : new Color(33, 33, 33);

        mainPanel.setBackground(bg);
        headerPanel.setBackground(bg);
        statsPanel.setBackground(bg);
        centerPanel.setBackground(bg);

        actionCard.setBackground(cardBg);
        historyCard.setBackground(cardBg);
        titleLabel.setForeground(fg);

        txtAmount.setBackground(isDark ? new Color(60, 60, 60) : Color.WHITE);
        txtAmount.setForeground(fg);
        txtAmount.setCaretColor(fg);

        txTable.setBackground(cardBg);
        txTable.setForeground(fg);
        txTable.getTableHeader().setBackground(isDark ? new Color(60, 60, 60) : new Color(230, 235, 240));
        txTable.getTableHeader().setForeground(fg);

        repaint();
    }
}