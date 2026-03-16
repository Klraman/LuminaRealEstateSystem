import MyLib.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AgentPage extends javax.swing.JPanel {

    private static final Color PRIMARY = new Color(26, 58, 92);
    private static final Color GOLD    = new Color(212, 175, 55);
    private static final Color GREEN   = new Color(34, 139, 34);
    private static final Color BG      = new Color(248, 249, 252);

    private final MainJFrame mainFrame;
    private final Agent      agent;
    private final AppContext  ctx;

    // Tab 1 – Available Lots
    private DefaultTableModel lotModel;
    private JTextField        filterBudgetField;
    private JComboBox<String> filterBlockCombo;

    // Tab 2 – Lot Actions
    private JComboBox<String> buyerCombo;
    private JTextField        actionBlkField;
    private JTextField        actionLotField;
    private JComboBox<String> actionCombo;       // Reserve / Sell
    private JComboBox<String> paymentCombo;
    private JTextField        paymentRefField;
    private JComboBox<String> discountCombo;
    private JLabel            actionResultLbl;

    // Tab 3 – Amortization Table
    private JComboBox<String>  amortModelCombo;
    private DefaultTableModel  amortModel;

    // Tab 4 – My Performance
    private JLabel             commissionLbl;
    private DefaultTableModel  perfModel;

    public AgentPage(MainJFrame mainFrame, Agent agent) {
        this.mainFrame = mainFrame;
        this.agent     = agent;
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

        JLabel title = new JLabel("LUMINA  ·  Agent Portal");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Agent: " + agent.getName()
                + "  |  ID: " + agent.getAgentId()
                + "  |  Commission: " + (agent.getCommissionRate() * 100) + "%");
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
        tabs.addTab("Available Lots",      buildAvailableLotsTab());
        tabs.addTab("Lot Actions",         buildLotActionsTab());
        tabs.addTab("Amortization Table",  buildAmortizationTab());
        tabs.addTab("My Performance",      buildPerformanceTab());
        return tabs;
    }

    // ── Tab 1: Available Lots ─────────────────────────────────────────────
    private JPanel buildAvailableLotsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BG);

        // Filter toolbar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.setOpaque(false);

        filterBudgetField = new JTextField("0", 10);
        filterBlockCombo  = new JComboBox<>(new String[]{"All Blocks", "1", "2", "3", "4", "5"});

        JButton filter  = actionBtn("Filter");
        JButton showAll = actionBtn("Show All");

        filter.addActionListener(e -> applyLotFilter());
        showAll.addActionListener(e -> populateLotTable(agent.viewAvailableLots()));

        filterBar.add(new JLabel("Max Budget (₱):"));
        filterBar.add(filterBudgetField);
        filterBar.add(new JLabel("Block:"));
        filterBar.add(filterBlockCombo);
        filterBar.add(filter);
        filterBar.add(showAll);

        String[] cols = {"Block", "Lot #", "Model", "Total Contract Price",
                         "Floor Area", "Lot Area", "Phase", "Status"};
        lotModel = nonEditableModel(cols);
        JTable table = styledTable(lotModel);
        setColWidths(table, 55, 55, 140, 170, 90, 80, 90, 90);

        panel.add(filterBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populateLotTable(agent.viewAvailableLots());
        return panel;
    }

    private void applyLotFilter() {
        double maxBudget = parseSafe(filterBudgetField.getText(), 0);
        int blockNum = 0;
        String sel = (String) filterBlockCombo.getSelectedItem();
        if (!"All Blocks".equals(sel)) blockNum = Integer.parseInt(sel);
        populateLotTable(agent.filterLots(maxBudget, blockNum));
    }

    private void populateLotTable(List<Lot> lots) {
        lotModel.setRowCount(0);
        for (Lot lot : lots) {
            PropertyUnit u = lot.getHouseModel();
            lotModel.addRow(new Object[]{
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

    // ── Tab 2: Lot Actions ────────────────────────────────────────────────
    private JPanel buildLotActionsTab() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 40, 16, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(6, 8, 6, 8);
        g.anchor  = GridBagConstraints.WEST;
        g.fill    = GridBagConstraints.NONE;

        // Section label
        JLabel heading = sectionLabel("Create Reservation or Process Sale");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 4;
        outer.add(heading, g);
        g.gridwidth = 1;

        // Buyer selector
        g.gridy = 1; g.gridx = 0;
        outer.add(new JLabel("Buyer:"), g);
        buyerCombo = new JComboBox<>();
        refreshBuyerCombo();
        buyerCombo.setPreferredSize(new Dimension(220, 30));
        g.gridx = 1;
        outer.add(buyerCombo, g);

        JButton refreshBuyers = actionBtn("⟳");
        refreshBuyers.setToolTipText("Refresh buyer list");
        refreshBuyers.addActionListener(e -> refreshBuyerCombo());
        g.gridx = 2;
        outer.add(refreshBuyers, g);

        // Block / Lot fields
        g.gridy = 2; g.gridx = 0;
        outer.add(new JLabel("Block #:"), g);
        actionBlkField = new JTextField(4);
        g.gridx = 1;
        outer.add(actionBlkField, g);

        g.gridy = 3; g.gridx = 0;
        outer.add(new JLabel("Lot #:"), g);
        actionLotField = new JTextField(4);
        g.gridx = 1;
        outer.add(actionLotField, g);

        // Action type
        g.gridy = 4; g.gridx = 0;
        outer.add(new JLabel("Action:"), g);
        actionCombo = new JComboBox<>(new String[]{"Reserve", "Sell (Complete)"});
        actionCombo.setPreferredSize(new Dimension(180, 30));
        g.gridx = 1;
        outer.add(actionCombo, g);

        // Payment method
        g.gridy = 5; g.gridx = 0;
        outer.add(new JLabel("Payment Method:"), g);
        paymentCombo = new JComboBox<>(new String[]{"Cash", "Bank", "Pag-IBIG", "EMI (In-House)"});
        paymentCombo.setPreferredSize(new Dimension(180, 30));
        g.gridx = 1;
        outer.add(paymentCombo, g);

        // Payment reference
        g.gridy = 6; g.gridx = 0;
        outer.add(new JLabel("Ref / Amount:"), g);
        paymentRefField = new JTextField(15);
        paymentRefField.setToolTipText("Cash/Bank: amount  |  Pag-IBIG: membership ID  |  EMI: account no.");
        g.gridx = 1; g.gridwidth = 2;
        outer.add(paymentRefField, g);
        g.gridwidth = 1;

        // Discount
        g.gridy = 7; g.gridx = 0;
        outer.add(new JLabel("Discount:"), g);
        discountCombo = new JComboBox<>(new String[]{"None", "PWD (20%)", "Senior Citizen (20%)", "First Time (5%)"});
        discountCombo.setPreferredSize(new Dimension(200, 30));
        g.gridx = 1;
        outer.add(discountCombo, g);

        // Execute button
        JButton execute = new JButton("Execute Transaction");
        execute.setBackground(new Color(34, 100, 34));
        execute.setForeground(Color.WHITE);
        execute.setFocusPainted(false);
        execute.setBorderPainted(false);
        execute.setFont(new Font("Arial", Font.BOLD, 13));
        execute.setPreferredSize(new Dimension(200, 38));
        execute.addActionListener(e -> executeTransaction());
        g.gridy = 8; g.gridx = 0; g.gridwidth = 3; g.anchor = GridBagConstraints.CENTER;
        outer.add(execute, g);

        // Result label
        actionResultLbl = new JLabel(" ");
        actionResultLbl.setFont(new Font("Arial", Font.BOLD, 13));
        g.gridy = 9; g.gridx = 0; g.gridwidth = 4;
        outer.add(actionResultLbl, g);

        return outer;
    }

    private void refreshBuyerCombo() {
        if (buyerCombo == null) return;
        buyerCombo.removeAllItems();
        for (Buyer b : ctx.getBuyers())
            buyerCombo.addItem(b.getName() + " (" + b.getBuyerId() + ")");
    }

    private void executeTransaction() {
        // Validate inputs
        if (buyerCombo.getSelectedIndex() < 0) { flashResult("Select a buyer.", Color.RED); return; }
        int blk, lot;
        try {
            blk = Integer.parseInt(actionBlkField.getText().trim());
            lot = Integer.parseInt(actionLotField.getText().trim());
        } catch (NumberFormatException ex) {
            flashResult("Enter valid Block and Lot numbers.", Color.RED);
            return;
        }

        Lot targetLot = ctx.getSubdivision().findLot(blk, lot);
        if (targetLot == null) { flashResult("Lot not found.", Color.RED); return; }

        Buyer targetBuyer = ctx.getBuyers().get(buyerCombo.getSelectedIndex());

        // Build PaymentMethod
        String payRef    = paymentRefField.getText().trim();
        PaymentMethod pm = buildPaymentMethod(payRef, targetLot);

        // Build Discount
        Discount discount = buildDiscount();

        // Execute via Agent (Buyers cannot create transactions directly)
        String action = (String) actionCombo.getSelectedItem();
        Transaction txn;
        if ("Reserve".equals(action)) {
            txn = agent.createReservation(targetBuyer, targetLot);
        } else {
            txn = agent.processSale(targetBuyer, targetLot);
        }

        if (txn == null) { flashResult("Transaction failed — lot may not be available.", Color.RED); return; }

        txn.setPaymentMethod(pm);
        txn.setDiscount(discount);
        ctx.addTransaction(txn);
        targetBuyer.addTransaction(txn);

        String tag = "Reserve".equals(action) ? "Reserved" : "Sold";
        flashResult("✓ " + tag + "  TXN#" + txn.getTransactionID()
                + "  Final TCP: ₱" + String.format("%,.2f", txn.getFinalTCP()), GREEN);

        // Refresh lots tab
        populateLotTable(agent.viewAvailableLots());
    }

    private PaymentMethod buildPaymentMethod(String ref, Lot lot) {
        String type = (String) paymentCombo.getSelectedItem();
        double amt  = parseSafe(ref, 0);
        switch (type) {
            case "Bank":          return new Bank(amt);
            case "Pag-IBIG":      return new Pagibig(ref.isEmpty() ? "PAGIBIG-000" : ref, "Housing", amt);
            case "EMI (In-House)":
                PropertyUnit u = lot.getHouseModel();
                return new EMI(ref.isEmpty() ? "EMI-001" : ref, 20, u);
            default:              return new Cash(amt);  // Cash
        }
    }

    private Discount buildDiscount() {
        String sel = (String) discountCombo.getSelectedItem();
        switch (sel) {
            case "PWD (20%)":            return new PWDDiscount();
            case "Senior Citizen (20%)": return new SeniorCitizenDiscount();
            case "First Time (5%)":      return new FirstTimeDiscount();
            default:                     return null;
        }
    }

    private void flashResult(String msg, Color color) {
        actionResultLbl.setText(msg);
        actionResultLbl.setForeground(color);
    }

    // ── Tab 3: Amortization Table ─────────────────────────────────────────
    private JPanel buildAmortizationTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        panel.setBackground(BG);

        // Model picker row
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);

        amortModelCombo = new JComboBox<>();
        for (PropertyUnit u : ctx.getCatalog().getAllUnits())
            amortModelCombo.addItem(u.getModelName());

        JButton show = actionBtn("Show Table");
        show.addActionListener(e -> buildAmortTable());

        top.add(new JLabel("Model:"));
        top.add(amortModelCombo);
        top.add(show);

        // Table
        String[] cols = {"Loan Term (years)", "Monthly Amortization (Php)"};
        amortModel = nonEditableModel(cols);
        JTable table = styledTable(amortModel);
        setColWidths(table, 160, 200);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        // Spec panel beneath table
        JLabel specLbl = new JLabel(" ");
        specLbl.setFont(new Font("Arial", Font.ITALIC, 12));
        specLbl.setForeground(Color.GRAY);
        specLbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        amortModelCombo.addActionListener(e -> {
            buildAmortTable();
            String name = (String) amortModelCombo.getSelectedItem();
            if (name == null) return;
            PropertyUnit u = ctx.getCatalog().getUnitByName(name);
            if (u != null)
                specLbl.setText("TCP: ₱" + String.format("%,.0f", u.getEstimatedTCP())
                        + "  |  Loanable: ₱" + String.format("%,.0f", u.getLoanableAmount())
                        + "  |  Rate: " + (u.getInterestRate() * 100) + "% p.a."
                        + "  |  Reservation: ₱" + String.format("%,.0f", u.getReservationFee())
                        + "  |  Down Payment: ₱" + String.format("%,.0f", u.getDownPayment()));
        });

        panel.add(top,     BorderLayout.NORTH);
        panel.add(center,  BorderLayout.CENTER);
        panel.add(specLbl, BorderLayout.SOUTH);

        buildAmortTable();
        return panel;
    }

    private void buildAmortTable() {
        if (amortModelCombo == null || amortModel == null) return;
        String name = (String) amortModelCombo.getSelectedItem();
        if (name == null) return;
        PropertyUnit u = ctx.getCatalog().getUnitByName(name);
        if (u == null) return;

        amortModel.setRowCount(0);
        for (int yr : new int[]{5, 10, 15, 20, 25, 30})
            amortModel.addRow(new Object[]{
                yr + " years",
                "₱" + String.format("%,.2f", u.getMonthlyAmort(yr))
            });
    }

    // ── Tab 4: My Performance ─────────────────────────────────────────────
    private JPanel buildPerformanceTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        panel.setBackground(BG);

        // Commission summary card
        JPanel card = new JPanel(new GridLayout(1, 3, 20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235)),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));

        commissionLbl = statLabel("₱0.00");
        card.add(statCard("Total Commission Earned", commissionLbl));
        JLabel txnCountLbl = statLabel("0");
        card.add(statCard("Transactions Handled", txnCountLbl));
        JLabel rateLbl = statLabel(agent.getCommissionRate() * 100 + "%");
        card.add(statCard("Commission Rate", rateLbl));

        // Refresh button
        JButton refresh = actionBtn("⟳  Refresh");
        refresh.addActionListener(e -> {
            commissionLbl.setText("₱" + String.format("%,.2f", agent.computeCommission()));
            txnCountLbl.setText(String.valueOf(agent.getHandledTransactions().size()));
            populatePerfTable();
        });

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(card, BorderLayout.CENTER);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrap.setOpaque(false);
        btnWrap.add(refresh);
        topRow.add(btnWrap, BorderLayout.SOUTH);

        // Transaction history table
        String[] cols = {"TXN ID", "Buyer", "Block", "Lot #", "Model", "Final TCP", "Status"};
        perfModel = nonEditableModel(cols);
        JTable table = styledTable(perfModel);
        setColWidths(table, 90, 140, 55, 55, 130, 150, 100);

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        populatePerfTable();
        return panel;
    }

    private void populatePerfTable() {
        if (perfModel == null) return;
        perfModel.setRowCount(0);
        for (Transaction t : agent.getHandledTransactions()) {
            Lot lot = t.getLot();
            perfModel.addRow(new Object[]{
                t.getTransactionID(),
                t.getBuyer() != null ? t.getBuyer().getName() : "—",
                lot != null ? lot.getBlkNum() : "—",
                lot != null ? lot.getLotNum() : "—",
                lot != null && lot.getHouseModel() != null ? lot.getHouseModel().getModelName() : "—",
                "₱" + String.format("%,.2f", t.getFinalTCP()),
                t.getTransactionStatus()
            });
        }
    }

    // ── Shared UI helpers ─────────────────────────────────────────────────
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    private JPanel statCard(String title, JLabel valueLabel) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);
        p.add(t);
        p.add(valueLabel);
        return p;
    }

    private JLabel statLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setForeground(PRIMARY);
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

    private double parseSafe(String text, double defaultVal) {
        try { return Double.parseDouble(text.trim()); }
        catch (NumberFormatException ex) { return defaultVal; }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() { /* unused */ }
}
