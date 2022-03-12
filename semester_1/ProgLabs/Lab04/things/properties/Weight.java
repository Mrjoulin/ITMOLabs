package Lab04.things.properties;

public enum Weight {
    LIGHT("лёгкий"),
    MIDDLE("что-то среднее между лёгким и тяжелым"),
    HEAVY("тяжёлый");

    private final String name;

    Weight(String name) {
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
