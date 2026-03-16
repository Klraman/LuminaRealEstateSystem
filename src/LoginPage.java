import MyLib.*;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends javax.swing.JPanel {

    // Brand colours
    private static final Color PRIMARY = new Color(26, 58, 92);
    private static final Color GOLD    = new Color(212, 175, 55);
    private static final Color WHITE   = Color.WHITE;

    private final MainJFrame mainFrame;

    private JTextField    emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public LoginPage(MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setBackground(PRIMARY);

        // ── White card ──────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(420, 460));

        // Brand header
        JLabel logo = new JLabel("LUMINA");
        logo.setFont(new Font("Arial", Font.BOLD, 42));
        logo.setForeground(PRIMARY);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Real Estate Sales & Management");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(320, 2));
        sep.setForeground(GOLD);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));

        // Email
        card.add(fieldLabel("Email / Username"));
        card.add(Box.createVerticalStrut(4));
        emailField = styledTextField();
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));

        // Password
        card.add(fieldLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        passwordField = new JPasswordField();
        styleInput(passwordField);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(12));

        // Role
        card.add(fieldLabel("Role"));
        card.add(Box.createVerticalStrut(4));
        roleCombo = new JComboBox<>(new String[]{"Admin", "Agent", "Buyer"});
        roleCombo.setMaximumSize(new Dimension(320, 36));
        roleCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        card.add(roleCombo);
        card.add(Box.createVerticalStrut(28));

        // Buttons
        JButton loginBtn = styledButton("Log In", PRIMARY);
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        JButton registerBtn = styledButton("Register New Account", GOLD);
        registerBtn.addActionListener(e -> showRegisterDialog());
        card.add(registerBtn);

        // Demo hint
        card.add(Box.createVerticalStrut(16));
        JLabel hint = new JLabel("<html><center><font color='gray' size='2'>"
                + "Demo → admin@lumina.com / admin123<br>"
                + "agent@lumina.com / agent123 &nbsp;|&nbsp; buyer@lumina.com / buyer123"
                + "</font></center></html>");
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        add(card);
    }

    // ── Login logic ─────────────────────────────────────────────────────
    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role     = (String) roleCombo.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your email and password.", "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = AppContext.getInstance().authenticate(email, password, role);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.login();
        if      (user instanceof Admin) mainFrame.showAdminPage((Admin) user);
        else if (user instanceof Agent) mainFrame.showAgentPage((Agent) user);
        else if (user instanceof Buyer) mainFrame.showBuyerPage((Buyer) user);
    }

    // ── Register dialog ─────────────────────────────────────────────────
    private void showRegisterDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Register New Account", true);
        dlg.setSize(420, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        JTextField  nameF  = new JTextField();
        JTextField  emailF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JComboBox<String> roleC = new JComboBox<>(new String[]{"Agent", "Buyer"});
        JTextField  budgetF = new JTextField("1000000");
        JTextField  commF   = new JTextField("0.05");

        JLabel budgetLbl = new JLabel("Budget (Php):");
        JLabel commLbl   = new JLabel("Commission Rate:");

        // Toggle budget/commission fields by role
        roleC.addActionListener(e -> {
            boolean isBuyer = "Buyer".equals(roleC.getSelectedItem());
            budgetLbl.setVisible(isBuyer);  budgetF.setVisible(isBuyer);
            commLbl.setVisible(!isBuyer);   commF.setVisible(!isBuyer);
        });
        commLbl.setVisible(false);
        commF.setVisible(false);

        form.add(new JLabel("Full Name:"));  form.add(nameF);
        form.add(new JLabel("Email:"));      form.add(emailF);
        form.add(new JLabel("Password:"));   form.add(passF);
        form.add(new JLabel("Register As:")); form.add(roleC);
        form.add(budgetLbl);                  form.add(budgetF);
        form.add(commLbl);                    form.add(commF);

        JButton submit = new JButton("Register");
        submit.setBackground(PRIMARY);
        submit.setForeground(WHITE);
        submit.setFocusPainted(false);
        submit.setBorderPainted(false);
        submit.addActionListener(e -> {
            String name  = nameF.getText().trim();
            String email = emailF.getText().trim();
            String pass  = new String(passF.getPassword());
            String role  = (String) roleC.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.");
                return;
            }
            AppContext ctx = AppContext.getInstance();
            if ("Buyer".equals(role)) {
                double budget = parseSafe(budgetF.getText(), 1_000_000);
                String id = "BUY" + String.format("%03d", ctx.getBuyers().size() + 1);
                ctx.registerBuyer(new Buyer(name, email, pass, id, budget,
                        BuyerType.REGULAR, ctx.getSubdivision()));
            } else {
                double comm = parseSafe(commF.getText(), 0.05);
                String id = "AGT" + String.format("%03d", ctx.getAgents().size() + 1);
                ctx.registerAgent(new Agent(name, email, pass, id, comm, ctx.getSubdivision()));
            }
            JOptionPane.showMessageDialog(dlg, "Registration successful! You may now log in.");
            dlg.dispose();
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        btnRow.add(submit);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Helpers ──────────────────────────────────────────────────────────
    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JTextField styledTextField() {
        JTextField tf = new JTextField();
        styleInput(tf);
        return tf;
    }

    private void styleInput(JTextField tf) {
        tf.setMaximumSize(new Dimension(320, 36));
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setAlignmentX(Component.CENTER_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(320, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private double parseSafe(String text, double defaultVal) {
        try { return Double.parseDouble(text.trim()); }
        catch (NumberFormatException ex) { return defaultVal; }
    }

    // NetBeans designer stubs — kept so the .form file doesn't break on open
    @SuppressWarnings("unchecked")
    private void initComponents() { /* unused — UI built in buildUI() */ }
}
