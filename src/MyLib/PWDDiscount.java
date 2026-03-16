package MyLib;

public class PWDDiscount extends Discount {

    {
        discountRate  = 0.20;
        discountLabel = "PWD";
    }

    @Override
    public double computeDiscount(double tcp) {
        return tcp * discountRate;
    }
}
