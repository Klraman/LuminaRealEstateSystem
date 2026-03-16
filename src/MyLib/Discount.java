package MyLib;

public abstract class Discount {

    protected double discountRate;
    protected String discountLabel;

    public double computeDiscount(double tcp) {
        return tcp * discountRate;
    }

    public String getDiscountDescription() {
        return discountLabel + " Discount (" + (discountRate * 100) + "%)";
    }

    public double getDiscountRate()  { return discountRate; }
    public String getDiscountLabel() { return discountLabel; }
}
