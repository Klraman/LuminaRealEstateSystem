# CLAUDE.md — Lumina Real Estate Sales & Management System
# Mapua University | CSS122P Object-Oriented Programming | Group 1

## Project Overview
Java-based desktop application modelled after Lumina Homes Philippines.
Manages a 5-block, 20-lot-per-block subdivision (100 lots total).
Built in Apache NetBeans using Java Swing (JFrame/JPanel).
GitHub repo: Klraman/LuminaRealEstateSystem

## Team
- Villanueva, Marcus Jiro
- Villanueva, Prince Leonard
- Bello, Jhio Russell
- Bolivar, Von Steven
- Segismundo, Kevin Arnold

Submitted to: Prof. Gloren S. Fuentes

---

## Package Structure
- MyLib — all model/logic classes
- MyApp — all GUI classes (Swing)

---

## Design Patterns
1. Registry Pattern — PropertyCatalog is the single source for all PropertyUnit instances
2. Strategy Pattern — PaymentMethod interface with interchangeable implementations

---

## GUI Architecture
- Single MainJFrame.java as the application shell
- Role-specific screens as swappable JPanels: LoginPage, AdminPage, AgentPage, BuyerPage
- On login, MainJFrame swaps the active JPanel based on user role

---

## CLASS INVENTORY (MyLib)

### Enums

Phase: CLASSIC_2, PHASE_3

Status (used by both Lot and Transaction): PENDING, RESERVED, COMPLETED, CANCELLED

BuyerType: REGULAR, SENIOR_CITIZEN, PWD, FIRST_TIME_BUYER

---

### Property Hierarchy (3-level inheritance)

PropertyUnit <<abstract>>
  # modelName: String
  # lotArea: int
  # floorArea: int
  # estimatedTCP: double
  # loanableAmount: double
  # reservationFee: double
  # downPayment: double
  # dpTarget: double
  # dpPeriod: double
  # interestRate: double
  # phase: Phase
  + getMonthlyAmort(years: int): double  <<abstract>>
  + getCalculatedTCP(): double
  # computeAmort(years: int): double     [formula: M = P*[r(1+r)^n]/[(1+r)^n-1]]

Angelique <<abstract>> extends PropertyUnit
  # floorArea: int = 35
  # reservationFee: double = 5000
  # dpPeriod: int = 12
  # interestRate: double = 0.07
  # phase: Phase = CLASSIC_2
  + getMonthlyAmort(years: int): double  [implemented here, NOT redeclared in leaf classes]

angeliqueInner extends Angelique
  # modelName: Static = "Angelique Inner"
  # lotArea: int = 36
  # estimatedTCP: double = 805000
  # loanableAmount: double = 734000
  # downPayment: double = 71000
  # dpTarget: double = 5500

angeliqueEnd extends Angelique
  # modelName: Static = "Angelique End"
  # lotArea: int = 56
  # estimatedTCP: double = 873000
  # loanableAmount: double = 802000
  # downPayment: double = 71000
  # dpTarget: double = 5500

Angeli <<abstract>> extends PropertyUnit
  # floorArea: int = 42
  # reservationFee: double = 5000
  # dpPeriod: int = 12
  # interestRate: double = 0.07
  # phase: Phase = CLASSIC_2
  + getMonthlyAmort(years: int): double  [implemented here, NOT redeclared in leaf classes]

angeliInner extends Angeli
  # modelName: Static = "Angeli Inner"
  # lotArea: int = 36
  # estimatedTCP: double = 825000
  # loanableAmount: double = 742000
  # downPayment: double = 83000
  # dpTarget: double = 6500

angeliEnd extends Angeli
  # modelName: Static = "Angeli End"
  # lotArea: int = 54
  # estimatedTCP: double = 941820
  # loanableAmount: double = 852820
  # downPayment: double = 89000
  # dpTarget: double = 7000

Aimee <<abstract>> extends PropertyUnit
  # floorArea: int = 22
  # phase: Phase = PHASE_3
  [interestRate stays in leaf classes — Inner=3%, End=7%]
  + getMonthlyAmort(years: int): double  [implemented here, NOT redeclared in leaf classes]

aimeeInner extends Aimee
  # modelName: Static = "Aimee Inner"
  # lotArea: int = 36
  # estimatedTCP: double = 485000
  # loanableAmount: double = 450000
  # reservationFee: double = 3000
  # downPayment: double = 35000
  # dpTarget: double = 2909
  # dpPeriod: double = 11
  # interestRate: double = 0.03   [UNIQUE: only unit with 3% rate]

aimeeEnd extends Aimee
  # modelName: Static = "Aimee End"
  # lotArea: int = 45
  # estimatedTCP: double = 566565
  # loanableAmount: double = 499000
  # reservationFee: double = 4000
  # downPayment: double = 67565
  # dpTarget: double = 5297
  # dpPeriod: double = 12
  # interestRate: double = 0.07

---

### Subdivision Structure (Composition chain)

Subdivision
  - name: String
  - totalBlocks: int
  - blocks: List<Block>
  + generateInventory(): void
  + findLot(int, int): Lot
  + filterLots(minSqm: double, maxSqm: double): List<Lot>
  + getSummaryReport(): String
  [Composition: Subdivision contains 1 to 1..* Blocks]

Block
  - blockNum: int
  - lots: List<Lot>
  - totalSqm: double
  + addLot(lot: Lot): void
  + getAvailableLots(): List<Lot>
  [Composition: Block contains 1 to 1..* Lots]

Lot
  - blkNum: int
  - lotNum: int
  - transactionStatus: Status    [uses Status enum NOT String]
  - houseModel: PropertyUnit     [association 0..1]
  - block: Block                 [back-reference to parent Block]
  + getAvailability(): bool
  + updateStatus(newStatus: Status): void   [parameter is Status enum NOT String]

PropertyCatalog (Registry Pattern)
  - availableUnits: List<PropertyUnit>
  + registerUnit(unit: PropertyUnit): void
  + getAllUnits(): List<PropertyUnit>
  + getUnitsByPhase(phase: Phase): List<PropertyUnit>
  + getUnitsByBudget(budget: double): List<PropertyUnit>
  + getUnitByName(modelName: String): PropertyUnit

---

### Payment Branch

PaymentMethod <<interface>>
  + validate(): boolean
  + pay(amount: double): void

Cash implements PaymentMethod
  - receivedAmount: double
  + validate(): boolean
  + pay(amount: double): void

Bank implements PaymentMethod
  - receivedAmount: double
  + validate(): boolean
  + pay(amount: double): void

Pagibig implements PaymentMethod
  - membershipId: String
  - loanType: String
  - loanAmount: double
  - approved: boolean
  + validate(): boolean
  + pay(amount: double): void
  + applyLoan(): void
  + validateApproval(): boolean
  + computeLoanRate(): double

EMI implements PaymentMethod
  - platformName: String = "Lumina In-House"
  - accountNumber: String
  - loanTerm: int
  - installmentsPaid: int
  - propertyUnit: PropertyUnit   [calls propertyUnit.getMonthlyAmort(loanTerm)]
  + validate(): boolean
  + pay(amount: double): void
  + getMonthlyDue(): double
  + getRemainingBalance(): double

---

### Transaction

Transaction
  - transactionID: String
  - amount: double
  - isPaid: boolean
  - installmentPlan: int
  - installmentsPaid: int
  - paymentMethod: PaymentMethod
  - discount: Discount
  - lot: Lot
  - buyer: Buyer
  - transactionStatus: Status
  + processPayment(): void
  + validatePayment(): boolean
  + getDetails(): String
  + getFinalTCP(): double
  + setTransactionStatus(newStatus: Status): void

---

### Discount Branch

Discount <<abstract>>
  # discountRate: double
  # discountLabel: String
  [NOTE: buyerType does NOT belong here, it belongs in Buyer]
  + computeDiscount(tcp: double): double   [formula: tcp * discountRate]
  + getDiscountDescription(): String

PWDDiscount extends Discount
  # discountRate: double = 0.20
  # discountLabel: String = "PWD"
  + computeDiscount(tcp: double): double

SeniorCitizenDiscount extends Discount
  # discountRate: double = 0.20
  # discountLabel: String = "Senior Citizen"
  + computeDiscount(tcp: double): double

FirstTimeDiscount extends Discount
  # discountRate: double = 0.05
  # discountLabel: String = "First Time"
  + computeDiscount(tcp: double): double

---

### User Hierarchy

User <<abstract>>
  - name: String
  - email: String
  - password: String
  + login(): boolean   <<abstract>>
  + logout(): void

Admin extends User
  - adminId: String
  + login(): boolean
  + generateInventoryReport(): String
  + viewAllTransactions(): String

Agent extends User
  - agentId: String
  - commissionRate: double
  + register(): boolean
  + login(): boolean
  + viewAvailableLots(): List<Lot>
  + filterLots(maxBudget, blockNum): List<Lot>
  + createReservation(buyer, lot): Transaction
  + processSale(buyer, lot): Transaction
  + printAmortizationTable(lot): void
  + computeCommission(): double
  + generateBuyerReport(): String

Buyer extends User
  - buyerId: String
  - budget: double
  - buyerType: BuyerType   [belongs here NOT in Discount]
  + register(): boolean
  + login(): boolean
  + viewAvailableLots(): List<Lot>
  + viewMyTransactions(): List<Transaction>
  + computeAffordability(lot, years): double

---

## Key Design Rules (NEVER VIOLATE)

1. getMonthlyAmort() is declared abstract in PropertyUnit, implemented ONCE in each
   mid-layer (Angelique/Angeli/Aimee). Leaf classes NEVER override or redeclare it.

2. computeAmort() lives ONLY in PropertyUnit. Formula: M = P * [r(1+r)^n] / [(1+r)^n - 1]

3. Buyers CANNOT directly create transactions. All reservations/sales go through Agent.

4. buyerType: BuyerType belongs in Buyer, NOT in Discount.

5. Lot.transactionStatus and updateStatus() use the Status ENUM, never a raw String.

6. PropertyCatalog is the ONLY class that calls new angeliqueInner(), new aimeeEnd() etc.
   All other classes get PropertyUnit references from the catalog.

7. Subdivision -> Block -> Lot is COMPOSITION. Lots and Blocks cannot exist without parent.

8. EMI is the ONLY PaymentMethod with a direct reference to PropertyUnit.

---

## Amortization Expected Values (from Lumina pricelist, for verification)

Model            | 30yr    | 25yr    | 20yr    | 15yr    | 10yr     | 5yr
AngeliqueInner   | 4,883   | 5,188   | 5,691   | 6,597   | 8,522    | 14,534
AngeliqueEnd     | 5,336   | 5,668   | 6,218   | 7,209   | 9,312    | 15,881
AngeliInner      | 4,937   | 5,244   | 5,753   | 6,669   | 8,615    | 14,692
AngeliEnd        | 5,674   | 6,028   | 6,612   | 7,665   | 9,902    | 16,887
AimeeInner       | 1,897   | 2,134   | 2,496   | 3,108   | 4,345    | 8,086
AimeeEnd         | 3,320   | 3,527   | 3,869   | 4,485   | 5,794    | 9,881
All values in Php.
