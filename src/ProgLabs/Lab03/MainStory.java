package Lab03;

/*
Отрывок рассказа:

Общее недоумение вызвал тот факт, что сам Знайка, а также профессор Звездочкин в силу каких-то причин не подверглись
действию невесомости и как ни в чем не бывало продолжали работать внизу. Они переносили прибор невесомости с места
на место, отходили от него в дальние уголки пещеры, проверяя при помощи пружинных весов изменение силы тяжести в
разных местах. Все спрашивали Знайку и Звездочкина, почему на них не действует невесомость, но Знайка и Звездочкин
только посмеивались втихомолку и делали вид, что не слышат вопросов. Натешившись вдоволь, они признались, что нашли
антилунит, который и позволяет им сохранить вес. Выключив прибор невесомости, в результате чего все коротышки
моментально опустились вниз, Знайка вытряхнул из своего рюкзака несколько мелких камней. Все с интересом принялись
разглядывать их. Камни были твердые, плотные, по виду напоминавшие кремень, но в отличие от кремня они были не
темно-серого, а яркого фиолетового цвета и к тому же обладали какой-то энергией, в силу которой притягивались
друг к другу, подобно тому, как притягиваются наэлектризованные предметы или кусочки намагниченного железа.
Знайка сказал, что им стоило большого труда отколоть эти камешки от огромнейшей глыбы, найденной в глубине пещеры,
так как антилунит чрезвычайно твердое вещество.

*/


import Lab03.characters.MainCharacter;
import Lab03.characters.MinorCharacter;
import Lab03.places.Cave;
import Lab03.places.Work;
import Lab03.things.Antilunite;
import Lab03.things.Material;
import Lab03.things.abstracts.AbstractMaterial;
import Lab03.things.properties.Attraction;
import Lab03.things.properties.Color;
import Lab03.things.properties.Hardness;
import Lab03.utils.abstracts.CharactersMessagesPrint;

import java.util.ArrayList;
import java.util.List;

public class MainStory {
    public static void main(String[] args) {
        // Create characters
        MainCharacter znayka = new MainCharacter("Знайка");
        MainCharacter zvezdochkin = new MainCharacter("Звёздочкин");

        MinorCharacter others = new MinorCharacter("Все", false);
        // Create arrays with main and minor characters
        MainCharacter[] mainCharacters = {zvezdochkin, znayka};
        MinorCharacter[] minorCharacters = {others};

        // Add materials to characters
        others.addMaterialsToBackpack(
                new Material("Кремень", Color.GRAY, Hardness.HIGH, Attraction.NO),
                new Material("Кусочки намагниченного железа", Color.WHITE, Hardness.MEDIUM, Attraction.ORDINARY)
        );

        Cave cave = new Cave();
        Work work = new Work(mainCharacters, minorCharacters);

        // Part 1
        cave.fullCave();

        // Main characters explore cave (without messages here, character will tell about the find at the end)
        work.exploreCave(cave, mainCharacters);

        work.startWorking();

        if (others.isMisunderstanding()) {
            List<String> messages = new ArrayList<>();

            for (MainCharacter mainCharacter: mainCharacters) {
                String message = mainCharacter.decideToTellOtherCharacters(work.getAllCharacters());
                messages.add(message);
            }

            CharactersMessagesPrint.optimizePrintCharactersMessages(mainCharacters, messages);

            others.setMisunderstanding(false);
        }

        work.stopWorking();

        // Part 2

        Antilunite znaykasAntilunite = znayka.popAntiluniteWithMessage();
        others.seeNewMaterial(znaykasAntilunite);

        if (others.isInterested()) {
            // Start compare with another materials
            for (AbstractMaterial material: others.getBackpack())
                znaykasAntilunite.compare(material);
        }

        znayka.tellCharactersAboutHowTheyFoundMaterial(work.getAllCharacters(), znaykasAntilunite);
    }
}
