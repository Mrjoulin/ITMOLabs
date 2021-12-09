package Lab03.things.properties;

public enum Color {
    WHITE("белый"),
    BLACK("чёрный"),
    RAD("красный"),
    BRIGHT_RAD("ярко-красный"),
    BLUE("синий"),
    BRIGHT_BLUE("яско-синий"),
    GREEN("зелёный"),
    BRIGHT_GREEN("светло-зелёный"),
    GRAY("тёмно-серый"),
    BRIGHT_GRAY("серый"),
    PURPLE("фиолетовый"),
    BRIGHT_PURPLE("ярко-фиолетовый");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
