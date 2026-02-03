package custom.velvet.entity.enums;

public enum ServiceStatus {
    OK(3, "");
    private int value;
    private String message;

    ServiceStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
