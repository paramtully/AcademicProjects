package model;

public class TeamChampionshipsProjectionModel {
    private final String teamName;
    private final String teamCity;
    private final String leagueName;
    private final int championships;

    public TeamChampionshipsProjectionModel(String teamName, String teamCity, String leagueName, int championships) {
        this.teamName = teamName;
        this.teamCity = teamCity;
        this.leagueName = leagueName;
        this.championships = championships;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public int getChampionships() {
        return championships;
    }
}
