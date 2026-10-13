import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class LoginFrame extends JFrame {
    private JTextField txtUsername, txtFullName;
    private JPasswordField txtPassword;
    private JLabel lblStrengthFeedback;
    private JButton btnLogin, btnRegister, btnToggleMode;
    private JLabel lblTitle, lblFullName;
    private boolean isRegisterMode = false;

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("NovaBank - Secure Authentication");
        setSize(780, 480); // Split-screen width
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main layout container split into two halves
        setLayout(new GridLayout(1, 2));

        // ==================== LEFT: BRAND PANEL ====================
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(new Color(21, 101, 192)); // Deep professional blue
        brandPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblBrandTitle = new JLabel("NovaBank");
        lblBrandTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBrandTitle.setForeground(Color.WHITE);
        lblBrandTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBrandSubtitle = new JLabel("<html>Secure, modern banking with enterprise-grade encryption & protection.</html>");
        lblBrandSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBrandSubtitle.setForeground(new Color(224, 224, 224));
        lblBrandSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSecurityBadge = new JLabel("✓ BCrypt Hashed  |  ✓ Brute-Force Protected");
        lblSecurityBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSecurityBadge.setForeground(new Color(129, 199, 132)); // Soft green
        lblSecurityBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandPanel.add(lblBrandTitle);
        brandPanel.add(Box.createVerticalStrut(15));
        brandPanel.add(lblBrandSubtitle);
        brandPanel.add(Box.createVerticalStrut(30));
        brandPanel.add(lblSecurityBadge);

        // ==================== RIGHT: FORM PANEL ====================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(30, 35, 30, 35));
        formPanel.setBackground(Color.WHITE);

        lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Full Name Field (Registration Only)
        lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFullName.setForeground(new Color(100, 100, 100));
        lblFullName.setVisible(false);

        txtFullName = new JTextField();
        txtFullName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtFullName.setVisible(false);

        // Username Field
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(100, 100, 100));
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Password Field Section with Show/Hide Toggle Button
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(100, 100, 100));

        JPanel passwordContainer = new JPanel();
        passwordContainer.setLayout(new BorderLayout(5, 0));
        passwordContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordContainer.setBackground(Color.WHITE);
        passwordContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));

        JButton btnToggleVisibility = new JButton("Show");
        btnToggleVisibility.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnToggleVisibility.setForeground(new Color(21, 101, 192));
        btnToggleVisibility.setBackground(new Color(240, 244, 248));
        btnToggleVisibility.setFocusPainted(false);
        btnToggleVisibility.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleVisibility.setPreferredSize(new Dimension(70, 38));

        // Toggle password mask on click
        final char defaultEchoChar = txtPassword.getEchoChar();
        btnToggleVisibility.addActionListener(e -> {
            if (txtPassword.getEchoChar() == 0) {
                txtPassword.setEchoChar(defaultEchoChar);
                btnToggleVisibility.setText("Show");
            } else {
                txtPassword.setEchoChar((char) 0);
                btnToggleVisibility.setText("Hide");
            }
        });

        passwordContainer.add(txtPassword, BorderLayout.CENTER);
        passwordContainer.add(btnToggleVisibility, BorderLayout.EAST);

        // Password Strength Feedback Label (Registration Only)
        lblStrengthFeedback = new JLabel(" ");
        lblStrengthFeedback.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStrengthFeedback.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStrengthFeedback.setVisible(false);

        // Live typing listener for password strength
        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void checkStrength() {
                if (!isRegisterMode) return;
                String pwd = new String(txtPassword.getPassword());
                if (pwd.isEmpty()) {
                    lblStrengthFeedback.setText(" ");
                    return;
                }
                PasswordValidator.ValidationResult result = PasswordValidator.evaluatePassword(pwd);
                String category = result.getStrengthCategory();

                if (category.equals("Weak")) {
                    lblStrengthFeedback.setForeground(new Color(211, 47, 47)); // Red
                    lblStrengthFeedback.setText("Strength: Weak (Add uppercase, numbers, symbols)");
                } else if (category.equals("Moderate")) {
                    lblStrengthFeedback.setForeground(new Color(230, 126, 34)); // Orange
                    lblStrengthFeedback.setText("Strength: Moderate");
                } else {
                    lblStrengthFeedback.setForeground(new Color(46, 125, 50)); // Green
                    lblStrengthFeedback.setText("Strength: Strong!");
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { checkStrength(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { checkStrength(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { checkStrength(); }
        });

        // Buttons
        btnLogin = createButton("Login", new Color(21, 101, 192));
        btnRegister = createButton("Create Account", new Color(46, 125, 50));
        btnRegister.setVisible(false);

        btnToggleMode = new JButton("Need an account? Register here");
        btnToggleMode.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnToggleMode.setForeground(new Color(21, 101, 192));
        btnToggleMode.setContentAreaFilled(false);
        btnToggleMode.setBorderPainted(false);
        btnToggleMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleMode.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Event Handlers
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> handleRegister());
        btnToggleMode.addActionListener(e -> toggleMode());

        // Assembly of Form Panel
        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(lblFullName);
        formPanel.add(txtFullName);
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(lblUser);
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(lblPass);
        formPanel.add(passwordContainer);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(lblStrengthFeedback);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(btnLogin);
        formPanel.add(btnRegister);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(btnToggleMode);

        // Add both halves to the main Frame
        add(brandPanel);
        add(formPanel);
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
        lblStrengthFeedback.setVisible(isRegisterMode);
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

        // 1. Check if account is temporarily locked out due to repeated failures
        if (LoginAttemptTracker.isLockedOut(username)) {
            long remainingSeconds = LoginAttemptTracker.getRemainingLockoutSeconds(username);
            JOptionPane.showMessageDialog(this,
                    "Too many failed login attempts.\nPlease try again in " + remainingSeconds + " seconds.",
                    "Account Temporarily Locked", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = Database.getConnection()) {
            String sql = "SELECT u.user_id, u.password, u.full_name, a.account_id, a.account_number, a.balance " +
                    "FROM users u JOIN accounts a ON u.user_id = a.user_id " +
                    "WHERE u.username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                if (PasswordHasher.verifyPassword(password, storedHash)) {
                    // Successful login: reset failed attempt counter
                    LoginAttemptTracker.recordSuccess(username);

                    // Lazy Migration for legacy plaintext accounts
                    if (!PasswordHasher.isHashed(storedHash)) {
                        String newHash = PasswordHasher.hashPassword(password);
                        String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
                        try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                            updatePs.setString(1, newHash);
                            updatePs.setInt(2, rs.getInt("user_id"));
                            updatePs.executeUpdate();
                        }
                    }

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
                    return;
                }
            }

            // Failed login: record attempt for rate limiting
            LoginAttemptTracker.recordFailure(username);

            // Generic failure message (prevents username enumeration)
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);

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

        // 1. Evaluate Password Strength before registration
        PasswordValidator.ValidationResult validationResult = PasswordValidator.evaluatePassword(password);
        if (!validationResult.isValid()) {
            String feedback = String.join("\n", validationResult.getFeedbackMessages());
            JOptionPane.showMessageDialog(this, feedback, "Password Policy Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Hash Password Securely with BCrypt
        String hashedPassword = PasswordHasher.hashPassword(password);

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);

            // 3. Create User with Hashed Password
            String userSql = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
            PreparedStatement userPs = con.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userPs.setString(1, username);
            userPs.setString(2, hashedPassword);
            userPs.setString(3, fullName);
            userPs.executeUpdate();

            ResultSet generatedKeys = userPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                String accNum = "NB-" + (1000 + new Random().nextInt(9000));

                // 4. Create Account with 0.00 Initial Balance
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