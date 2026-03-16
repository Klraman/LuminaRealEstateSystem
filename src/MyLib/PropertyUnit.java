package MyLib;

public abstract class PropertyUnit {

    protected String modelName;
    protected int lotArea;
    protected int floorArea;
    protected double estimatedTCP;
    protected double loanableAmount;
    protected double reservationFee;
    protected double downPayment;
    protected double dpTarget;
    protected double dpPeriod;
    protected double interestRate;
    protected Phase phase;

    // Abstract — implemented once in each mid-layer (Angelique, Angeli, Aimee)
    public abstract double getMonthlyAmort(int years);

    public double getCalculatedTCP() {
        return estimatedTCP;
    }

    // Formula: M = P * [r(1+r)^n] / [(1+r)^n - 1]
    // P = loanableAmount, r = monthly rate, n = total months
    protected double computeAmort(int years) {
        double r = interestRate / 12.0;
        int n = years * 12;
        double factor = Math.pow(1 + r, n);
        return loanableAmount * (r * factor) / (factor - 1);
    }

    public String getModelName()       { return modelName; }
    public int getLotArea()            { return lotArea; }
    public int getFloorArea()          { return floorArea; }
    public double getEstimatedTCP()    { return estimatedTCP; }
    public double getLoanableAmount()  { return loanableAmount; }
    public double getReservationFee()  { return reservationFee; }
    public double getDownPayment()     { return downPayment; }
    public double getDpTarget()        { return dpTarget; }
    public double getDpPeriod()        { return dpPeriod; }
    public double getInterestRate()    { return interestRate; }
    public Phase getPhase()            { return phase; }
}
