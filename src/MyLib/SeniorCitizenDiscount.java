package MyLib;

public class SeniorCitizenDiscount extends Discount {

    {
        discountRate  = 0.20;
        discountLabel = "Senior Citizen";
    }

    @Override
    public double computeDiscount(double tcp) {
        return tcp * discountRate;
    }
}
