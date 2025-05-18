package kz.saya.finals.common.Enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum NotificationType {
    EMAIL("EMAIL"),
    TELEGRAM("TELEGRAM");

    @Setter
    private String type;

    NotificationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static NotificationType fromString(String type) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.type.equalsIgnoreCase(type)) {
                return notificationType;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }

    public static NotificationType fromStringIgnoreCase(String type) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.type.equalsIgnoreCase(type)) {
                return notificationType;
            }
        }
        return null; // or throw an exception, depending on your use case
    }

    public static NotificationType fromStringOrDefault(String type, NotificationType defaultType) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.type.equalsIgnoreCase(type)) {
                return notificationType;
            }
        }
        return defaultType;
    }

}
