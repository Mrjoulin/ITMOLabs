package Lab03.things.abstracts;

import Lab03.things.interfaces.MovableDevice;
import Lab03.things.interfaces.Switchable;
import Lab03.things.properties.InCavePosition;
import Lab03.things.properties.SwitchState;

import java.util.Objects;

public abstract class AbstractWeightlessnessDevice implements Switchable, MovableDevice {
    private SwitchState state = SwitchState.OFF;
    private InCavePosition position = InCavePosition.CENTER;

    public SwitchState getState() {
        return state;
    }

    public InCavePosition getPosition() {
        return position;
    }

    public void setState(SwitchState state) {
        this.state = state;
    }

    public void setPosition(InCavePosition position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return state.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (! (obj instanceof AbstractWeightlessnessDevice)) return false;
        AbstractWeightlessnessDevice device = (AbstractWeightlessnessDevice) obj;
        return this.state == device.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
