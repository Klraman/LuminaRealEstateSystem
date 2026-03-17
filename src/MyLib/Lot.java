package MyLib;

public class Lot {

    private int blkNum;
    private int lotNum;
    private Status transactionStatus;
    private PropertyUnit houseModel;
    private Block block;

    public Lot(int blkNum, int lotNum, Block block) {
        this.blkNum = blkNum;
        this.lotNum = lotNum;
        this.block = block;
        this.transactionStatus = Status.PENDING;
    }

    public boolean getAvailability() {
        return transactionStatus == Status.PENDING;
    }

    public void updateStatus(Status newStatus) {
        this.transactionStatus = newStatus;
    }

    public int getBlkNum(){
        return blkNum;
    }

    public int getLotNum(){
        return lotNum;
    }

    public Status getTransactionStatus(){
        return transactionStatus;
    }

    public PropertyUnit getHouseModel(){
        return houseModel;
    }

    public void setHouseModel(PropertyUnit houseModel){
        this.houseModel = houseModel;
    }

    public Block getBlock(){
        return block;
    }

    @Override
    public String toString() {
        return "Block " + blkNum + " Lot " + lotNum
                + " | Model: " + (houseModel != null ? houseModel.getModelName() : "None")
                + " | Status: " + transactionStatus;
    }
}
