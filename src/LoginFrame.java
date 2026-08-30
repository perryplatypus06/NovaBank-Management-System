import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class LoginFrame extends JFrame {
    private JTextField txtUsername, txtFullName;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnToggleMode;
    private JLabel lblTitle, lblFullName;
    private boolean isRegisterMode = false;

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("NovaBank - Secure Authentication");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        lblTitle = new JLabel("Welcome to NovaBank", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full Name Field (Registration Only)
        lblFullName = new JLabel("Full Name:");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFullName.setVisible(false);

        txtFullName = new JTextField();
        txtFullName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtFullName.setVisible(false);

        // Username Field
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Password Field
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Buttons
        btnLogin = createButton("Login", new Color(21, 101, 192));
        btnRegister = createButton("Create Account", new Color(46, 125, 50));
        btnRegister.setVisible(false);

        btnToggleMode = new JButton("Need an account? Register here");
        btnToggleMode.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnToggleMode.setContentAreaFilled(false);
        btnToggleMode.setBorderPainted(false);
        btnToggleMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleMode.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Event Handlers
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> handleRegister());
        btnToggleMode.addActionListener(e -> toggleMode());

        // Assembly
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(lblFullName);
        mainPanel.add(txtFullName);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(lblUser);
        mainPanel.add(txtUsername);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(lblPass);
        mainPanel.add(txtPassword);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(btnLogin);
        mainPanel.add(btnRegister);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnToggleMode);

        add(mainPanel);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        lblTitle.setText(isRegisterMode ? "Create New Account" : "Welcome to NovaBank");
        lblFullName.setVisible(isRegisterMode);
        txtFullName.setVisible(isRegisterMode);
        btnLogin.setVisible(!isRegisterMode);
        btnRegister.setVisible(isRegisterMode);
        btnToggleMode.setText(isRegisterMode ? "Already have an account? Login" : "Need an account? Register here");
        revalidate();
        repaint();
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = Database.getConnection()) {
            String sql = "SELECT u.user_id, u.full_name, a.account_id, a.account_number, a.balance " +
                    "FROM users u JOIN accounts a ON u.user_id = a.user_id " +
                    "WHERE u.username = ? AND u.password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String accNum = rs.getString("account_number");
                double balance = rs.getDouble("balance");
                String fullName = rs.getString("full_name");

                BankAccount account = new BankAccount(accountId, accNum, balance);

                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    BankDashboard dashboard = new BankDashboard(account, fullName);
                    dashboard.setVisible(true);
                });

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all registration fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);

            // 1. Create User
            String userSql = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
            PreparedStatement userPs = con.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userPs.setString(1, username);
            userPs.setString(2, password);
            userPs.setString(3, fullName);
            userPs.executeUpdate();

            ResultSet generatedKeys = userPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                String accNum = "NB-" + (1000 + new Random().nextInt(9000));

                // 2. Create Account with 0.00 Initial Balance
                String accSql = "INSERT INTO accounts (user_id, account_number, balance) VALUES (?, ?, ?)";
                PreparedStatement accPs = con.prepareStatement(accSql);
                accPs.setInt(1, userId);
                accPs.setString(2, accNum);
                accPs.setDouble(3, 0.00);
                accPs.executeUpdate();

                con.commit();

                JOptionPane.showMessageDialog(this, "Account Created Successfully!\nYour Account Number: " + accNum, "Success", JOptionPane.INFORMATION_MESSAGE);
                toggleMode();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists. Choose another.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}