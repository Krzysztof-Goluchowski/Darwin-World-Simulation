package org.model;

import java.util.*;

//Klasa tworzy Board ale z tunelami
public class BoardWithTunnels extends Board {
    private final int width;
    private final int height;
    private final Map<Vector2D, Animal> animals;
    private final Map<Vector2D, Plant> plants;
    List<Map<Vector2D, Vector2D>> tunnelsMaps;

    public BoardWithTunnels(int width, int height, int numberOfTunnels){
        super(width, height);
        this.width = width;
        this.height = height;
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
        this.tunnelsMaps = List.of(new HashMap<>(), new HashMap<>());

        //Losuje pary punktow i dodaje do dwoch HashMap w tunnelsMpas
        Random random = new Random();
        for (int i = 0; i < numberOfTunnels; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            Vector2D point1 = new Vector2D(x1, y1);

            int x2, y2;
            do {
                x2 = random.nextInt(width);
                y2 = random.nextInt(height);
            } while (x2 == x1 && y2 == y1); // Zapewnia, że punkty tunelu nie będą takie same

            Vector2D point2 = new Vector2D(x2, y2);

            tunnelsMaps.get(0).put(point1, point2);
            tunnelsMaps.get(1).put(point2, point1);
        }
    }
    @Override //Nadpisanie move aby uwzglednial wejscie na tunel
    public void move(Animal animal) {
        super.move(animal); // Wywołanie oryginalnej metody move

        // Sprawdzanie, czy nowa pozycja zwierzęcia jest wejściem do tunelu
        for (Map<Vector2D, Vector2D> tunnelMap : tunnelsMaps) {
            Vector2D newPosition = animal.getPosition();
            if (tunnelMap.containsKey(newPosition)) {
                Vector2D tunnelExit = tunnelMap.get(newPosition);

                if(plants.containsKey(newPosition)){ //no tutaj jest problem z tym jedzniem roslin
                    animal.consumePlant(plants.get(newPosition));
                }

                animal.move(tunnelExit); // Przeniesienie zwierzęcia na drugi koniec tunelu
                animal.nextMove(); //Przesuwam gen na nastepny po przejsciu przez tunel
                break;
            }
        }
    }
}
