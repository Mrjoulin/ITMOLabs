package Lab03.things.properties;

public enum InCavePosition {
    CENTER("в центр пещеры", false),
    CORNER("в угол пещеры", false),
    DEEP("в глубине пещеры", true),
    INCOMPREHENSIBLE("с места на место", false);

    private final String position;
    private final boolean special;

    InCavePosition(String position, boolean special) {
        this.position = position;
        this.special = special;
    }

    public String getPosition() {
        return position;
    }

    public boolean isSpecial() {
        return special;
    }

    @Override
    public String toString() {
        return position;
    }
}
