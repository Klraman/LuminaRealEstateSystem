package MyLib;
public class angeliInner extends Angeli {

    protected static final String MODEL_NAME      = "Angeli Inner";
    protected static final int    LOT_AREA        = 36;
    protected static final double ESTIMATED_TCP   = 825000;
    protected static final double LOANABLE_AMOUNT = 742000;
    protected static final double DOWN_PAYMENT    = 83000;
    protected static final double DP_TARGET       = 6500;

    public angeliInner() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, DOWN_PAYMENT, DP_TARGET);
    }
}
