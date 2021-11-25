package Lab03.things.properties;

public enum InCavePosition {
    CENTER("в центр пещеры"),
    CORNER("в угол пещеры"),
    DEEP("в глубине пещеры"),
    INCOMPREHENSIBLE("с места на место");

    private final String position;

    InCavePosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return position;
    }
}
