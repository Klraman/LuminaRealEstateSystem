package MyLib;
public abstract class Angeli extends PropertyUnit {
    protected static final int    FLOOR_AREA      = 42;
    protected static final double RESERVATION_FEE = 5000;
    protected static final int    DP_PERIOD       = 12;
    protected static final double INTEREST_RATE   = 0.07;
    protected static final Phase  PHASE           = Phase.CLASSIC_2;

    public Angeli(String modelName, int lotArea, double estimatedTCP,double loanableAmount, double downPayment, double dpTarget) {
        super(modelName,lotArea,FLOOR_AREA,estimatedTCP,loanableAmount,RESERVATION_FEE,downPayment,dpTarget,DP_PERIOD,INTEREST_RATE,PHASE);
    }

    @Override
    public double getMonthlyAmort(int years) {
        return computeAmort(years);
    }
}