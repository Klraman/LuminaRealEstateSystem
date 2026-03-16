package MyLib;

public interface PaymentMethod {
    boolean validate();
    void pay(double amount);
}
