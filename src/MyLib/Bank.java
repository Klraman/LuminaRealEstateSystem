package MyLib;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marcu
 */
public class Bank implements PaymentMethod {
    private double receivedAmount;
    private RecogBank bankName;
    private String transactionId;

    public Bank(double receivedAmount, RecogBank bankName, String transactionId) {
        this.receivedAmount = receivedAmount;
        this.bankName = bankName;
        this.transactionId = transactionId;
    }
}
