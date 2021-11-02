package Lab02.attacks;

import Lab02.utils.DefaultStats;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;

import java.io.FileNotFoundException;
import java.util.List;


public class UniversalAttack {
    private List<List<String>> oppAttackEffects;
    private List<List<String>> selfAttackEffects;
    private List<String> oppAttackDamage;
    private List<String> selfAttackDamage;
    public String describe;

    public UniversalAttack (String attackClassName) {
        try {
            DefaultStats ds = new DefaultStats(DefaultStats.attacksFileName, attackClassName);

            oppAttackEffects = ds.getDefaultAttackOpponentEffects();
            selfAttackEffects = ds.getDefaultAttackSelfEffects();
            oppAttackDamage = ds.getDefaultAttackOpponentDamage();
            selfAttackDamage = ds.getDefaultAttackSelfDamage();
            describe = ds.getDefaultAttackDescribe();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void applyOppEffects(Pokemon poke) {
        applyEffects(poke, oppAttackEffects);
    }

    protected void applySelfEffects(Pokemon poke) {
        applyEffects(poke, selfAttackEffects);
    }

    protected void applyOppDamage(Pokemon poke, double damage) {
        applyDamage(poke, oppAttackDamage, damage);
    }

    protected void applySelfDamage(Pokemon poke, double damage) {
        applyDamage(poke, selfAttackDamage, damage);
    }

    protected void applyEffects(Pokemon poke, List<List<String>> attackEffects) {
        if (attackEffects == null || attackEffects.isEmpty()) return;

        for (List<String> attackEffect : attackEffects) {
            try {
                Status attackStatus = Status.valueOf(attackEffect.get(0));
                double turns = Double.parseDouble(attackEffect.get(1));
                double chance = Double.parseDouble(attackEffect.get(2));
                double attack = Double.parseDouble(attackEffect.get(3));

                Effect effect = (new Effect()).condition(attackStatus).turns((int) turns).chance(chance).attack(attack);
                poke.addEffect(effect);
            } catch (IllegalArgumentException e) {
                String effectName = attackEffect.get(0);

                if (effectName.equals("CONFUSE")) {
                    double chance = Double.parseDouble(attackEffect.get(1));
                    if (Math.random() < chance) Effect.confuse(poke);
                } else if (effectName.startsWith("ADD_") && effectName.split("_").length == 2) {
                    // Add some stages to stat of pokemon
                    String stat = effectName.split("_")[1];
                    double chance = Double.parseDouble(attackEffect.get(1));
                    double add_stages = Double.parseDouble(attackEffect.get(2));

                    if (Math.random() < chance) {
                        if (stat.equals("ALL")) {
                            for (Stat cur : Stat.values()) {
                                if (!cur.isHidden()) poke.setMod(cur, (int) add_stages);
                            }
                        } else if (stat.equals("FULL_HP")) {
                            double full_hp = poke.getHP() - poke.getStat(Stat.HP);
                            poke.setMod(Stat.HP, (int) full_hp);
                        } else {
                            poke.setMod(Stat.valueOf(stat), (int) add_stages);
                        }
                    }
                }
            }
        }
    }

    protected void applyDamage(Pokemon poke, List<String> attackDamage, double damage) {
        if (attackDamage == null || attackDamage.isEmpty()) return;

        String effectName = attackDamage.get(0);

        if (effectName.equals("DEFAULT")) {
            poke.setMod(Stat.HP, (int) Math.round(damage));
        } else if (effectName.equals("DAMAGE_PERCENT")) {
            // Apply some percent of damage to pokemon
            double chance = Double.parseDouble(attackDamage.get(1));
            double percent = Double.parseDouble(attackDamage.get(2));

            if (Math.random() < chance) {
                poke.setMod(Stat.HP, (int) Math.round(percent * damage));
            }
        }
    }
}
