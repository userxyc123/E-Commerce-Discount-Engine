# Discount Engine Specification v2.0 (Prototype 2)

## Overview
Extends the discount engine to support both percentage-based AND fixed-amount promo codes. Returns the discounted price or original total if code is invalid. No breaking changes to v1 behavior.

---

## Functional Requirements

### 1. Discount Types
**Percentage-Based Codes** (v1 - unchanged)
- Example: "save10" → 10% off
- Stored as integer (0–99)
- Can have same discount as other percentage codes

**Fixed-Amount Codes** (NEW in v2)
- Example: "holiday20" → $20 off
- Stored as integer (e.g., 20, not 20.01)
- No upper/lower bounds on discount amounts
- Code names are arbitrary strings (not necessarily "FLAT" prefix)
- Can have same discount as other fixed codes

### 2. Differentiation Strategy
- **Two separate maps**: one for percentage codes, one for fixed codes
- **No string pattern matching**: explicit storage in appropriate map
- **Unique codes across maps**: "SAVE10" cannot exist in both maps
- **Lookup process**: check both maps when applying discount

### 3. Core Behavior (Unchanged from v1)
- **Single Discount**: Only 1 code per cart
- **Valid Code**: Apply discount (either percentage or fixed), return final price
- **Invalid Code**: Return cartTotal unchanged (no error thrown)
- **Case Sensitivity**: Case-insensitive lookup (normalize to lowercase)
- **Return Type**: `double`
- **Rounding**: Round to 2 decimal places

### 4. Fixed-Amount Discount Rules (NEW)
- **Floor Constraint**: Fixed discount can never bring total below $0
  - If `(cartTotal - fixedDiscount) < 0`, return `cartTotal` unchanged
  - Example: $15 cart with $20 fixed discount → return $15 (not allowed)
- **Integer Storage**: All fixed amounts are integers (e.g., $20, not $20.01)
- **Calculation**: `finalPrice = cartTotal - fixedDiscount`
- **Rounding**: Result rounded to 2 decimal places

### 5. Method Signature (Unchanged)
```java
public double applyDiscount(String promoCode, double cartTotal)
```

---

## Design Decisions

| Aspect | Decision | Rationale |
|--------|----------|-----------|
| Storage | Two separate HashMaps | Clear distinction; prevents ambiguity; scales easily |
| Fixed Naming | Arbitrary strings | Flexibility; allows random/marketing-driven names |
| Floor Constraint | Never below $0 | Prevents negative/invalid cart totals |
| Code Lookup | Check both maps | Unified API; caller doesn't need to know type |
| Uniqueness | Unique across maps | Prevents confusion; ensures clean lookup |
| Fixed Bounds | No limits | Flexibility for future marketing campaigns |
| Hardcoded Codes | 5–10 fixed + 12 percentage | Balance of test coverage vs simplicity |

---

## Test Coverage

### Percentage Codes (v1 - still passing)
- ✅ Valid percentage code applies correct discount
- ✅ Case insensitivity works
- ✅ Invalid percentage codes return total unchanged
- ✅ Calculation accuracy
- ✅ Edge cases (zero cart, small amounts)

### Fixed-Amount Codes (NEW)
- ✅ Valid fixed code applies correct discount
  - Example: $100 with $20 fixed = $80.00
- ✅ Case insensitivity works for fixed codes
- ✅ Invalid fixed codes return total unchanged
- ✅ Calculation accuracy with various cart totals
- ✅ **Floor constraint**: Fixed discount > cartTotal → return cartTotal unchanged
  - Example: $15 cart with $20 fixed = return $15 (not allowed)
- ✅ Edge cases: zero cart, small amounts

### Mixed Scenarios (NEW)
- ✅ Percentage and fixed codes coexist
- ✅ Multiple codes of each type work independently
- ✅ Codes with same discount (e.g., two $20 fixed codes)
- ✅ Switching between percentage and fixed works correctly

---

## Implementation Notes

1. **Storage Structure**:
   ```
   percentageCodes: Map<String, Integer>  // "save10" -> 10
   fixedAmountCodes: Map<String, Integer> // "holiday20" -> 20
   ```

2. **Lookup Logic**:
   - Normalize code to lowercase
   - Check `percentageCodes` map first, then `fixedAmountCodes`
   - Apply appropriate calculation
   - Return result

3. **Fixed Discount Logic**:
   ```
   if (cartTotal - fixedDiscount < 0) {
       return cartTotal;  // Floor constraint violated
   }
   finalPrice = cartTotal - fixedDiscount;
   ```

4. **Rounding**: Both types use `Math.round(result * 100.0) / 100.0`

---

## Out of Scope (v2)
- Combining multiple discounts
- Maximum fixed discount limits
- "FLAT" prefix requirement
- Expiry dates / seasonal codes
- Promo code creation/management UI
- Persistent storage

---

## Hardcoded Test Codes for v2

### Percentage Codes (from v1)
- save10, save15, save20, summer25, flash30, loyalty5, welcome50, newuser15, vip35, weekend10, clearance40, bulk12

### Fixed-Amount Codes (NEW - 5-10 codes)
- *(To be determined in implementation)*
- Examples: holiday20, spring15, promo30, clearance50, etc.

---

**Status**: Ready for implementation review  
**Breaking Changes**: None (v1 behavior preserved)  
**New Capabilities**: Fixed-amount discounts with floor constraint
