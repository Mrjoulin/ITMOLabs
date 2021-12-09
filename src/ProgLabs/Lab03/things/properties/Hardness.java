package Lab03.things.properties;

public enum Hardness {
    LOW("не твёрдый"),
    MEDIUM("твёрдый"),
    HIGH("очень твёрдый");

    private final String level;

    Hardness(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }

    @Override
    public String toString() {
        return this.level;
    }
}
