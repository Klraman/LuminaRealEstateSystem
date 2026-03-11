package MyLib;
abstract class PropertyUnit {
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
    protected Phase  phase;
    
    public PropertyUnit(String modelName, int lotArea, int floorArea,double estimatedTCP, double loanableAmount,double reservationFee, double downPayment,double dpTarget, double dpPeriod,double interestRate, Phase phase) {
        this.modelName      = modelName;
        this.lotArea        = lotArea;
        this.floorArea      = floorArea;
        this.estimatedTCP   = estimatedTCP;
        this.loanableAmount = loanableAmount;
        this.reservationFee = reservationFee;
        this.downPayment    = downPayment;
        this.dpTarget       = dpTarget;
        this.dpPeriod       = dpPeriod;
        this.interestRate   = interestRate;
        this.phase          = phase;
    }
    
    public abstract double getMonthlyAmort(int years);
   
    public double getCalculatedTCP() {
        return estimatedTCP;
    }
    
    protected double computeAmort(int years) {
        double monthlyRate = interestRate / 12.0;
        int    totalMonths = years * 12;
        double power       = Math.pow(1 + monthlyRate, totalMonths);
        return loanableAmount * (monthlyRate * power) / (power - 1);
    }
    
    public String getModelName() { return modelName; }
    public int    getLotArea() { return lotArea; }
    public int    getFloorArea() { return floorArea; }
    public double getEstimatedTCP() { return estimatedTCP; }
    public double getLoanableAmount() { return loanableAmount; }
    public double getReservationFee() { return reservationFee; }
    public double getDownPayment() { return downPayment; }
    public double getDpTarget() { return dpTarget; }
    public double getDpPeriod() { return dpPeriod; }
    public double getInterestRate() { return interestRate; }
    public Phase  getPhase() { return phase; }
}
