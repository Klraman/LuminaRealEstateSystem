package MyLib;

public class EMI implements PaymentMethod {

    private String platformName = "Lumina In-House";
    private String accountNumber;
    private int loanTerm;          // in years
    private int installmentsPaid;
    private PropertyUnit propertyUnit;  // EMI is the ONLY PaymentMethod with a direct ref to PropertyUnit

    public EMI(String accountNumber, int loanTerm, PropertyUnit propertyUnit) {
        this.accountNumber = accountNumber;
        this.loanTerm = loanTerm;
        this.propertyUnit = propertyUnit;
        this.installmentsPaid = 0;
    }

    @Override
    public boolean validate() {
        return accountNumber != null && !accountNumber.isEmpty()
                && loanTerm > 0 && propertyUnit != null;
    }

    @Override
    public void pay(double amount) {
        if (validate()) {
            installmentsPaid++;
            System.out.println(platformName + " EMI payment #" + installmentsPaid
                    + " of Php " + String.format("%.2f", amount) + " processed.");
        } else {
            System.out.println("EMI payment failed: invalid account or loan details.");
        }
    }

    public double getMonthlyDue() {
        return propertyUnit.getMonthlyAmort(loanTerm);
    }

    public double getRemainingBalance() {
        int totalMonths = loanTerm * 12;
        int remaining = totalMonths - installmentsPaid;
        return getMonthlyDue() * remaining;
    }

    public String getPlatformName(){
        return platformName;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public int getLoanTerm(){
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm){
        this.loanTerm = loanTerm;
    }

    public int getInstallmentsPaid(){
        return installmentsPaid;
    }

    public PropertyUnit getPropertyUnit(){
        return propertyUnit;
    }
}
