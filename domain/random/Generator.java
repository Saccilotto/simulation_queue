package domain.random;

import domain.Intervalo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generator {

    public static final int C = 1013904263;
    public static final double M = Math.pow(2, 32);
    public static final double A = Math.pow(5, 9);
    private final int SEED = 73;
    private final int maxNumberOfRandoms;
    private BigDecimal lastRandom;

    private int numberOfGeneratedRandoms;

    private static final List<Double> FIXED_RANDOMS = Arrays.asList(0.2176, 0.0103, 0.1109, 0.3456, 0.991, 0.2323, 0.9211, 0.0322, 0.1211, 0.5131, 0.7208, 0.9172, 0.9922, 0.8324, 0.5011, 0.2931);

    public Generator(final int maxNumberOfRandoms) {
        this.lastRandom = BigDecimal.valueOf(SEED);
        this.numberOfGeneratedRandoms = 0;
        this.maxNumberOfRandoms = maxNumberOfRandoms;
    }

    public BigDecimal generateRandom() {
        final double x = ((A * lastRandom.doubleValue()) + C) % M;
        lastRandom = BigDecimal.valueOf(x);
        numberOfGeneratedRandoms++;
        return lastRandom.setScale(4, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(M), RoundingMode.HALF_EVEN);

//        lastRandom = BigDecimal.valueOf(FIXED_RANDOMS.get(numberOfGeneratedRandoms));
//        numberOfGeneratedRandoms++;
//        return lastRandom.setScale(4, RoundingMode.HALF_EVEN);
    }

    public boolean canGenerate() {
        return numberOfGeneratedRandoms < maxNumberOfRandoms;
    }

    public BigDecimal generateRandom(Intervalo intervalo) {
        return this.generateRandom(intervalo.getInicio(), intervalo.getFim());
    }

    public BigDecimal generateRandom(double min, double max) {
        return this.generateRandom()
                .multiply(BigDecimal.valueOf(max - min))
                .add(BigDecimal.valueOf(min));
    }

    public void reset() {
        this.numberOfGeneratedRandoms = 0;
        this.lastRandom = BigDecimal.valueOf(SEED);
    }
}
