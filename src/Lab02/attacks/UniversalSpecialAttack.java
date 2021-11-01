package Lab02.attacks;

import Lab02.utils.DefaultStats;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

import java.util.HashMap;


public class UniversalSpecialAttack extends SpecialMove {
    public static String attackClassName;
    private final UniversalAttack universalAttack;


    public UniversalSpecialAttack(String attackName, Type attackType, HashMap<String ,Double> attackStats) {
        super(
                attackType,
                attackStats.get(DefaultStats.POWER),
                attackStats.get(DefaultStats.ACCURACY),
                attackStats.get(DefaultStats.PRIORITY).intValue(),
                attackStats.get(DefaultStats.HITS).intValue()
        );

        attackClassName = attackName;
        universalAttack = new UniversalAttack(attackClassName);
    }

    @Override
    protected void applyOppEffects(Pokemon poke) {
        this.universalAttack.applyOppEffects(poke);
    }

    @Override
    protected void applySelfEffects(Pokemon poke) {
        this.universalAttack.applySelfEffects(poke);
    }

    @Override
    protected void applyOppDamage(Pokemon poke, double damage) {
        this.universalAttack.applyOppDamage(poke, damage);
    }

    @Override
    protected void applySelfDamage(Pokemon poke, double damage) {
        this.universalAttack.applySelfDamage(poke, damage);
    }

    @Override
    protected String describe() {
        return this.universalAttack.describe;
    }
}
