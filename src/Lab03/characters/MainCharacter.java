package Lab03.characters;

import Lab03.characters.abstracts.Character;
import Lab03.characters.properties.Status;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.Antilunite;
import Lab03.things.properties.InCavePosition;
import Lab03.utils.CompareCharacters;
import Lab03.utils.interfaces.ComparableCharactersPosition;

public class MainCharacter extends Character {
    private boolean mocked = false;

    public MainCharacter(String name, boolean haveAntilunite, boolean singular) {
        setName(name);
        setSingular(singular);

        if (haveAntilunite) {
            Antilunite antilunite = new Antilunite();
            addMaterialsToBackpack(antilunite);
        }
    }

    public MainCharacter(String name, boolean haveAntilunite) {
        this(name, haveAntilunite, true);
    }

    @Override
    public void reactOnChangedWeightlessness(Character[] characters) {
        if (mocked || getStatus() == Status.WORKING) return;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (charactersOnOtherPositionNames != null) {
            this.mocked = true;

            String action = isSingular() ? "посмеивался втихомолку и делал вид, что не слышит вопросов" :
                    "посмеивались втихомолку и делали вид, что не слышат вопросов";

            System.out.printf(
                    "%s %s, которые %s задают.\n", getName(), action, charactersOnOtherPositionNames
            );
        }
    }

    public void decideToTellOtherCharacters(Character[] characters) {
        // Main hero didn't mocked on others :)
        if (!mocked || getStatus() == Status.WORKING) return;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnOtherPositionNames = compareCharactersPosition.getCharactersOnOtherPositionNames();

        // If any characters found
        if (charactersOnOtherPositionNames != null && haveAntilunite()) {
            Antilunite antilunite = getAntilunite();

            String action = isSingular() ? "признался, что нашёл" : "признались, что нашли";

            System.out.printf(
                    "Натешившись вдоволь, %s %s %s, который и %s.\n",
                    getName(), action, antilunite.getName(), antilunite.getUniqueAbility()
            );
        }
    }

    public Antilunite popAntiluniteWithMessage() {
        if (haveAntilunite()) {
            Antilunite antilunite = popAntilunite();

            String action = isSingular() ? " вытряхнул из своего рюкзака " : " вытряхнули из своих рюкзаков ";

            System.out.println(getName() + action + antilunite.getName() + ".");

            return antilunite;
        }

        return null;
    }

    public void tellCharactersAboutHowTheyGotMaterial(Character[] characters, AbstractMaterial material) {
        if (getStatus() == Status.WORKING) return;

        ComparableCharactersPosition compareCharactersPosition = new CompareCharacters(this, characters);

        String charactersOnMyPosition = compareCharactersPosition.getCharactersOnSamePositionNames();

        // If any characters found and material is Antilunite
        if (charactersOnMyPosition != null && material.equals(new Antilunite())) {
            String action = isSingular() ? "сказал" : "сказали";

            System.out.printf(
                    "%s %s, что им стоило большого труда отколоть %s от огромнейшей глыбы, " +
                            "найденной %s, так как %s %s.\n",
                    getName(), action, material.getName(), InCavePosition.DEEP,
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
