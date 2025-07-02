package ahjd.asgardus.statserivce.utils;

public class StatSettings {
    private final int defaultValue;
    private final int minValue;
    private final int maxValue;

    public StatSettings(int defaultValue, int minValue, int maxValue) {
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int clamp(int value) {
        return Math.max(minValue, Math.min(maxValue, value));
    }
}
