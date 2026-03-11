package MyLib;
public class angeliEnd extends Angeli {

    protected static final String MODEL_NAME      = "Angeli End";
    protected static final int    LOT_AREA        = 54;
    protected static final double ESTIMATED_TCP   = 941820;
    protected static final double LOANABLE_AMOUNT = 852820;
    protected static final double DOWN_PAYMENT    = 89000;
    protected static final double DP_TARGET       = 7000;

    public angeliEnd() {
        super(MODEL_NAME, LOT_AREA, ESTIMATED_TCP,
              LOANABLE_AMOUNT, DOWN_PAYMENT, DP_TARGET);
    }
}
