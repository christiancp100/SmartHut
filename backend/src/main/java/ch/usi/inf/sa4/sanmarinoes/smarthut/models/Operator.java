package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import com.google.gson.annotations.SerializedName;

public enum Operator {
    @SerializedName("EQUAL")
    EQUAL,
    @SerializedName("LESS")
    LESS,
    @SerializedName("GREATER")
    GREATER,
    @SerializedName("LESS_EQUAL")
    LESS_EQUAL,
    @SerializedName("GREATER_EQUAL")
    GREATER_EQUAL;

    boolean checkAgainst(double value, double range) {
        switch (this) {
            case EQUAL:
                return value == range;
            case LESS:
                return value < range;
            case GREATER:
                return value > range;
            case GREATER_EQUAL:
                return value >= range;
            case LESS_EQUAL:
                return value <= range;
            default:
                throw new IllegalStateException();
        }
    }
}
