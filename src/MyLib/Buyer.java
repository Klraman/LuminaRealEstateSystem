package MyLib;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends User {

    private String buyerId;
    private double budget;
    private BuyerType buyerType;   // belongs in Buyer, NOT in Discount
    private Subdivision subdivision;
    private List<Transaction> myTransactions;

    public Buyer(String name, String email, String password,
                 String buyerId, double budget, BuyerType buyerType,
                 Subdivision subdivision) {
        super(name, email, password);
        this.buyerId = buyerId;
        this.budget = budget;
        this.buyerType = buyerType;
        this.subdivision = subdivision;
        this.myTransactions = new ArrayList<>();
    }

    public boolean register() {
        System.out.println("Buyer " + getName() + " registered with ID: " + buyerId);
        return true;
    }

    @Override
    public boolean login() {
        System.out.println("Buyer " + getName() + " logged in.");
        return true;
    }

    public List<Lot> viewAvailableLots() {
        List<Lot> available = new ArrayList<>();
        for (Block block : subdivision.getBlocks()) {
            available.addAll(block.getAvailableLots());
        }
        return available;
    }

    public List<Transaction> viewMyTransactions() {
        return new ArrayList<>(myTransactions);
    }

    // Returns the monthly amortization for a given lot and loan term
    public double computeAffordability(Lot lot, int years) {
        if (lot.getHouseModel() == null) return 0;
        return lot.getHouseModel().getMonthlyAmort(years);
    }

    public void addTransaction(Transaction t) {
        myTransactions.add(t);
    }

    public String getBuyerId()       { return buyerId; }
    public double getBudget()        { return budget; }
    public BuyerType getBuyerType()  { return buyerType; }
    public void setBudget(double b)  { this.budget = b; }
}
