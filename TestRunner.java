public class TestRunner {
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Discount Engine Test Suite v2 ===\n");

        DiscountEngine engine = new DiscountEngine();

        // ===== PERCENTAGE DISCOUNT TESTS (v1) =====
        System.out.println("--- Percentage Discount Tests ---");

        test("Valid code applies correct discount", () -> {
            double result = engine.applyDiscount("save10", 100.0);
            assertEquals(90.0, result);
        });

        test("Valid code with 25% discount", () -> {
            double result = engine.applyDiscount("summer25", 80.0);
            assertEquals(60.0, result);
        });

        test("Valid code with 50% discount", () -> {
            double result = engine.applyDiscount("welcome50", 100.0);
            assertEquals(50.0, result);
        });

        test("Case insensitivity - uppercase", () -> {
            double result = engine.applyDiscount("SAVE10", 100.0);
            assertEquals(90.0, result);
        });

        test("Case insensitivity - mixed case", () -> {
            double result = engine.applyDiscount("SaVe10", 100.0);
            assertEquals(90.0, result);
        });

        test("Case insensitivity - with whitespace", () -> {
            double result = engine.applyDiscount("  save10  ", 100.0);
            assertEquals(90.0, result);
        });

        test("Invalid code returns original total", () -> {
            double result = engine.applyDiscount("nonexistent", 100.0);
            assertEquals(100.0, result);
        });

        test("Invalid code with random string", () -> {
            double result = engine.applyDiscount("xyz123", 50.0);
            assertEquals(50.0, result);
        });

        test("Null code returns original total", () -> {
            double result = engine.applyDiscount(null, 100.0);
            assertEquals(100.0, result);
        });

        test("Calculation accuracy - small discount", () -> {
            double result = engine.applyDiscount("loyalty5", 100.0);
            assertEquals(95.0, result);
        });

        test("Calculation accuracy - large discount", () -> {
            double result = engine.applyDiscount("clearance40", 100.0);
            assertEquals(60.0, result);
        });

        test("Calculation with decimal cart total", () -> {
            double result = engine.applyDiscount("save20", 99.99);
            assertEquals(79.99, result);
        });

        test("Calculation with odd number discount", () -> {
            double result = engine.applyDiscount("bulk12", 75.50);
            assertEquals(66.44, result);
        });

        test("Zero cart total", () -> {
            double result = engine.applyDiscount("save10", 0.0);
            assertEquals(0.0, result);
        });

        test("Very small cart total", () -> {
            double result = engine.applyDiscount("save10", 0.99);
            assertEquals(0.89, result);
        });

        test("Negative cart total returns unchanged", () -> {
            double result = engine.applyDiscount("save10", -10.0);
            assertEquals(-10.0, result);
        });

        test("Multiple codes work independently", () -> {
            double result1 = engine.applyDiscount("save10", 100.0);
            double result2 = engine.applyDiscount("save15", 100.0);
            double result3 = engine.applyDiscount("vip35", 100.0);

            assertEquals(90.0, result1);
            assertEquals(85.0, result2);
            assertEquals(65.0, result3);
        });

        test("Rounding to 2 decimal places", () -> {
            double result = engine.applyDiscount("save10", 33.33);
            assertEquals(30.0, result);
        });

        test("Rounding with complex calculation", () -> {
            double result = engine.applyDiscount("flash30", 99.99);
            assertEquals(69.99, result);
        });

        test("Same code multiple times returns consistent results", () -> {
            double result1 = engine.applyDiscount("save20", 50.0);
            double result2 = engine.applyDiscount("save20", 50.0);
            assertEquals(result1, result2);
            assertEquals(40.0, result1);
        });

        test("Multiple codes with same discount", () -> {
            double result1 = engine.applyDiscount("save15", 100.0);
            double result2 = engine.applyDiscount("newuser15", 100.0);
            assertEquals(result1, result2);
            assertEquals(85.0, result1);
        });

        // ===== FIXED-AMOUNT DISCOUNT TESTS (v2) =====
        System.out.println("\n--- Fixed-Amount Discount Tests ---");

        test("Valid fixed code applies correct discount", () -> {
            double result = engine.applyDiscount("holiday20", 100.0);
            assertEquals(80.0, result);
        });

        test("Fixed code with large discount", () -> {
            double result = engine.applyDiscount("promo30", 100.0);
            assertEquals(70.0, result);
        });

        test("Fixed code with small discount", () -> {
            double result = engine.applyDiscount("bonus10", 50.0);
            assertEquals(40.0, result);
        });

        test("Case insensitivity - fixed code uppercase", () -> {
            double result = engine.applyDiscount("HOLIDAY20", 100.0);
            assertEquals(80.0, result);
        });

        test("Case insensitivity - fixed code mixed case", () -> {
            double result = engine.applyDiscount("Holiday20", 100.0);
            assertEquals(80.0, result);
        });

        test("Case insensitivity - fixed code with whitespace", () -> {
            double result = engine.applyDiscount("  holiday20  ", 100.0);
            assertEquals(80.0, result);
        });

        test("Fixed discount with decimal cart total", () -> {
            double result = engine.applyDiscount("spring15", 99.99);
            assertEquals(84.99, result);
        });

        test("Fixed discount calculation accuracy", () -> {
            double result = engine.applyDiscount("fall25", 75.50);
            assertEquals(50.50, result);
        });

        test("Zero cart total with fixed discount", () -> {
            double result = engine.applyDiscount("holiday20", 0.0);
            assertEquals(0.0, result);
        });

        test("Very small cart total with fixed discount", () -> {
            double result = engine.applyDiscount("bonus10", 5.0);
            assertEquals(5.0, result);
        });

        // ===== FLOOR CONSTRAINT TESTS (v2) =====
        System.out.println("\n--- Floor Constraint Tests ---");

        test("Floor constraint - fixed discount equals cart total", () -> {
            double result = engine.applyDiscount("holiday20", 20.0);
            assertEquals(0.0, result);
        });

        test("Floor constraint - fixed discount exceeds cart total", () -> {
            double result = engine.applyDiscount("holiday20", 10.0);
            assertEquals(10.0, result);
        });

        test("Floor constraint - large discount exceeds cart total", () -> {
            double result = engine.applyDiscount("clearance50", 25.0);
            assertEquals(25.0, result);
        });

        test("Floor constraint - small discount within limit", () -> {
            double result = engine.applyDiscount("bonus10", 50.0);
            assertEquals(40.0, result);
        });

        test("Floor constraint with decimal cart total", () -> {
            double result = engine.applyDiscount("winter35", 20.50);
            assertEquals(20.50, result);
        });

        // ===== MIXED TYPE TESTS (v2) =====
        System.out.println("\n--- Mixed Discount Type Tests ---");

        test("Percentage and fixed codes coexist", () -> {
            double result1 = engine.applyDiscount("save10", 100.0);
            double result2 = engine.applyDiscount("holiday20", 100.0);
            assertEquals(90.0, result1);
            assertEquals(80.0, result2);
        });

        test("Multiple fixed codes work independently", () -> {
            double result1 = engine.applyDiscount("holiday20", 100.0);
            double result2 = engine.applyDiscount("spring15", 100.0);
            double result3 = engine.applyDiscount("promo30", 100.0);

            assertEquals(80.0, result1);
            assertEquals(85.0, result2);
            assertEquals(70.0, result3);
        });

        test("Fixed codes with same discount amount", () -> {
            double result1 = engine.applyDiscount("holiday20", 100.0);
            double result2 = engine.applyDiscount("bonus10", 30.0);

            assertEquals(80.0, result1);
            assertEquals(20.0, result2);
        });

        test("Invalid code with both maps populated", () -> {
            double result = engine.applyDiscount("fake_code", 100.0);
            assertEquals(100.0, result);
        });

        test("Switching between percentage and fixed", () -> {
            double result1 = engine.applyDiscount("save10", 100.0);
            double result2 = engine.applyDiscount("holiday20", 100.0);
            double result3 = engine.applyDiscount("summer25", 100.0);

            assertEquals(90.0, result1);
            assertEquals(80.0, result2);
            assertEquals(75.0, result3);
        });

        // ===== TEST SUMMARY =====
        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Total:  " + (passedTests + failedTests));

        if (failedTests == 0) {
            System.out.println("\n✓ All tests passed!");
        } else {
            System.out.println("\n✗ Some tests failed.");
            System.exit(1);
        }
    }

    private static void test(String testName, Runnable testMethod) {
        try {
            testMethod.run();
            System.out.println("  ✓ " + testName);
            passedTests++;
        } catch (AssertionError e) {
            System.out.println("  ✗ " + testName);
            System.out.println("    Error: " + e.getMessage());
            failedTests++;
        } catch (Exception e) {
            System.out.println("  ✗ " + testName);
            System.out.println("    Error: " + e.getMessage());
            failedTests++;
        }
    }

    private static void assertEquals(double expected, double actual) {
        if (Double.compare(expected, actual) != 0) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private interface Runnable {
        void run() throws Exception;
    }
}
