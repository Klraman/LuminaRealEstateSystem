package MyLib;

public class VeteranDiscount extends Discount {

    {
        discountRate  = 0.10;
        discountLabel = "Veteran";
    }

    @Override
    public double computeDiscount(double tcp) {
        return tcp * discountRate;
    }

    @Override
    public String getDiscountDescription() {
        return "Veteran Discount (10%)";
    }
}
