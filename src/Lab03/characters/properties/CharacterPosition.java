package Lab03.characters.properties;

public enum CharacterPosition {
    ON_FLOOR("на полу"),
    UNDER_CEILING("под полоком");

    private String positionName;

    CharacterPosition(String positionName) {
        this.positionName = positionName;
    }

    public CharacterPosition changed() {
        if (positionName.equals(ON_FLOOR.positionName)) {
            return UNDER_CEILING;
        }

        return ON_FLOOR;
    }

    public String getPositionName() {
        return this.positionName;
    }

    @Override
    public String toString() {
        return this.positionName;
    }
}
