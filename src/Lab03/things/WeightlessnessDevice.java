package Lab03.things;

import Lab03.things.abstracts.AbstractWeightlessnessDevice;
import Lab03.things.properties.InCavePosition;
import Lab03.things.properties.SwitchState;

public class WeightlessnessDevice extends AbstractWeightlessnessDevice {
    @Override
    public void charactersMovesDevice(String characterName, InCavePosition position) {
        if (getPosition() != position) {
            setPosition(position);

            String action = characterName.contains(" и ") ? "преместили" : "переместил";

            System.out.printf("%s %s прибор невесомости %s.\n", characterName, action, position);
        }
    }

    @Override
    public void turnOn() {
        if (getState() != SwitchState.ON) {
            setState(SwitchState.ON);
            System.out.println("Включаем прибор невесомости.");
        }
    }

    @Override
    public void turnOff() {
        if (getState() != SwitchState.OFF) {
            setState(SwitchState.OFF);
            System.out.println("Выключаем прибор невесомости.");
        }
    }
}
