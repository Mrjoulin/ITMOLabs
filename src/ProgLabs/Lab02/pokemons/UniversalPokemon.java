package Lab02.pokemons;

import Lab02.attacks.UniversalPhysicalAttack;
import Lab02.attacks.UniversalSpecialAttack;
import Lab02.attacks.UniversalStatusAttack;
import Lab02.utils.DefaultStats;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;


public class UniversalPokemon extends Pokemon {
    public UniversalPokemon(String pokemonClassName, String name, int lvl) {
        super(name, lvl);

        try {
            DefaultStats ds = new DefaultStats(DefaultStats.pokemonsFileName, pokemonClassName);

            List<Type> pokemonTypes = ds.getDefaultPokemonTypes();
            HashMap<String, Double> pokemonStats = ds.getDefaultPokemonStats();
            List<String> pokemonAttacks = ds.getDefaultPokemonAttacks();

            setPokemonStats(pokemonTypes, pokemonStats);
            setPokemonAttacks(pokemonAttacks);
        } catch (FileNotFoundException e) {
            System.out.println("Get exception while reading pokemons file:");
            e.printStackTrace();
        }
    }

    private void setPokemonAttacks(List<String> pokemonAttacks) {
        if (pokemonAttacks == null) return;

        for (String pokemonAttackName: pokemonAttacks) {
            try {
                DefaultStats ds = new DefaultStats(DefaultStats.attacksFileName, pokemonAttackName);

                Type attackType = ds.getDefaultAttackType();
                HashMap<String, Double> attackStats = ds.getDefaultAttackStats();
                String attackExtends = ds.getDefaultAttackExtends();

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
            } catch (FileNotFoundException e) {
                System.out.println("Get exception while reading attacks file:");
                e.printStackTrace();
            }
        }
    }

    private void setPokemonStats(List<Type> types, HashMap<String, Double> stats) {
        if (types == null || stats == null) return;

        Type[] typesArray = types.toArray(new Type[0]);

        this.setType(typesArray);
        this.setStats(
                stats.get(DefaultStats.HP),
                stats.get(DefaultStats.ATTACK),
                stats.get(DefaultStats.DEFENSE),
                stats.get(DefaultStats.SPECIAL_ATTACK),
                stats.get(DefaultStats.SPECIAL_DEFENSE),
                stats.get(DefaultStats.SPEED)
        );
    }
}
