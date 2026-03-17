package MyLib;

import java.util.ArrayList;
import java.util.List;

public class Agent extends User {

    private String agentId;
    private double commissionRate;
    private Subdivision subdivision;
    private List<Transaction> handledTransactions;

    public Agent(String name, String email, String password,
                 String agentId, double commissionRate, Subdivision subdivision) {
        super(name, email, password);
        this.agentId = agentId;
        this.commissionRate = commissionRate;
        this.subdivision = subdivision;
        this.handledTransactions = new ArrayList<>();
    }

    @Override
    public boolean login() {
        System.out.println("Agent " + getName() + " logged in.");
        return true;
    }

    public List<Lot> viewAvailableLots() {
        List<Lot> available = new ArrayList<>();
        for (Block block : subdivision.getBlocks()) {
            available.addAll(block.getAvailableLots());
        }
        return available;
    }

    public List<Lot> filterLots(double maxBudget, int blockNum) {
        List<Lot> result = new ArrayList<>();
        for (Block block : subdivision.getBlocks()) {
            if (blockNum > 0 && block.getBlockNum() != blockNum) continue;
            for (Lot lot : block.getAvailableLots()) {
                if (lot.getHouseModel() != null
                        && lot.getHouseModel().getEstimatedTCP() <= maxBudget) {
                    result.add(lot);
                }
            }
        }
        return result;
    }

    // Buyers CANNOT directly create transactions — all reservations/sales go through Agent
    public Transaction createReservation(Buyer buyer, Lot lot) {
        if (!lot.getAvailability()) {
            System.out.println("Lot is not available.");
            return null;
        }
        Transaction t = new Transaction(buyer, lot, null, null);
        t.setTransactionStatus(Status.RESERVED);
        lot.updateStatus(Status.RESERVED);
        handledTransactions.add(t);
        return t;
    }

    public Transaction processSale(Buyer buyer, Lot lot) {
        if (!lot.getAvailability() && lot.getTransactionStatus() != Status.RESERVED) {
            System.out.println("Lot is not available for sale.");
            return null;
        }
        Transaction t = new Transaction(buyer, lot, null, null);
        t.setTransactionStatus(Status.COMPLETED);
        lot.updateStatus(Status.COMPLETED);
        handledTransactions.add(t);
        return t;
    }

    public void printAmortizationTable(Lot lot) {
        if (lot.getHouseModel() == null) {
            System.out.println("No house model assigned to this lot.");
            return;
        }
        PropertyUnit unit = lot.getHouseModel();
        System.out.println("=== Amortization Table: " + unit.getModelName() + " ===");
        System.out.printf("%-8s %15s%n", "Term(yr)", "Monthly (Php)");
        System.out.println("-".repeat(25));
        int[] terms = {5, 10, 15, 20, 25, 30};
        for (int yr : terms) {
            System.out.printf("%-8d %,15.0f%n", yr, unit.getMonthlyAmort(yr));
        }
    }

    public double computeCommission() {
        double total = 0;
        for (Transaction t : handledTransactions) {
            if (t.getTransactionStatus() == Status.COMPLETED
                    || t.getTransactionStatus() == Status.RESERVED) {
                total += t.getFinalTCP();
            }
        }
        return total * commissionRate;
    }

    public String generateBuyerReport() {
        if (handledTransactions.isEmpty()) return "No transactions handled.";
        StringBuilder sb = new StringBuilder("=== Buyer Report for Agent " + getName() + " ===\n");
        for (Transaction t : handledTransactions) {
            sb.append(t.getDetails()).append("\n");
        }
        return sb.toString();
    }

    // Called when buyer forwards transaction to this agent
    public void addPendingTransaction(Transaction t) {
        handledTransactions.add(t);
    }

    // Returns only PENDING transactions
    public List<Transaction> getPendingTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : handledTransactions)
            if (t.getTransactionStatus() == Status.PENDING) result.add(t);
        return result;
    }

    // Returns RESERVED + COMPLETED transactions
    public List<Transaction> getApprovedTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : handledTransactions)
            if (t.getTransactionStatus() == Status.RESERVED
                    || t.getTransactionStatus() == Status.COMPLETED)
                result.add(t);
        return result;
    }

    // Agent approves: transaction -> RESERVED, lot -> RESERVED
    public void approveTransaction(Transaction t) {
        t.setTransactionStatus(Status.RESERVED);
        if (t.getLot() != null) t.getLot().updateStatus(Status.RESERVED);
    }

    // Agent rejects: transaction -> REJECTED, lot -> AVAILABLE (available again), store remark
    public void rejectTransaction(Transaction t, String reason) {
        t.setTransactionStatus(Status.REJECTED);
        t.setRemark(reason);
        if (t.getLot() != null) t.getLot().updateStatus(Status.AVAILABLE);
    }

    // Returns only RESERVED transactions handled by this agent
    public List<Transaction> getReservedTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : handledTransactions)
            if (t.getTransactionStatus() == Status.RESERVED) result.add(t);
        return result;
    }

    // Agent cancels reservation: transaction -> CANCELLED, lot -> AVAILABLE (available again)
    public void cancelReservation(Transaction t) {
        t.setTransactionStatus(Status.CANCELLED);
        if (t.getLot() != null) t.getLot().updateStatus(Status.AVAILABLE);
    }

    // Agent finalizes reservation: transaction -> COMPLETED, lot -> COMPLETED
    public void finalizeReservation(Transaction t) {
        t.setTransactionStatus(Status.COMPLETED);
        if (t.getLot() != null) t.getLot().updateStatus(Status.COMPLETED);
    }

    public String getAgentId(){
        return agentId;
    }

    public double getCommissionRate(){
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate){
        this.commissionRate = commissionRate;
    }

    public Subdivision getSubdivision(){
        return subdivision;
    }

    public List<Transaction> getHandledTransactions(){
        return handledTransactions;
    }
}
