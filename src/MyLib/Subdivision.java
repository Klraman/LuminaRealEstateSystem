package MyLib;

import java.util.ArrayList;
import java.util.List;

public class Subdivision {

    private String name;
    private int totalBlocks;
    private List<Block> blocks;

    public Subdivision(String name, int totalBlocks) {
        this.name = name;
        this.totalBlocks = totalBlocks;
        this.blocks = new ArrayList<>();
    }

    // Builds 5 blocks x 20 lots each; house models assigned by PropertyCatalog externally
    public void generateInventory() {
        blocks.clear();
        for (int b = 1; b <= totalBlocks; b++) {
            Block block = new Block(b);
            for (int l = 1; l <= 20; l++) {
                Lot lot = new Lot(b, l, block);
                block.addLot(lot);
            }
            blocks.add(block);
        }
    }

    public Lot findLot(int blockNum, int lotNum) {
        for (Block block : blocks) {
            if (block.getBlockNum() == blockNum) {
                for (Lot lot : block.getLots()) {
                    if (lot.getLotNum() == lotNum) {
                        return lot;
                    }
                }
            }
        }
        return null;
    }

    public List<Lot> filterLots(double minSqm, double maxSqm) {
        List<Lot> result = new ArrayList<>();
        for (Block block : blocks) {
            for (Lot lot : block.getLots()) {
                if (lot.getHouseModel() != null) {
                    int area = lot.getHouseModel().getLotArea();
                    if (area >= minSqm && area <= maxSqm) {
                        result.add(lot);
                    }
                }
            }
        }
        return result;
    }

    public String getSummaryReport() {
        int total = 0, available = 0, reserved = 0, sold = 0;
        for (Block block : blocks) {
            for (Lot lot : block.getLots()) {
                total++;
                switch (lot.getTransactionStatus()) {
                    case PENDING   -> available++;
                    case RESERVED  -> reserved++;
                    case COMPLETED -> sold++;
                    default        -> {}
                }
            }
        }
        return "=== " + name + " Summary ===\n"
                + "Total Lots : " + total + "\n"
                + "Available  : " + available + "\n"
                + "Reserved   : " + reserved + "\n"
                + "Sold       : " + sold + "\n";
    }

    public String getName()         { return name; }
    public int getTotalBlocks()     { return totalBlocks; }
    public List<Block> getBlocks()  { return blocks; }
}
