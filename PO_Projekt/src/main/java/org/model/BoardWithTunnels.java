package org.model;

import java.util.*;

//Klasa tworzy Board, ale z tunelami
public class BoardWithTunnels extends Board {
    Map<Vector2D, Vector2D> tunnelsMaps;

    public BoardWithTunnels(int width, int height, int numberOfTunnels){
        super(width, height);
        this.tunnelsMaps = new HashMap<>();

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

            tunnelsMaps.put(point1, point2);
            tunnelsMaps.put(point2, point1);
        }
    }

    public void setTunnelsMaps(Map<Vector2D, Vector2D> tunnelsMaps) {
        this.tunnelsMaps = tunnelsMaps;
    }

    @Override //Nadpisanie move aby uwzglednial wejscie na tunel
    public void move(Animal animal) {
        // Sprawdzanie, czy nowa pozycja zwierzęcia jest wejściem do tunelu
        Vector2D animalPosition = animal.position();
        if (tunnelsMaps.containsKey(animalPosition)){
            Vector2D tunnelExit = tunnelsMaps.get(animalPosition);
            animal.move(tunnelExit);
        }
        super.move(animal); // Wywołanie oryginalnej metody move
    }
  
    public Map<Vector2D, Vector2D> getTunnelsMaps() {
        return tunnelsMaps;
    }
}
