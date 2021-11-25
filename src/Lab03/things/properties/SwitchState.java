package Lab03.things.properties;

public enum SwitchState {
    ON("включен"),
    OFF("выключен");

    private final String state;

    SwitchState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
