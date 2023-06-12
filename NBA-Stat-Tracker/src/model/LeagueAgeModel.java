package model;

public class LeagueAgeModel extends PrintableData {
    private final String leagueName;
    private final int age;

    public LeagueAgeModel(String leagueName, int age) {
        this.leagueName = leagueName;
        this.age = age;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public int getLeagueAge() {
        return age;
    }

    @Override
    public String getDataString() {
        return leagueName + " " + Integer.toString(age);
    }
}
