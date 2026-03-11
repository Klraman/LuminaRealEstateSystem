package MyLib;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marcu
 */
public class Pagibig implements PaymentMethod {
    private String membershipId;
    private String loanType;
    private double loanAmount;
    private boolean approved;

    public Pagibig(String membershipId, String loanType, double loanAmount, boolean approved) {
        this.membershipId = membershipId;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.approved = approved;
    }
}
