# Discount Engine Specification v1.0 (Final)

## Overview
A simple discount engine that applies percentage-based promo codes to e-commerce cart totals. Returns the discounted price or original total if the code is invalid.

---

## Functional Requirements

### 1. Promo Code Support
- **Format**: Percentage-based discounts (e.g., "SAVE10" → 10% off)
- **Mapping**: Codes map internally to discount percentages; no fixed relationship between code name and discount amount
- **Uniqueness**: Multiple codes can have the same discount percentage
- **Capacity**: 10–20 codes in memory initially, designed to scale later
- **Case Sensitivity**: Case-insensitive (SAVE10, save10, Save10 all resolve to the same code)

### 2. Core Behavior
- **Single Discount**: Only 1 code per cart (multi-discount scaling deferred)
- **Valid Code**: Apply discount percentage to cartTotal, return final price
- **Invalid Code**: Return cartTotal unchanged (no error thrown)
- **Eligible Carts**: Any non-negative cartTotal is eligible for discount
- **Return Type**: `double`

### 3. Promo Code Validation
- **Valid Range**: Discount percents must be > 0 and < 100 (0% and 100% are rejected)
- **Invalid Percents**: Reject negative, zero, ≥100%, non-numeric, or malformed values
- **Code Lookup**: Promo codes are validated against an internal HashMap of 10–20 known codes
- **Invalid Codes**: If code doesn't exist in HashMap, return cartTotal unchanged

### 4. Method Signature
```java
public double applyDiscount(String promoCode, double cartTotal)
```

---

## Design Decisions

| Aspect | Decision | Rationale |
|--------|----------|-----------|
| Storage | In-memory HashMap<String, Double> | Fast O(1) lookup; easily replaced with DB/cache later |
| Code Lookup | Case-insensitive | Prevents user frustration; normalize to lowercase internally |
| Percent Validation | Reject 0%, 100%, negatives, non-numeric | Prevent edge cases; maintain data integrity |
| Invalid Codes | Return original cartTotal silently | Graceful degradation; no exceptions for bad input |
| Return Type | `double` | Native support for pricing; sufficient precision for MVP |
| Expiry/Seasonality | Out of scope v1 | Scale in future iterations |
| Multi-Discount | Out of scope v1 | Scale in future iterations |

---

## Test Coverage

### Valid Scenarios
- ✅ Valid code → applies correct discount percentage
- ✅ Valid code, case-insensitive → "SAVE10", "save10", "Save10" all work
- ✅ Multiple valid codes → each independently applies its discount
- ✅ Calculation accuracy → e.g., $100 with 10% code = $90.00

### Invalid Scenarios
- ✅ Invalid code (not in HashMap) → returns cartTotal unchanged
- ✅ Invalid percent (negative, zero, ≥100%) → rejected, treated as invalid code
- ✅ Invalid percent (non-numeric) → rejected, treated as invalid code

### Edge Cases
- ✅ cartTotal = $0 → applies discount correctly (e.g., 0 * 0.9 = 0)
- ✅ Very small cartTotal → calculation precision verified
- ✅ Very small discount (e.g., 0.01%) → calculation precision verified

---

## Out of Scope (v1)
- Expiry dates / seasonal codes
- Multiple discounts per cart
- Validation on cartTotal max value
- Promo code creation/management UI
- Persistent storage

---

## Implementation Notes
1. **HashMap Initialization**: Populate with 10–20 test codes at class instantiation
2. **Normalization**: Convert input codes to lowercase before HashMap lookup
3. **Percent Storage**: Store as decimal (0.1 for 10%) or integer (10 for 10%)? *(Recommend: integer 0–99, multiply by 0.01 during calculation)*
4. **Return Precision**: Use `double` as-is; no rounding unless specified later

---

**Status**: Ready for implementation  
**Approved by**: [User review pending]
