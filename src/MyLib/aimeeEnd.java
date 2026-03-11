package MyLib;
public class aimeeEnd extends Aimee {

    protected static final String MODEL_NAME      = "Aimee End";
    protected static final int    LOT_AREA        = 45;
    protected static final double ESTIMATED_TCP   = 566565;
    protected static final double LOANABLE_AMOUNT = 499000;
    protected static final double RESERVATION_FEE = 4000;
    protected static final double DOWN_PAYMENT    = 67565;
    protected static final double DP_TARGET       = 5297;
    protected static final int    DP_PERIOD       = 12;
    protected static final double INTEREST_RATE   = 0.07;   // back to 7%

    public aimeeEnd() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, RESERVATION_FEE,
              DOWN_PAYMENT, DP_TARGET,
              DP_PERIOD, INTEREST_RATE);
    }
}
