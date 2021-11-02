package Lab02;

import Lab02.pokemons.names.*;
import Lab02.utils.DefaultStats;
import ru.ifmo.se.pokemon.Battle;

public class Main {
    public static void main(String[] args) {
        // Create pokemons
        Zapdos zapdos = new Zapdos("Furia", DefaultStats.DEFAULT_LEVEL);
        Stunky stunky = new Stunky("Shura", DefaultStats.DEFAULT_LEVEL);
        Starly starly = new Starly("Shurizard", DefaultStats.DEFAULT_LEVEL);
        Staravia staravia = new Staravia("Domp", DefaultStats.DEFAULT_LEVEL);
        Staraptor staraptor = new Staraptor("Dompra", DefaultStats.DEFAULT_LEVEL);
        Skuntank skuntank = new Skuntank("Domprapter", DefaultStats.DEFAULT_LEVEL);
        // Create new battle
        Battle battle = new Battle();
        // Add pokemons to Ally
        battle.addAlly(zapdos);
        battle.addAlly(starly);
        battle.addAlly(staraptor);
        // Add pokemons to Foe
        battle.addFoe(stunky);
        battle.addFoe(staravia);
        battle.addFoe(skuntank);
        // Run battle
        battle.go();
    }
}
