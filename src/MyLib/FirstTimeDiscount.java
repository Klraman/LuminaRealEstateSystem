package MyLib;

public class FirstTimeDiscount extends Discount {

    {
        discountRate  = 0.05;
        discountLabel = "First Time";
    }

    @Override
    public double computeDiscount(double tcp) {
        return tcp * discountRate;
    }
}
