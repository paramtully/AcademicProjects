package model;

public class TeamsContainedInLeagueModel {
    private final String name;
    private final String city;
    private final String leagueName;
    private final String championships;
    private final String owner;
    private final int wins;
    private final int losses;

    public TeamsContainedInLeagueModel(String name, String city, String leagueName, String championships, String owner, int wins, int losses) {
        this.name = name;
        this.city = city;
        this.leagueName = leagueName;
        this.championships = championships;
        this.owner = owner;
        this.wins = wins;
        this.losses = losses;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getChampionships() {
        return championships;
    }

    public String getOwner() {
        return owner;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }


}
