package MyLib;
public class aimeeInner extends Aimee {
    
    protected static final String MODEL_NAME      = "Aimee Inner";
    protected static final int    LOT_AREA        = 36;
    protected static final double ESTIMATED_TCP   = 485000;
    protected static final double LOANABLE_AMOUNT = 450000;
    protected static final double RESERVATION_FEE = 3000;   
    protected static final double DOWN_PAYMENT    = 35000;
    protected static final double DP_TARGET       = 2909;
    protected static final int    DP_PERIOD       = 11;     
    protected static final double INTEREST_RATE   = 0.03;   

    public aimeeInner() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, RESERVATION_FEE,
              DOWN_PAYMENT, DP_TARGET,
              DP_PERIOD, INTEREST_RATE);
    }
}
