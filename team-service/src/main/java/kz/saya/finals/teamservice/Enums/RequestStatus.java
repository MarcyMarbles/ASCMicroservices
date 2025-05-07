package kz.saya.finals.teamservice.Enums;

public enum RequestStatus {
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    PENDING("PENDING");
    private final String status;

    RequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static RequestStatus fromString(String status) {
        for (RequestStatus s : RequestStatus.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }

    public static RequestStatus fromStringIgnoreCase(String status) {
        for (RequestStatus s : RequestStatus.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }

    public static RequestStatus fromStringIgnoreCaseOrNull(String status) {
        for (RequestStatus s : RequestStatus.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }
}
