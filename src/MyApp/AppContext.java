package MyApp;
import MyLib.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton holding all shared application state:
 * subdivision, catalog, user lists, and transaction log.
 */
public class AppContext {

    private static AppContext instance;

    private final Subdivision subdivision;
    private final PropertyCatalog catalog;
    private final List<Admin> admins       = new ArrayList<>();
    private final List<Agent> agents       = new ArrayList<>();
    private final List<Buyer> buyers       = new ArrayList<>();
    private final List<Transaction> allTransactions = new ArrayList<>();

    private AppContext() {
        catalog     = new PropertyCatalog();
        subdivision = new Subdivision("Lumina Homes Quezon", 5);
        subdivision.generateInventory();
        assignModelsToLots();
        seedDemoUsers();
    }

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    // ------------------------------------------------------------------
    // Lot-model assignment
    // Blk 1-2 -> Angelique variants, Blk 3-4 -> Angeli, Blk 5 -> Aimee
    // ------------------------------------------------------------------
    private void assignModelsToLots() {
        PropertyUnit ai  = catalog.getUnitByName("Angelique Inner");
        PropertyUnit ae  = catalog.getUnitByName("Angelique End");
        PropertyUnit ali = catalog.getUnitByName("Angeli Inner");
        PropertyUnit ale = catalog.getUnitByName("Angeli End");
        PropertyUnit ami = catalog.getUnitByName("Aimee Inner");
        PropertyUnit ame = catalog.getUnitByName("Aimee End");

        for (Block block : subdivision.getBlocks()) {
            int b = block.getBlockNum();
            for (Lot lot : block.getLots()) {
                int l = lot.getLotNum();
                PropertyUnit model;
                if      (b <= 2) model = (l % 2 == 1) ? ai  : ae;
                else if (b <= 4) model = (l % 2 == 1) ? ali : ale;
                else             model = (l <= 10)     ? ami : ame;
                lot.setHouseModel(model);
            }
        }
    }

    // ------------------------------------------------------------------
    // Demo users — one per role for quick testing
    // ------------------------------------------------------------------
    private void seedDemoUsers() {
        admins.add(new Admin("Admin User", "admin@lumina.com", "admin123",
                "ADM001", subdivision, allTransactions));
        Agent demoAgent = new Agent("Marcus Villanueva", "agent@lumina.com", "agent123",
                "AGT001", 0.05, subdivision);
        agents.add(demoAgent);

        Buyer demoBuyer = new Buyer("Kevin Segismundo", "buyer@lumina.com", "buyer123",
                "BUY001", 1_000_000, BuyerType.REGULAR, subdivision);
        buyers.add(demoBuyer);
        seedDummyTransactions(demoBuyer, demoAgent);
    }

    // ------------------------------------------------------------------
    // Dummy transactions for demo buyer — realistic Lumina pricelist values
    // ------------------------------------------------------------------
    private void seedDummyTransactions(Buyer buyer, Agent agent) {
        // TXN 1 — Block 1 Lot 1 · Angelique Inner · Cash · COMPLETED
        Lot lot1 = subdivision.findLot(1, 1);
        Transaction t1 = new Transaction(buyer, lot1, new Cash(805_000), null);
        t1.setTransactionStatus(Status.COMPLETED);
        lot1.updateStatus(Status.COMPLETED);
        t1.setAssignedAgent(agent);
        buyer.addTransaction(t1);
        agent.addPendingTransaction(t1);
        allTransactions.add(t1);

        // TXN 2 — Block 3 Lot 3 · Angeli Inner · EMI 20yr · RESERVED
        Lot lot2 = subdivision.findLot(3, 3);
        EMI emi = new EMI("EMI-2025-003", 20, lot2.getHouseModel());
        Transaction t2 = new Transaction(buyer, lot2, emi, null);
        t2.setTransactionStatus(Status.RESERVED);
        lot2.updateStatus(Status.RESERVED);
        t2.setAssignedAgent(agent);
        buyer.addTransaction(t2);
        agent.addPendingTransaction(t2);
        allTransactions.add(t2);

        // TXN 3 — Block 5 Lot 13 · Aimee End · Pag-IBIG · First-Time 5% discount · COMPLETED
        Lot lot3 = subdivision.findLot(5, 13);
        Pagibig pagibig = new Pagibig("MBR-00123456", "Housing", 499_000);
        pagibig.setApproved(true);
        Transaction t3 = new Transaction(buyer, lot3, pagibig, new FirstTimeDiscount());
        t3.setTransactionStatus(Status.COMPLETED);
        lot3.updateStatus(Status.COMPLETED);
        t3.setAssignedAgent(agent);
        buyer.addTransaction(t3);
        agent.addPendingTransaction(t3);
        allTransactions.add(t3);
    }

    // ------------------------------------------------------------------
    // Authentication
    // ------------------------------------------------------------------
    public User authenticate(String email, String password, String role) {
        switch (role) {
            case "Admin":
                for (Admin a : admins)
                    if (a.getEmail().equals(email) && a.getPassword().equals(password)) return a;
                break;
            case "Agent":
                for (Agent a : agents)
                    if (a.getEmail().equals(email) && a.getPassword().equals(password)) return a;
                break;
            case "Buyer":
                for (Buyer b : buyers)
                    if (b.getEmail().equals(email) && b.getPassword().equals(password)) return b;
                break;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Registration helpers
    // ------------------------------------------------------------------
    public void registerAgent(Agent agent)    { agents.add(agent); }
    public void registerBuyer(Buyer buyer)    { buyers.add(buyer); }
    public void addTransaction(Transaction t) { allTransactions.add(t); }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    public Subdivision           getSubdivision()     { return subdivision; }
    public PropertyCatalog       getCatalog()          { return catalog; }
    public List<Admin>           getAdmins()           { return admins; }
    public List<Agent>           getAgents()           { return agents; }
    public List<Buyer>           getBuyers()           { return buyers; }
    public List<Transaction>     getAllTransactions()  { return allTransactions; }
}
