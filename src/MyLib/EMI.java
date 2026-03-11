/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MyLib;

/**
 *
 * @author marcu
 */
public class EMI implements PaymentMethod { // To add payment method
    private String platformName;
    private String accountNumber;
    private int loanTerm;
    private int installmentsPaid;
    private PropertyUnit propertyUnit;

    public EMI(String accountNumber, int loanTerm, int installmentsPaid, PropertyUnit propertyUnit) {
        this.platformName = "Lumina In-House"; // Hardcoded as per UML
        this.accountNumber = accountNumber;
        this.loanTerm = loanTerm;
        this.installmentsPaid = installmentsPaid;
        this.propertyUnit = propertyUnit;
    }
}