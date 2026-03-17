package MyLib;

import java.util.List;

public class Admin extends User {

    private String adminId;
    private Subdivision subdivision;
    private List<Transaction> allTransactions;

    public Admin(String name, String email, String password,
                 String adminId, Subdivision subdivision, List<Transaction> allTransactions) {
        super(name, email, password);
        this.adminId = adminId;
        this.subdivision = subdivision;
        this.allTransactions = allTransactions;
    }

    @Override
    public boolean login() {
        System.out.println("Admin " + getName() + " logged in.");
        return true;
    }

    public String generateInventoryReport() {
        return subdivision.getSummaryReport();
    }

    public String viewAllTransactions() {
        if (allTransactions == null || allTransactions.isEmpty()) {
            return "No transactions on record.";
        }
        StringBuilder sb = new StringBuilder("=== All Transactions ===\n");
        for (Transaction t : allTransactions) {
            sb.append(t.getDetails()).append("\n");
        }
        return sb.toString();
    }

    public String getAdminId(){
        return adminId;
    }

    public Subdivision getSubdivision(){
        return subdivision;
    }

    public List<Transaction> getAllTransactions(){
        return allTransactions;
    }
}
