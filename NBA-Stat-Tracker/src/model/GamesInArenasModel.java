package model;


import java.sql.Date;

public class GamesInArenasModel extends PrintableData {
    private final Date gameDate;
    private final boolean homeWon;
    private final String arenaName;
    private final String city;

    public GamesInArenasModel(Date gameDate, boolean homeWon, String arenaName, String city) {
        this.gameDate = gameDate;
        this.homeWon = homeWon;
        this.arenaName = arenaName;
        this.city = city;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public boolean isHomeWon() {
        return homeWon;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String getDataString() {
        return gameDate + " " + Boolean.toString(homeWon) + " " + arenaName + " " + city;
    }
}
