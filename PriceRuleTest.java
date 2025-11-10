import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for PriceRule.
 * @author Shawn Zhu, lab sec L23
 * @version November 8, 2025
 */

public class PriceRuleTest {

    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        Map<String, Double> base = new HashMap<>();
        base.put("MAIN", 20.0);
        base.put("VIP", 50.0);

        PriceRule rule = new PriceRule(base, 5.0);

        assertEquals(20.0, rule.getBaseBySection().get("MAIN"), 0.0001);
        assertEquals(50.0, rule.getBaseBySection().get("VIP"), 0.0001);
        assertEquals(5.0, rule.getPerSeatSurcharge(), 0.0001);
    }
}
