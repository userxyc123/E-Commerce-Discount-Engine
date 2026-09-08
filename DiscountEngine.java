import java.util.HashMap;
import java.util.Map;

public class DiscountEngine {
    private final Map<String, Integer> promoCodes;

    public DiscountEngine() {
        promoCodes = new HashMap<>();
        initializePromoCodes();
    }

    private void initializePromoCodes() {
        promoCodes.put("save10", 10);
        promoCodes.put("save15", 15);
        promoCodes.put("save20", 20);
        promoCodes.put("summer25", 25);
        promoCodes.put("flash30", 30);
        promoCodes.put("loyalty5", 5);
        promoCodes.put("welcome50", 50);
        promoCodes.put("newuser15", 15);
        promoCodes.put("vip35", 35);
        promoCodes.put("weekend10", 10);
        promoCodes.put("clearance40", 40);
        promoCodes.put("bulk12", 12);
    }

    public double applyDiscount(String promoCode, double cartTotal) {
        if (promoCode == null || cartTotal < 0) {
            return cartTotal;
        }

        String normalizedCode = promoCode.toLowerCase().trim();
        Integer discountPercent = promoCodes.get(normalizedCode);

        if (discountPercent == null) {
            return cartTotal;
        }

        double discountedTotal = cartTotal * (1 - discountPercent / 100.0);
        return Math.round(discountedTotal * 100.0) / 100.0;
    }
}
