package org.model.util;

import javafx.application.Platform;
import org.model.Board;
import org.model.presenter.SimulationPresenter;

public class ConsoleMapDisplay {
    private int updatesCounter = 0;

    private SimulationPresenter simulationPresenter;


    public ConsoleMapDisplay(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
    }

    public synchronized void mapChanged(Board worldMap, String message) {
        System.out.println("Update #" + (++updatesCounter) + ": "+ message);
        System.out.println(worldMap.toString());

        Platform.runLater(() -> {
            simulationPresenter.drawMap();
        });
    }
}
