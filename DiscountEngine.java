import java.util.HashMap;
import java.util.Map;

public class DiscountEngine {
    private final Map<String, Integer> percentageDiscounts;
    private final Map<String, Integer> fixedAmountDiscounts;

    public DiscountEngine() {
        percentageDiscounts = new HashMap<>();
        fixedAmountDiscounts = new HashMap<>();
        initializePromoCodes();
    }

    private void initializePromoCodes() {
        initializePercentageDiscounts();
        initializeFixedAmountDiscounts();
    }

    private void initializePercentageDiscounts() {
        percentageDiscounts.put("save10", 10);
        percentageDiscounts.put("save15", 15);
        percentageDiscounts.put("save20", 20);
        percentageDiscounts.put("summer25", 25);
        percentageDiscounts.put("flash30", 30);
        percentageDiscounts.put("loyalty5", 5);
        percentageDiscounts.put("welcome50", 50);
        percentageDiscounts.put("newuser15", 15);
        percentageDiscounts.put("vip35", 35);
        percentageDiscounts.put("weekend10", 10);
        percentageDiscounts.put("clearance40", 40);
        percentageDiscounts.put("bulk12", 12);
    }

    private void initializeFixedAmountDiscounts() {
        fixedAmountDiscounts.put("holiday20", 20);
        fixedAmountDiscounts.put("spring15", 15);
        fixedAmountDiscounts.put("promo30", 30);
        fixedAmountDiscounts.put("clearance50", 50);
        fixedAmountDiscounts.put("fall25", 25);
        fixedAmountDiscounts.put("winter35", 35);
        fixedAmountDiscounts.put("bonus10", 10);
        fixedAmountDiscounts.put("instant40", 40);
    }

    public double applyDiscount(String promoCode, double cartTotal) {
        if (promoCode == null || cartTotal < 0) {
            return cartTotal;
        }

        String normalizedCode = promoCode.toLowerCase().trim();

        Integer percentageDiscount = percentageDiscounts.get(normalizedCode);
        if (percentageDiscount != null) {
            double discountedTotal = cartTotal * (1 - percentageDiscount / 100.0);
            return Math.round(discountedTotal * 100.0) / 100.0;
        }

        Integer fixedDiscount = fixedAmountDiscounts.get(normalizedCode);
        if (fixedDiscount != null) {
            if (cartTotal - fixedDiscount < 0) {
                return cartTotal;
            }
            double discountedTotal = cartTotal - fixedDiscount;
            return Math.round(discountedTotal * 100.0) / 100.0;
        }

        return cartTotal;
    }
}
