package Lab03.characters;

import Lab03.characters.abstracts.Character;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;
import Lab03.utils.properties.CharactersActions;

public class MinorCharacter extends Character {
    private boolean misunderstanding = false;
    private boolean interested = false;

    public MinorCharacter(String name, boolean singular) {
        setName(name);
        setSingular(singular);
    }

    public MinorCharacter(String name) {
        this(name, true);
    }

    @Override
    public String reactOnChangedWeightlessness(Character[] characters) {
        // If Character already misunderstand what happen
        if (this.misunderstanding) return null;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (!charactersOnOtherPositionNames.isEmpty()) {
            this.misunderstanding = true;

            String action = CharactersActions.STAY.getAction(!charactersOnOtherPositionNames.contains(" и "));

            return String.format(
                    "<name> в недоумении, что %s %s %s.",
                    charactersOnOtherPositionNames, action, getPosition().changed()
            );
        }

        return null;
    }

    public void seeNewMaterial(AbstractMaterial material) {
        if (!inBackpack(material)) {
            interested = true;

            String action = CharactersActions.BEGIN.getAction(isSingular());

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
