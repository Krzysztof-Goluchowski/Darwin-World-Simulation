package org.model.util;

import javafx.application.Platform;
import org.model.Board;

public class ConsoleMapDisplay {
    private int updatesCounter = 0;

//    private SimulationPresenter simulationPresenter;


//    public ConsoleMapDisplay(SimulationPresenter simulationPresenter) {
//        this.simulationPresenter = simulationPresenter;
//    }

    public synchronized void mapChanged(Board worldMap, String message) {
        System.out.println("Update #" + (++updatesCounter) + ": "+ message);
        System.out.println(worldMap.toString());


//        String update = "Update #" + (++updatesCounter) + ": "+ message;
//        simulationPresenter.changeInfoLabel(update);
//
//        Platform.runLater(() -> {
//            simulationPresenter.drawMap();
//        });
    }
}
