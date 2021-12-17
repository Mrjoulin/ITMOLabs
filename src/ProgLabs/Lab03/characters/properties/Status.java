package Lab03.characters.properties;

public enum Status {
    CHILLING("ничего не делает"),
    WORKING("проверяет при помощи пружинных весов изменение силы тяжести");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
