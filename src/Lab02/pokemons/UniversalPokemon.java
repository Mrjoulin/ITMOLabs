package Lab02.pokemons;

import Lab02.attacks.UniversalPhysicalAttack;
import Lab02.attacks.UniversalSpecialAttack;
import Lab02.attacks.UniversalStatusAttack;
import Lab02.utils.DefaultStats;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

import java.util.HashMap;
import java.util.List;


public class UniversalPokemon extends Pokemon {
    public UniversalPokemon(String pokemonClassName, String name, int lvl) {
        super(name, lvl);

        List<Type> pokemonTypes = DefaultStats.getDefaultPokemonTypes(pokemonClassName);
        HashMap<String, Double> pokemonStats = DefaultStats.getDefaultPokemonStats(pokemonClassName);

        if (pokemonTypes != null && pokemonStats != null) {
            setPokemonStats(
                    pokemonTypes,
                    pokemonStats.get(DefaultStats.HP),
                    pokemonStats.get(DefaultStats.ATTACK),
                    pokemonStats.get(DefaultStats.DEFENSE),
                    pokemonStats.get(DefaultStats.SPECIAL_ATTACK),
                    pokemonStats.get(DefaultStats.SPECIAL_DEFENSE),
                    pokemonStats.get(DefaultStats.SPEED)
            );
        }

        List<String> pokemonAttacks = DefaultStats.getDefaultPokemonAttacks(pokemonClassName);

        if (pokemonAttacks != null) {
            System.out.println("Num attacks for " + pokemonClassName + ": " + pokemonAttacks.size());

            for (String pokemonAttackName: pokemonAttacks) {
                Type attackType = DefaultStats.getDefaultAttackType(pokemonAttackName);
                HashMap<String, Double> attackStats = DefaultStats.getDefaultAttackStats(pokemonAttackName);
                String attackExtends = DefaultStats.getDefaultAttackExtends(pokemonAttackName);

                System.out.println("Add attacks to " + pokemonClassName + ": " + pokemonAttackName);

                if (attackType != null && attackStats != null && attackExtends != null) {
                    switch (attackExtends) {
                        case "Special" : {
                            this.addMove(new UniversalSpecialAttack(pokemonAttackName, attackType, attackStats));
                            break;
                        }
                        case "Physical" : {
                            this.addMove(new UniversalPhysicalAttack(pokemonAttackName, attackType, attackStats));
                            break;
                        }
                        case "Status" : {
                            this.addMove(new UniversalStatusAttack(pokemonAttackName, attackType, attackStats));
                        }
                    }
                }
            }
        }
    }

    private void setPokemonStats(
            List<Type> types,
            double hp,
            double attack,
            double defense,
            double specialAttack,
            double specialDefense,
            double speed
        ) {

        Type[] typesArray = types.toArray(new Type[0]);

        this.setType(typesArray);
        this.setStats(hp, attack, defense, specialAttack, specialDefense, speed);
    }
}
