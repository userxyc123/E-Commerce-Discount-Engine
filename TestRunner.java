public class TestRunner {
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Discount Engine Test Suite ===\n");

        DiscountEngine engine = new DiscountEngine();

        // Test 1: Valid code applies correct discount
        test("Valid code applies correct discount", () -> {
            double result = engine.applyDiscount("save10", 100.0);
            assertEquals(90.0, result);
        });

        // Test 2: Valid code with 25% discount
        test("Valid code with 25% discount", () -> {
            double result = engine.applyDiscount("summer25", 80.0);
            assertEquals(60.0, result);
        });

        // Test 3: Valid code with 50% discount
        test("Valid code with 50% discount", () -> {
            double result = engine.applyDiscount("welcome50", 100.0);
            assertEquals(50.0, result);
        });

        // Test 4: Case insensitivity - uppercase
        test("Case insensitivity - uppercase", () -> {
            double result = engine.applyDiscount("SAVE10", 100.0);
            assertEquals(90.0, result);
        });

        // Test 5: Case insensitivity - mixed case
        test("Case insensitivity - mixed case", () -> {
            double result = engine.applyDiscount("SaVe10", 100.0);
            assertEquals(90.0, result);
        });

        // Test 6: Case insensitivity - with whitespace
        test("Case insensitivity - with whitespace", () -> {
            double result = engine.applyDiscount("  save10  ", 100.0);
            assertEquals(90.0, result);
        });

        // Test 7: Invalid code returns original total
        test("Invalid code returns original total", () -> {
            double result = engine.applyDiscount("nonexistent", 100.0);
            assertEquals(100.0, result);
        });

        // Test 8: Invalid code with random string
        test("Invalid code with random string", () -> {
            double result = engine.applyDiscount("xyz123", 50.0);
            assertEquals(50.0, result);
        });

        // Test 9: Null code returns original total
        test("Null code returns original total", () -> {
            double result = engine.applyDiscount(null, 100.0);
            assertEquals(100.0, result);
        });

        // Test 10: Calculation accuracy - small discount
        test("Calculation accuracy - small discount", () -> {
            double result = engine.applyDiscount("loyalty5", 100.0);
            assertEquals(95.0, result);
        });

        // Test 11: Calculation accuracy - large discount
        test("Calculation accuracy - large discount", () -> {
            double result = engine.applyDiscount("clearance40", 100.0);
            assertEquals(60.0, result);
        });

        // Test 12: Calculation with decimal cart total
        test("Calculation with decimal cart total", () -> {
            double result = engine.applyDiscount("save20", 99.99);
            assertEquals(79.99, result);
        });

        // Test 13: Calculation with odd number discount
        test("Calculation with odd number discount", () -> {
            double result = engine.applyDiscount("bulk12", 75.50);
            assertEquals(66.44, result);
        });

        // Test 14: Zero cart total
        test("Zero cart total", () -> {
            double result = engine.applyDiscount("save10", 0.0);
            assertEquals(0.0, result);
        });

        // Test 15: Very small cart total
        test("Very small cart total", () -> {
            double result = engine.applyDiscount("save10", 0.99);
            assertEquals(0.89, result);
        });

        // Test 16: Negative cart total returns unchanged
        test("Negative cart total returns unchanged", () -> {
            double result = engine.applyDiscount("save10", -10.0);
            assertEquals(-10.0, result);
        });

        // Test 17: Multiple codes work independently
        test("Multiple codes work independently", () -> {
            double result1 = engine.applyDiscount("save10", 100.0);
            double result2 = engine.applyDiscount("save15", 100.0);
            double result3 = engine.applyDiscount("vip35", 100.0);

            assertEquals(90.0, result1);
            assertEquals(85.0, result2);
            assertEquals(65.0, result3);
        });

        // Test 18: Rounding to 2 decimal places
        test("Rounding to 2 decimal places", () -> {
            double result = engine.applyDiscount("save10", 33.33);
            assertEquals(30.0, result);
        });

        // Test 19: Rounding with complex calculation
        test("Rounding with complex calculation", () -> {
            double result = engine.applyDiscount("flash30", 99.99);
            assertEquals(69.99, result);
        });

        // Test 20: Same code multiple times returns consistent results
        test("Same code multiple times returns consistent results", () -> {
            double result1 = engine.applyDiscount("save20", 50.0);
            double result2 = engine.applyDiscount("save20", 50.0);
            assertEquals(result1, result2);
            assertEquals(40.0, result1);
        });

        // Test 21: Multiple codes with same discount
        test("Multiple codes with same discount", () -> {
            double result1 = engine.applyDiscount("save15", 100.0);
            double result2 = engine.applyDiscount("newuser15", 100.0);
            assertEquals(result1, result2);
            assertEquals(85.0, result1);
        });

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
            System.out.println("✓ " + testName);
            passedTests++;
        } catch (AssertionError e) {
            System.out.println("✗ " + testName);
            System.out.println("  Error: " + e.getMessage());
            failedTests++;
        } catch (Exception e) {
            System.out.println("✗ " + testName);
            System.out.println("  Error: " + e.getMessage());
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
