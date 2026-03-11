package MyLib;

public enum Status {
    PENDING, RESERVED, COMPLETED, CANCELLED
}

public interface PaymentMethod {
    boolean validate();
    void pay(double amount);
}
