package model;

import java.sql.Date;

public class PlayModel {
    private final String arenaName;
    private final String arenaCity;
    private final Date gameDate;
    private final String teamName;
    private final String teamCity;

    public PlayModel(String arenaName, String arenaCity, Date gameDate, String teamName, String teamCity) {
        this.arenaName = arenaName;
        this.arenaCity = arenaCity;
        this.gameDate = gameDate;
        this.teamName = teamName;
        this.teamCity = teamCity;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getArenaCity() {
        return arenaCity;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }
}
