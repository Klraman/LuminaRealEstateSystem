package MyLib;

public abstract class Angeli extends PropertyUnit {

    {
        floorArea      = 42;
        reservationFee = 5000;
        dpPeriod       = 12;
        interestRate   = 0.07;
        phase          = Phase.CLASSIC_2;
    }

    @Override
    public double getMonthlyAmort(int years) {
        return computeAmort(years);
    }
}
