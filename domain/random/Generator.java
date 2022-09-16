package domain.random;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Generator {

    public static final int C = 1013904263;
    public static final double M = Math.pow(2, 32);
    public static final double A = Math.pow(5,9);
    private final int SEED = 73;
    private BigDecimal lastRandom;

    public Generator() {
        this.lastRandom = BigDecimal.valueOf(SEED);
    }

    public BigDecimal generateRandom() {
        final double x = ((A * lastRandom.doubleValue()) + C) % M;
        lastRandom = BigDecimal.valueOf(x);
        return lastRandom.setScale(4, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(M), RoundingMode.HALF_EVEN);
    }
}
