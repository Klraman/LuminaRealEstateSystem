package MyLib;
public abstract class Aimee extends PropertyUnit {
    protected static final int   FLOOR_AREA = 22;
    protected static final Phase PHASE      = Phase.PHASE_3;

    public Aimee(String modelName, int lotArea, double estimatedTCP,double loanableAmount, double reservationFee,double downPayment, double dpTarget,double dpPeriod, double interestRate) {
        super(modelName,lotArea,FLOOR_AREA,estimatedTCP,loanableAmount,reservationFee,downPayment,dpTarget,dpPeriod,interestRate,PHASE);
    }

    @Override
    public double getMonthlyAmort(int years) {
        return computeAmort(years);
    }
}