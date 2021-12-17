package Lab03.things.properties;

public enum UniqueAbility {
    FLY("позволяет летать"),
    CHANGE_GRAVITY("изменяет гравитацию"),
    KEEP_WEIGHT("позволяет сохранять вес"),
    WALK_ON_WATER("позволяет ходить по воде");

    private final String ability;

    UniqueAbility(String ability) {
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }

    @Override
    public String toString() {
        return ability;
    }
}
