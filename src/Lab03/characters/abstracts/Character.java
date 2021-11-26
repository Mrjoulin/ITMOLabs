package Lab03.characters.abstracts;

import Lab03.characters.interfaces.PossessingAntilunite;
import Lab03.characters.interfaces.WeightlessnessSusceptibility;
import Lab03.characters.properties.CharacterPosition;
import Lab03.characters.properties.Status;
import Lab03.things.Antilunite;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.InCavePosition;
import Lab03.utils.properties.CharactersActions;

import java.util.*;

public abstract class Character implements WeightlessnessSusceptibility, PossessingAntilunite {
    private String name;
    private Status status = Status.CHILLING;
    private CharacterPosition position = CharacterPosition.ON_FLOOR;
    private InCavePosition inCavePosition = InCavePosition.CENTER;
    private final HashMap<AbstractMaterial, InCavePosition> foundedMaterials = new HashMap<>();
    private final List<AbstractMaterial> backpack = new ArrayList<>();
    private boolean singular = true;

    public abstract String reactOnChangedWeightlessness(Character[] characters);

    @Override
    public boolean haveAntilunite() {
        return indexOfAntilunite() >= 0;
    }

    @Override
    public Antilunite getAntilunite() {
        return indexOfAntilunite() >= 0 ? new Antilunite() : null;
    }

    @Override
    public Antilunite popAntilunite() {
        Antilunite antilunite = getAntilunite();
        if (antilunite != null) backpack.remove(antilunite);
        return antilunite;
    }

    private int indexOfAntilunite() {
        Antilunite antilunite = new Antilunite();
        return backpack.indexOf(antilunite);
    }

    @Override
    public String exposedToChangedWeightlessness() {
        String action;

        if (!haveAntilunite()) {
            // Change current position to another
            setPosition(position.changed());
            action = CharactersActions.ENDED_UP.getAction(singular);
        } else {
            action = CharactersActions.STAY.getAction(singular);
        }

        return "<name> " + action + " " + position + ".";
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public CharacterPosition getPosition() {
        return position;
    }

    public InCavePosition getInCavePosition() {
        return inCavePosition;
    }

    public HashMap<AbstractMaterial, InCavePosition> getFoundedMaterials() {
        return foundedMaterials;
    }

    public InCavePosition getFoundedMaterialPosition(AbstractMaterial material) {
        if (foundedMaterials.containsKey(material)) return foundedMaterials.get(material);
        return null;
    }

    public boolean inFoundedMaterials(AbstractMaterial material) {
        return foundedMaterials.containsKey(material);
    }

    public List<AbstractMaterial> getBackpack() {
        return backpack;
    }

    public boolean inBackpack(AbstractMaterial material) {
        return backpack.contains(material);
    }

    public boolean isSingular() {
        return singular;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPosition(CharacterPosition position) {
        this.position = position;
    }

    public void setInCavePosition(InCavePosition inCavePosition) {
        this.inCavePosition = inCavePosition;
    }

    public void addFoundMaterialInCave(AbstractMaterial material, InCavePosition position) {
        this.foundedMaterials.put(material, position);
        addMaterialsToBackpack(material);
    }

    public void addMaterialsToBackpack(AbstractMaterial... newMaterials) {
        this.backpack.addAll(Arrays.asList(newMaterials));
    }

    public void setSingular(boolean singular) {
        this.singular = singular;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Character)) return false;
        Character character = (Character) obj;
        return singular == character.singular && Objects.equals(name, character.name) &&
                position == character.position && Objects.equals(backpack, character.backpack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, backpack, singular);
    }
}
