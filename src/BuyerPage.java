import MyLib.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class BuyerPage extends javax.swing.JPanel {

    private static final Color PRIMARY = new Color(26, 58, 92);
    private static final Color GOLD    = new Color(212, 175, 55);
    private static final Color BG      = new Color(248, 249, 252);

    private final MainJFrame mainFrame;
    private final Buyer      buyer;
    private final AppContext  ctx;

    // Available Lots tab
    private DefaultTableModel lotModel;

    // Affordability tab
    private JComboBox<String>  affordLotCombo;
    private JComboBox<Integer> affordYearsCombo;
    private JLabel             affordResult;
    private JLabel             affordDetails;

    // My Transactions tab
    private DefaultTableModel txnModel;

    public BuyerPage(MainJFrame mainFrame, Buyer buyer) {
        this.mainFrame = mainFrame;
        this.buyer     = buyer;
        this.ctx       = AppContext.getInstance();

        setLayout(new BorderLayout());
        setBackground(BG);
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
    }

    // ── Header bar ───────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);

        JLabel title = new JLabel("LUMINA  ·  Buyer Portal");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Logged in as: " + buyer.getName()
                + "  |  Budget: ₱" + String.format("%,.0f", buyer.getBudget())
                + "  |  Type: " + buyer.getBuyerType());
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

    // ── Tabs ─────────────────────────────────────────────────────────────
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Available Lots",            buildAvailableLotsTab());
        tabs.addTab("Affordability Calculator",  buildAffordabilityTab());
        tabs.addTab("My Transactions",           buildTransactionsTab());
        return tabs;
    }

    // ── Tab 1: Available Lots ────────────────────────────────────────────
    private JPanel buildAvailableLotsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BG);

        String[] cols = {"Block", "Lot #", "Model", "Total Contract Price",
                         "Floor Area", "Lot Area", "Phase", "Status"};
        lotModel = nonEditableModel(cols);

        JTable table = styledTable(lotModel);
        setColWidths(table, 55, 55, 140, 170, 90, 80, 90, 90);

        JButton refresh = actionBtn("⟳  Refresh");
        refresh.addActionListener(e -> populateLotTable());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.add(refresh);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populateLotTable();
        return panel;
    }

    private void populateLotTable() {
        lotModel.setRowCount(0);
        for (Lot lot : buyer.viewAvailableLots()) {
            PropertyUnit u = lot.getHouseModel();
            lotModel.addRow(new Object[]{
                lot.getBlkNum(),
                lot.getLotNum(),
                u != null ? u.getModelName()                              : "—",
                u != null ? "₱" + String.format("%,.0f", u.getEstimatedTCP()) : "—",
                u != null ? u.getFloorArea() + " sqm"                    : "—",
                u != null ? u.getLotArea()   + " sqm"                    : "—",
                u != null ? u.getPhase()                                  : "—",
                lot.getTransactionStatus()
            });
        }
        // Keep affordability combo in sync
        if (affordLotCombo != null) rebuildAffordCombo();
    }

    // ── Tab 2: Affordability Calculator ──────────────────────────────────
    private JPanel buildAffordabilityTab() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.NONE;

        JLabel heading = new JLabel("Monthly Amortization Calculator");
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setForeground(PRIMARY);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        outer.add(heading, g);

        // Lot selector
        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        outer.add(new JLabel("Select Lot:"), g);
        affordLotCombo = new JComboBox<>();
        affordLotCombo.setPreferredSize(new Dimension(320, 30));
        g.gridx = 1;
        outer.add(affordLotCombo, g);

        // Years selector
        g.gridy = 2; g.gridx = 0;
        outer.add(new JLabel("Loan Term (years):"), g);
        affordYearsCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20, 25, 30});
        affordYearsCombo.setPreferredSize(new Dimension(100, 30));
        g.gridx = 1;
        outer.add(affordYearsCombo, g);

        // Compute button
        JButton compute = actionBtn("Compute");
        compute.setPreferredSize(new Dimension(130, 36));
        g.gridy = 3; g.gridx = 1; g.anchor = GridBagConstraints.EAST;
        outer.add(compute, g);

        // Result
        affordResult = new JLabel(" ");
        affordResult.setFont(new Font("Arial", Font.BOLD, 24));
        affordResult.setForeground(PRIMARY);
        g.gridy = 4; g.gridx = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        outer.add(affordResult, g);

        affordDetails = new JLabel(" ");
        affordDetails.setFont(new Font("Arial", Font.PLAIN, 13));
        affordDetails.setForeground(Color.GRAY);
        g.gridy = 5;
        outer.add(affordDetails, g);

        compute.addActionListener(e -> computeAffordability());
        rebuildAffordCombo();
        return outer;
    }

    private void rebuildAffordCombo() {
        if (affordLotCombo == null) return;
        affordLotCombo.removeAllItems();
        for (Lot lot : buyer.viewAvailableLots()) {
            if (lot.getHouseModel() != null)
                affordLotCombo.addItem("Blk " + lot.getBlkNum()
                        + " Lot " + lot.getLotNum()
                        + "  —  " + lot.getHouseModel().getModelName());
        }
    }

    private void computeAffordability() {
        if (affordLotCombo.getSelectedIndex() < 0) return;
        List<Lot> available = buyer.viewAvailableLots();
        int idx = affordLotCombo.getSelectedIndex();
        if (idx >= available.size()) return;

        Lot lot  = available.get(idx);
        int years = (Integer) affordYearsCombo.getSelectedItem();
        double monthly = buyer.computeAffordability(lot, years);
        PropertyUnit u = lot.getHouseModel();

        affordResult.setText("₱" + String.format("%,.2f", monthly) + " / month");
        affordDetails.setText("TCP: ₱" + String.format("%,.0f", u.getEstimatedTCP())
                + "  |  Loanable: ₱" + String.format("%,.0f", u.getLoanableAmount())
                + "  |  Rate: " + (u.getInterestRate() * 100) + "%");
    }

    // ── Tab 3: My Transactions ───────────────────────────────────────────
    private JPanel buildTransactionsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BG);

        String[] cols = {"TXN ID", "Block", "Lot #", "Model", "Final TCP", "Status"};
        txnModel = nonEditableModel(cols);

        JTable table = styledTable(txnModel);
        setColWidths(table, 90, 60, 60, 140, 160, 100);

        JButton refresh = actionBtn("⟳  Refresh");
        refresh.addActionListener(e -> populateTxnTable());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        toolbar.setOpaque(false);
        toolbar.add(refresh);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populateTxnTable();
        return panel;
    }

    private void populateTxnTable() {
        txnModel.setRowCount(0);
        for (Transaction t : buyer.viewMyTransactions()) {
            Lot lot = t.getLot();
            txnModel.addRow(new Object[]{
                t.getTransactionID(),
                lot != null ? lot.getBlkNum() : "—",
                lot != null ? lot.getLotNum() : "—",
                lot != null && lot.getHouseModel() != null ? lot.getHouseModel().getModelName() : "—",
                "₱" + String.format("%,.2f", t.getFinalTCP()),
                t.getTransactionStatus()
            });
        }
    }

    // ── Shared UI helpers ────────────────────────────────────────────────
    private JButton headerBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }

    private JButton actionBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
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
