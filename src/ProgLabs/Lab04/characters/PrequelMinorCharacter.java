package Lab04.characters;

import Lab03.characters.MinorCharacter;
import Lab03.characters.properties.CharacterPosition;
import Lab03.utils.properties.CharactersActions;
import Lab04.characters.properties.ExtendedCharactersActions;
import Lab04.things.abstracts.AbstractEquipment;
import Lab04.things.properties.Size;
import Lab04.things.properties.Weight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrequelMinorCharacter extends MinorCharacter {
    private boolean successfulTryToGetDown = true;
    private final List<AbstractEquipment> equipment = new ArrayList<>();

    public PrequelMinorCharacter(String name, boolean singular) {
        super(name, singular);
    }

    public PrequelMinorCharacter(String name) {
        this(name, true);
    }

    private AbstractEquipment findEquipmentPreventingFromGoingDown() {
        for (AbstractEquipment curEquipment: this.equipment) {
            if (curEquipment.getEquipmentSize() == Size.BIG ||
                    curEquipment.getEquipmentWeight() == Weight.HEAVY) {
                return curEquipment;
            }
        }
        return null;
    }

    public String tryToGetDown() {
        if (getPosition() != CharacterPosition.UNDER_CEILING) return null;

        if (findEquipmentPreventingFromGoingDown() != null) {
            successfulTryToGetDown = false;

            String action = ExtendedCharactersActions.TRIED.getAction(isSingular());

            return String.format(
                    "Плавая %s пещеры в самых разнообразных позах, <name> всячески %s спуститься вниз, " +
                            "но попытки были малоуспешны.",
                    getPosition(), action
            );
        } else {
            setPosition(getPosition().changed());
            successfulTryToGetDown = true;

            String action = CharactersActions.ENDED_UP.getAction(isSingular());

            return String.format("Приложив не мало усилилий, <name> всё же %s %s", action, getPosition());
        }
    }

    public String useReactiveForcesToGetDown(){
        // If character not under the ceiling or already get down
        if (getPosition() != CharacterPosition.UNDER_CEILING || successfulTryToGetDown) return null;

        AbstractEquipment interferingEquipment = findEquipmentPreventingFromGoingDown();

        if (interferingEquipment != null) {
            successfulTryToGetDown = false;

            String action = ExtendedCharactersActions.CANT.getAction(isSingular());

            return String.format(
                    "Находясь в %s, <name> %s точно рассчитать свои " +
                            "телодвижения и использовать реактивные силы, чтобы спуститься вниз.",
                    interferingEquipment, action
            );
        } else {
            setPosition(getPosition().changed());
            successfulTryToGetDown = true;

            String action = ExtendedCharactersActions.ABLE_TO_DO.getAction(isSingular());

            return String.format(
                    "Используя реактивные силы <name> %s точно рассчитать свои телодвижения и спуститься вниз.",
                    action
            );
        }
    }


    public boolean isSuccessfulTryToGetDown() {
        return successfulTryToGetDown;
    }

    public List<AbstractEquipment> getEquipment() {
        return equipment;
    }

    public boolean inEquipment(AbstractEquipment equipment) {
        return this.equipment.contains(equipment);
    }

    public void setSuccessfulTryToGetDown(boolean successfulTryToGetDown) {
        this.successfulTryToGetDown = successfulTryToGetDown;
    }

    public void addEquipment(AbstractEquipment... newEquipment) {
        this.equipment.addAll(Arrays.asList(newEquipment));
    }
}
