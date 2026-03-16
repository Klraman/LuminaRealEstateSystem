import MyLib.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminPage extends javax.swing.JPanel {

    private static final Color PRIMARY = new Color(26, 58, 92);
    private static final Color GOLD    = new Color(212, 175, 55);
    private static final Color BG      = new Color(248, 249, 252);

    private final MainJFrame mainFrame;
    private final Admin      admin;
    private final AppContext  ctx;

    // Tab 1 – Dashboard
    private JLabel totalLbl, availLbl, reservedLbl, soldLbl;

    // Tab 2 – Inventory
    private DefaultTableModel invModel;

    // Tab 3 – All Transactions
    private DefaultTableModel txnModel;

    public AdminPage(MainJFrame mainFrame, Admin admin) {
        this.mainFrame = mainFrame;
        this.admin     = admin;
        this.ctx       = AppContext.getInstance();

        setLayout(new BorderLayout());
        setBackground(BG);
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
    }

    // ── Header ────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);

        JLabel title = new JLabel("LUMINA  ·  Admin Panel");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Administrator: " + admin.getName()
                + "  |  ID: " + admin.getAdminId());
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(190, 215, 240));

        info.add(title);
        info.add(sub);

        JButton logout = headerBtn("Logout", GOLD);
        logout.addActionListener(e -> mainFrame.showLogin());

        header.add(info,   BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
        return header;
    }

    // ── Tabs ──────────────────────────────────────────────────────────────
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Dashboard",        buildDashboardTab());
        tabs.addTab("Inventory",        buildInventoryTab());
        tabs.addTab("All Transactions", buildTransactionsTab());
        return tabs;
    }

    // ── Tab 1: Dashboard ──────────────────────────────────────────────────
    private JPanel buildDashboardTab() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(12, 12, 12, 12);
        g.fill   = GridBagConstraints.BOTH;

        JLabel heading = new JLabel("Subdivision Overview — " + ctx.getSubdivision().getName());
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setForeground(PRIMARY);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 4;
        outer.add(heading, g);
        g.gridwidth = 1;

        // Stat cards
        totalLbl    = bigStatLabel("—");
        availLbl    = bigStatLabel("—");
        reservedLbl = bigStatLabel("—");
        soldLbl     = bigStatLabel("—");

        g.gridy = 1;
        g.gridx = 0; outer.add(statCard("Total Lots",   totalLbl,    new Color(44, 83, 130)),  g);
        g.gridx = 1; outer.add(statCard("Available",    availLbl,    new Color(34, 120, 34)),   g);
        g.gridx = 2; outer.add(statCard("Reserved",     reservedLbl, new Color(180, 130, 20)),  g);
        g.gridx = 3; outer.add(statCard("Sold",         soldLbl,     new Color(160, 40, 40)),   g);

        // Users summary
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        userRow.setOpaque(false);
        userRow.add(inlineLabel("Registered Agents: " + ctx.getAgents().size()));
        userRow.add(inlineLabel("Registered Buyers: " + ctx.getBuyers().size()));
        userRow.add(inlineLabel("Total Transactions: " + ctx.getAllTransactions().size()));

        g.gridy = 2; g.gridx = 0; g.gridwidth = 4;
        outer.add(userRow, g);

        // Refresh button
        JButton refresh = actionBtn("⟳  Refresh Dashboard");
        refresh.addActionListener(e -> refreshDashboard());
        g.gridy = 3; g.gridwidth = 1; g.gridx = 0;
        outer.add(refresh, g);

        refreshDashboard();
        return outer;
    }

    private void refreshDashboard() {
        int total = 0, avail = 0, reserved = 0, sold = 0;
        for (Block block : ctx.getSubdivision().getBlocks()) {
            for (Lot lot : block.getLots()) {
                total++;
                switch (lot.getTransactionStatus()) {
                    case PENDING   -> avail++;
                    case RESERVED  -> reserved++;
                    case COMPLETED -> sold++;
                    default        -> {}
                }
            }
        }
        totalLbl.setText(String.valueOf(total));
        availLbl.setText(String.valueOf(avail));
        reservedLbl.setText(String.valueOf(reserved));
        soldLbl.setText(String.valueOf(sold));
    }

    // ── Tab 2: Inventory ──────────────────────────────────────────────────
    private JPanel buildInventoryTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BG);

        String[] cols = {"Block", "Lot #", "Model", "Total Contract Price",
                         "Floor Area", "Lot Area", "Phase", "Status"};
        invModel = nonEditableModel(cols);
        JTable table = styledTable(invModel);
        setColWidths(table, 55, 55, 140, 170, 90, 80, 90, 90);

        JButton refresh = actionBtn("⟳  Refresh Inventory");
        refresh.addActionListener(e -> populateInventory());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.add(refresh);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populateInventory();
        return panel;
    }

    private void populateInventory() {
        invModel.setRowCount(0);
        for (Block block : ctx.getSubdivision().getBlocks()) {
            for (Lot lot : block.getLots()) {
                PropertyUnit u = lot.getHouseModel();
                invModel.addRow(new Object[]{
                    lot.getBlkNum(),
                    lot.getLotNum(),
                    u != null ? u.getModelName() : "—",
                    u != null ? "₱" + String.format("%,.0f", u.getEstimatedTCP()) : "—",
                    u != null ? u.getFloorArea() + " sqm" : "—",
                    u != null ? u.getLotArea()   + " sqm" : "—",
                    u != null ? u.getPhase() : "—",
                    lot.getTransactionStatus()
                });
            }
        }
    }

    // ── Tab 3: All Transactions ───────────────────────────────────────────
    private JPanel buildTransactionsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BG);

        String[] cols = {"TXN ID", "Buyer", "Block", "Lot #", "Model",
                         "Final TCP", "Payment Method", "Discount", "Status"};
        txnModel = nonEditableModel(cols);
        JTable table = styledTable(txnModel);
        setColWidths(table, 90, 130, 55, 55, 130, 150, 120, 100, 90);

        JButton refresh = actionBtn("⟳  Refresh");
        refresh.addActionListener(e -> populateTransactions());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.add(refresh);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populateTransactions();
        return panel;
    }

    private void populateTransactions() {
        txnModel.setRowCount(0);
        for (Transaction t : ctx.getAllTransactions()) {
            Lot lot = t.getLot();
            String pmName  = t.getPaymentMethod() != null
                    ? t.getPaymentMethod().getClass().getSimpleName() : "—";
            String discStr = t.getDiscount() != null
                    ? t.getDiscount().getDiscountDescription() : "None";
            txnModel.addRow(new Object[]{
                t.getTransactionID(),
                t.getBuyer() != null ? t.getBuyer().getName() : "—",
                lot != null ? lot.getBlkNum() : "—",
                lot != null ? lot.getLotNum() : "—",
                lot != null && lot.getHouseModel() != null ? lot.getHouseModel().getModelName() : "—",
                "₱" + String.format("%,.2f", t.getFinalTCP()),
                pmName,
                discStr,
                t.getTransactionStatus()
            });
        }
    }

    // ── Shared UI helpers ─────────────────────────────────────────────────
    private JPanel statCard(String title, JLabel valueLbl, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        card.setPreferredSize(new Dimension(190, 90));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);

        valueLbl.setForeground(accent);
        card.add(t);
        card.add(valueLbl);
        return card;
    }

    private JLabel bigStatLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        return lbl;
    }

    private JLabel inlineLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JButton headerBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }

    private JButton actionBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.PLAIN, 12));
        return b;
    }

    private DefaultTableModel nonEditableModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(26);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setSelectionBackground(new Color(210, 228, 252));
        t.setGridColor(new Color(220, 220, 220));
        t.setShowGrid(true);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.getTableHeader().setBackground(PRIMARY);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    private void setColWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() { /* unused */ }
}
