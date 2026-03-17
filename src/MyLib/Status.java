package MyLib;

public enum Status {
    PENDING,     // lot = available; transaction = awaiting agent
    SUBMITTED,   // lot only: buyer submitted, agent hasn't reviewed yet
    RESERVED,
    COMPLETED,
    CANCELLED,
    REJECTED     // transaction only: agent rejected
}
