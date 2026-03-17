package MyLib;

public class Bank implements PaymentMethod {

    private String bankName;
    private String accountNumber;
    private double interestRate = 0.065; // 6.5% p.a.
    private int loanTerm;
    private double loanAmount;
    private boolean approved;

    public Bank(String bankName, String accountNumber,
                double loanAmount, int loanTerm) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.approved = false;
    }

    @Override
    public boolean validate() {
        return approved;
    }

    @Override
    public void pay(double amount) {
        if (approved) {
            System.out.println("Bank (" + bankName + ") payment of Php "
                    + String.format("%.2f", amount) + " processed.");
        } else {
            System.out.println("Bank payment failed: loan not yet approved.");
        }
    }

    public void applyLoan() {
        System.out.println("Bank loan application submitted to " + bankName
                + " for Php " + String.format("%.2f", loanAmount)
                + " over " + loanTerm + " years.");
    }

    public boolean validateApproval() {
        return approved;
    }

    public double getMonthlyPayment() {
        double r = interestRate / 12.0;
        int n = loanTerm * 12;
        if (r == 0 || n == 0) return 0;
        double factor = Math.pow(1 + r, n);
        return loanAmount * (r * factor) / (factor - 1);
    }

    public String getBankName(){
        return bankName;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public double getInterestRate(){
        return interestRate;
    }

    public double getLoanAmount(){
        return loanAmount;
    }

    public int getLoanTerm(){
        return loanTerm;
    }

    public boolean isApproved(){
        return approved;
    }

    public void setApproved(boolean approved){
        this.approved = approved;
    }
}
