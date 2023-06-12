package model;

public class HasModel extends PrintableData {
    private final String arenaName;
    private final String arenaCity;
    private final String teamName;
    private final String teamCity;

    public HasModel(String arenaName, String arenaCity, String teamName, String teamCity) {
        this.arenaName = arenaName;
        this.arenaCity = arenaCity;
        this.teamName = teamName;
        this.teamCity = teamCity;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getArenaCity() {
        return arenaCity;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }

    @Override
    public String getDataString() {
        return arenaName + " " + arenaCity + " " + teamName + " " + teamCity;
    }
}
