package Lab04.things.properties;

public enum Size {
    SMALL("маленьком"),
    MEDIUM("небольшом"),
    BIG("большом");

    private final String name;

    Size(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
