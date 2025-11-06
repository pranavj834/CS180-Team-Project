/*
What it is: (Manager-only) a config DTO to send to the server when updating pricing rules.

Used by: PacketFactory.setPriceRule(...) → handled server-side.
 */

import java.io.Serializable;
import java.util.Map;

public class PriceRule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Double> baseBySection;
    private double perSeatSurcharge;

    public PriceRule(Map<String, Double> baseBySection, double perSeatSurcharge) {
        this.baseBySection = baseBySection;
        this.perSeatSurcharge = perSeatSurcharge;
    }

    public Map<String, Double> getBaseBySection() { return baseBySection; }
    public double getPerSeatSurcharge() { return perSeatSurcharge; }
}
