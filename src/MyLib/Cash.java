package MyLib;

public class Cash implements PaymentMethod {

    private double receivedAmount;

    public Cash(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    @Override
    public boolean validate() {
        return receivedAmount > 0;
    }

    @Override
    public void pay(double amount) {
        if (validate() && receivedAmount >= amount) {
            System.out.println("Cash payment of Php " + amount + " processed.");
        } else {
            System.out.println("Cash payment failed: insufficient amount.");
        }
    }

    public double getReceivedAmount() { return receivedAmount; }
    public void setReceivedAmount(double receivedAmount) { this.receivedAmount = receivedAmount; }
}
