/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MyLib;

/**
 *
 * @author marcu
 */
public class Cash implements PaymentMethod {
    private double receivedAmount;

    public Cash(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
}
