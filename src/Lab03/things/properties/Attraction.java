package Lab03.things.properties;

public enum Attraction {
    NO("не притягиваются"),
    WEAK("слабо притягиваются"),
    ORDINARY("притягиваются"),
    STRONG("сильно притягиваются");

    private final String level;

    Attraction(String level) {
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
