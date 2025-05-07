package kz.saya.finals.common.Enums;

public enum ScrimType {
    SOLO("Solo"),
    DUO("Duo"),
    SQUAD("Squad");

    private final String type;

    ScrimType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ScrimType fromString(String type) {
        for (ScrimType s : ScrimType.values()) {
            if (s.type.equalsIgnoreCase(type)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }

    public static ScrimType fromStringIgnoreCase(String type) {
        for (ScrimType s : ScrimType.values()) {
            if (s.type.equalsIgnoreCase(type)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }

    public static ScrimType fromStringIgnoreCaseOrNull(String type) {
        for (ScrimType s : ScrimType.values()) {
            if (s.type.equalsIgnoreCase(type)) {
                return s;
            }
        }
        return null;
    }
}
