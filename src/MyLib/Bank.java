package MyLib;

public class Bank implements PaymentMethod {

    private double receivedAmount;

    public Bank(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    @Override
    public boolean validate() {
        return receivedAmount > 0;
    }

    @Override
    public void pay(double amount) {
        if (validate() && receivedAmount >= amount) {
            System.out.println("Bank payment of Php " + amount + " processed.");
        } else {
            System.out.println("Bank payment failed: insufficient amount.");
        }
    }

    public double getReceivedAmount(){
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount){
        this.receivedAmount = receivedAmount;
    }
}
