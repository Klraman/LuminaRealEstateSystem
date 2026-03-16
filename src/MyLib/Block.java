package MyLib;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private int blockNum;
    private List<Lot> lots;
    private double totalSqm;

    public Block(int blockNum) {
        this.blockNum = blockNum;
        this.lots = new ArrayList<>();
        this.totalSqm = 0;
    }

    public void addLot(Lot lot) {
        lots.add(lot);
        if (lot.getHouseModel() != null) {
            totalSqm += lot.getHouseModel().getLotArea();
        }
    }

    public List<Lot> getAvailableLots() {
        List<Lot> available = new ArrayList<>();
        for (Lot lot : lots) {
            if (lot.getAvailability()) {
                available.add(lot);
            }
        }
        return available;
    }

    public int getBlockNum()      { return blockNum; }
    public List<Lot> getLots()    { return lots; }
    public double getTotalSqm()   { return totalSqm; }
}
