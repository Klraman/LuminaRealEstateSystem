package MyLib;

public class Pagibig implements PaymentMethod {

    private String membershipId;
    private String loanType;
    private double loanAmount;
    private boolean approved;

    public Pagibig(String membershipId, String loanType, double loanAmount) {
        this.membershipId = membershipId;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.approved = false;
    }

    @Override
    public boolean validate() {
        return membershipId != null && !membershipId.isEmpty() && loanAmount > 0;
    }

    @Override
    public void pay(double amount) {
        if (validate() && approved) {
            System.out.println("Pag-IBIG payment of Php " + amount + " processed. Loan ID: " + membershipId);
        } else {
            System.out.println("Pag-IBIG payment failed: loan not approved or invalid details.");
        }
    }

    public void applyLoan() {
        if (validate()) {
            System.out.println("Pag-IBIG loan application submitted for Php " + loanAmount);
        }
    }

    public boolean validateApproval() {
        return approved;
    }

    public double computeLoanRate() {
        // Pag-IBIG standard rate: 6.375% per annum for loans up to 750k
        if (loanAmount <= 750000) return 0.06375;
        return 0.0675;
    }

    public String getMembershipId()          { return membershipId; }
    public String getLoanType()              { return loanType; }
    public double getLoanAmount()            { return loanAmount; }
    public boolean isApproved()              { return approved; }
    public void setApproved(boolean approved){ this.approved = approved; }
}
