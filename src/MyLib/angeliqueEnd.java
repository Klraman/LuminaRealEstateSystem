package MyLib;
public class angeliqueEnd extends Angelique {

    protected static final String MODEL_NAME      = "Angelique End";
    protected static final int    LOT_AREA        = 56;
    protected static final double ESTIMATED_TCP   = 873000;
    protected static final double LOANABLE_AMOUNT = 802000;
    protected static final double DOWN_PAYMENT    = 71000;
    protected static final double DP_TARGET       = 5500;

    public angeliqueEnd() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, DOWN_PAYMENT, DP_TARGET);
    }
}