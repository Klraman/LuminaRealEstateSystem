package MyLib;
public class angeliqueInner extends Angelique {

    protected static final String MODEL_NAME      = "Angelique Inner";
    protected static final int    LOT_AREA        = 36;
    protected static final double ESTIMATED_TCP   = 805000;
    protected static final double LOANABLE_AMOUNT = 734000;
    protected static final double DOWN_PAYMENT    = 71000;
    protected static final double DP_TARGET       = 5500;

    public angeliqueInner() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, DOWN_PAYMENT, DP_TARGET);
    }
}
