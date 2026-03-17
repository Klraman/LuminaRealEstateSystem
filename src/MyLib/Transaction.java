package MyLib;

import java.util.UUID;

public class Transaction {

    private String transactionID;
    private double amount;
    private boolean isPaid;
    private int installmentPlan;
    private int installmentsPaid;
    private PaymentMethod paymentMethod;
    private Discount discount;
    private Lot lot;
    private Buyer buyer;
    private Status transactionStatus;
    private String remark;
    private Agent assignedAgent;
    private int loanTerm;

    public Transaction(Buyer buyer, Lot lot, PaymentMethod paymentMethod, Discount discount) {
        this.transactionID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.buyer = buyer;
        this.lot = lot;
        this.paymentMethod = paymentMethod;
        this.discount = discount;
        this.transactionStatus = Status.PENDING;
        this.isPaid = false;
        this.installmentsPaid = 0;
        this.amount = (lot != null && lot.getHouseModel() != null)
                ? lot.getHouseModel().getCalculatedTCP() : 0;
    }

    public void processPayment() {
        if (paymentMethod == null) {
            System.out.println("No payment method set.");
            return;
        }
        if (paymentMethod.validate()) {
            paymentMethod.pay(getFinalTCP());
            isPaid = true;
            transactionStatus = Status.COMPLETED;
        } else {
            System.out.println("Payment validation failed.");
        }
    }

    public boolean validatePayment() {
        return paymentMethod != null && paymentMethod.validate();
    }

    public double getFinalTCP() {
        double tcp = (lot != null && lot.getHouseModel() != null)
                ? lot.getHouseModel().getCalculatedTCP() : amount;
        if (discount != null) {
            tcp -= discount.computeDiscount(tcp);
        }
        return tcp;
    }

    public String getDetails() {
        String model = (lot != null && lot.getHouseModel() != null)
                ? lot.getHouseModel().getModelName() : "N/A";
        String lotInfo = (lot != null)
                ? "Block " + lot.getBlkNum() + " Lot " + lot.getLotNum() : "N/A";
        String buyerName = (buyer != null) ? buyer.getName() : "N/A";
        return "TXN#" + transactionID
                + " | Buyer: " + buyerName
                + " | " + lotInfo
                + " | Model: " + model
                + " | Final TCP: Php " + String.format("%,.2f", getFinalTCP())
                + " | Status: " + transactionStatus;
    }

    public void setTransactionStatus(Status newStatus) {
        this.transactionStatus = newStatus;
    }

    public String getTransactionID()           { return transactionID; }
    public double getAmount()                  { return amount; }
    public boolean isPaid()                    { return isPaid; }
    public int getInstallmentPlan()            { return installmentPlan; }
    public int getInstallmentsPaid()           { return installmentsPaid; }
    public PaymentMethod getPaymentMethod()    { return paymentMethod; }
    public Discount getDiscount()              { return discount; }
    public Lot getLot()                        { return lot; }
    public Buyer getBuyer()                    { return buyer; }
    public Status getTransactionStatus()       { return transactionStatus; }
    public String getRemark()                  { return remark; }
    public Agent getAssignedAgent()            { return assignedAgent; }
    public int getLoanTerm()                   { return loanTerm; }

    public void setPaymentMethod(PaymentMethod pm) { this.paymentMethod = pm; }
    public void setDiscount(Discount d)            { this.discount = d; }
    public void setInstallmentPlan(int plan)       { this.installmentPlan = plan; }
    public void setInstallmentsPaid(int paid)      { this.installmentsPaid = paid; }
    public void setRemark(String remark)           { this.remark = remark; }
    public void setAssignedAgent(Agent agent)      { this.assignedAgent = agent; }
    public void setLoanTerm(int loanTerm)          { this.loanTerm = loanTerm; }
}
