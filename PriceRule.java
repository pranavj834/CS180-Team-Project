import java.io.Serializable;
import java.util.Map;

/**
 * A data transfer object describing pricing rules for the restaurant.
 *
 * <p>This configuration is typically maintained by a manager and sent to the
 * server when updating how reservations are priced. It can express different
 * base prices per section as well as a per-seat surcharge.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class PriceRule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Double> baseBySection; // base price per section ID
    private double perSeatSurcharge;          // additional cost per seat

    /**
     * Constructs a new pricing rule configuration.
     *
     * @param baseBySection    mapping from section ID to base price
     * @param perSeatSurcharge additional price applied per seat reserved
     */
    public PriceRule(Map<String, Double> baseBySection, double perSeatSurcharge) {
        this.baseBySection = baseBySection;
        this.perSeatSurcharge = perSeatSurcharge;
    }

    /**
     * Returns the base price map keyed by section identifier.
     *
     * @return map from section ID to base price
     */
    public Map<String, Double> getBaseBySection() {
        return baseBySection;
    }

    /**
     * Returns the surcharge added for each seat.
     *
     * @return per-seat surcharge value
     */
    public double getPerSeatSurcharge() {
        return perSeatSurcharge;
    }
}
