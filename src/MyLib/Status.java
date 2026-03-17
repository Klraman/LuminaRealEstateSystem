package MyLib;

public enum Status {
    AVAILABLE,   // lot only: lot is open for purchase
    PENDING,     // transaction only: awaiting agent approval
    SUBMITTED,   // lot only: buyer submitted, agent hasn't reviewed yet
    RESERVED,
    COMPLETED,
    CANCELLED,
    REJECTED     // transaction only: agent rejected
}
