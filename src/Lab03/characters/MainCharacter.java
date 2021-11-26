package Lab03.characters;

import Lab03.characters.abstracts.Character;
import Lab03.characters.properties.Status;
import Lab03.things.Antilunite;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.InCavePosition;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;
import Lab03.utils.properties.CharactersActions;

public class MainCharacter extends Character {
    private boolean mocked = false;

    public MainCharacter(String name, boolean singular) {
        setName(name);
        setSingular(singular);
    }

    public MainCharacter(String name) {
        this(name, true);
    }

    @Override
    public String reactOnChangedWeightlessness(Character[] characters) {
        if (mocked || getStatus() == Status.WORKING) return null;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (charactersOnOtherPositionNames != null) {
            this.mocked = true;

            String action = CharactersActions.MOCK.getAction(isSingular());

            return String.format("<name> %s, которые %s задают.", action, charactersOnOtherPositionNames);
        }

        return null;
    }

    public String decideToTellOtherCharacters(Character[] characters) {
        // Main hero didn't mocked on others :)
        if (!mocked || getStatus() == Status.WORKING) return null;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (charactersOnOtherPositionNames != null && haveAntilunite()) {
            Antilunite antilunite = getAntilunite();

            String action = CharactersActions.CONFESSED.getAction(isSingular());

            return String.format(
                    "Натешившись вдоволь, <name> %s %s, который и %s.",
                    action, antilunite.getName(), antilunite.getUniqueAbility()
            );
        }

        return null;
    }

    public Antilunite popAntiluniteWithMessage() {
        if (haveAntilunite()) {
            Antilunite antilunite = popAntilunite();

            String action = CharactersActions.POP_FROM_BACKPACK.getAction(isSingular());

            System.out.println(getName() + " " + action + " " + antilunite.getName() + ".");

            return antilunite;
        }

        return null;
    }

    public void tellCharactersAboutHowTheyFoundMaterial(Character[] characters, AbstractMaterial material) {
        if (getStatus() == Status.WORKING) return;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnMyPosition = compareCharactersPosition.getCharactersOnSamePositionNames();

        // If any characters found and material was founded
        if (charactersOnMyPosition != null && inFoundedMaterials(material)) {
            String action = CharactersActions.SAID.getAction(isSingular());

            InCavePosition foundedMaterialPosition = getFoundedMaterialPosition(material);

            System.out.printf(
                    "%s %s, что им стоило большого труда отколоть %s от огромнейшей глыбы, " +
                            "найденной %s, так как %s %s.\n",
                    getName(), action, material.getName(), foundedMaterialPosition,
                    material.getName(), material.getHardness()
            );
        }
    }

    public boolean isMocked() {
        return mocked;
    }

    public void setMocked(boolean mocked) {
        this.mocked = mocked;
    }
}
