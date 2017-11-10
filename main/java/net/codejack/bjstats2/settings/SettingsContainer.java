package net.codejack.bjstats2.settings;

import java.io.Serializable;

/**
 * Created by Degausser on 6/28/2017.
 */

public class SettingsContainer implements Serializable {

    private PlayerStrategy player;
    private HouseStrategy house;
    private ExecutionSettings execution;

    public PlayerStrategy getPlayer() {
        return player;
    }

    public void setPlayer(PlayerStrategy player) {
        this.player = player;
    }

    public HouseStrategy getHouse() {
        return house;
    }

    public void setHouse(HouseStrategy house) {
        this.house = house;
    }

    public ExecutionSettings getExecution() {
        return execution;
    }

    public void setExecution(ExecutionSettings execution) {
        this.execution = execution;
    }
}
