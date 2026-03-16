package MyLib;

public abstract class Aimee extends PropertyUnit {

    {
        floorArea = 22;
        phase     = Phase.PHASE_3;
        // interestRate is set by each leaf class (Inner=3%, End=7%)
    }

    @Override
    public double getMonthlyAmort(int years) {
        return computeAmort(years);
    }
}
