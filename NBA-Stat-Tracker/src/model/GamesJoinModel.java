package model;

import java.util.Date;

public class GamesJoinModel extends PrintableData {
    private final String homeTeamName;
    private final String homeTeamCity;
    private final String awayTeamName;
    private final String awayTeamCity;
    private final Date gameDate;
    private final boolean homeWon;

    public GamesJoinModel(String homeTeamName, String homeTeamCity, String awayTeamName, String awayTeamCity, Date gameDate, boolean homeWon) {
        this.homeTeamName = homeTeamName;
        this.homeTeamCity = homeTeamCity;
        this.awayTeamName = awayTeamName;
        this.awayTeamCity = awayTeamCity;
        this.gameDate = gameDate;
        this.homeWon = homeWon;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getHomeTeamCity() {
        return homeTeamCity;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getAwayTeamCity() {
        return awayTeamCity;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public boolean isHomeWon() {
        return homeWon;
    }

    @Override
    public String getDataString() {
        return homeTeamName + " " + homeTeamCity + " " + awayTeamName + " " + awayTeamCity + " " + gameDate.toString() +  " " + Boolean.toString(homeWon);
    }
}
