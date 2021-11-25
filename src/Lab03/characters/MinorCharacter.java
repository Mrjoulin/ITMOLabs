package Lab03.characters;

import Lab03.characters.abstracts.Character;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;

public class MinorCharacter extends Character {
    private boolean misunderstanding = false;
    private boolean interested = false;

    public MinorCharacter(String name, boolean singular) {
        setName(name);
        setSingular(singular);
    }

    @Override
    public void reactOnChangedWeightlessness(Character[] characters) {
        // If Character already misunderstand what happen
        if (this.misunderstanding) return;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (!charactersOnOtherPositionNames.isEmpty()) {
            this.misunderstanding = true;

            String action = charactersOnOtherPositionNames.contains(" и ") ? "остались" : "остался";

            System.out.printf(
                    "%s в недоумении, что %s %s %s.\n",
                    getName(), charactersOnOtherPositionNames, action, getPosition().changed()
            );
        }
    }

    public void seeNewMaterial(AbstractMaterial material) {
        if (!inBackpack(material)) {
            interested = true;

            String action = isSingular() ? "принялся" : "принялись";

            System.out.printf("%s с интересом %s разглядывать %s.\n", getName(), action, material.getName());
        }
    }

    public boolean isMisunderstanding() {
        return misunderstanding;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setMisunderstanding(boolean misunderstanding) {
        this.misunderstanding = misunderstanding;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }
}
