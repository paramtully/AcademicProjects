package model;

public class CommissionerManagesTeamModel extends PrintableData {
    private final String name;
    private final String teamName;
    private final String city;
    private final int yearsExperience;

    public CommissionerManagesTeamModel(String name, String teamName, String city, int yearsExperience) {
        this.name = name;
        this.teamName = teamName;
        this.city = city;
        this.yearsExperience = yearsExperience;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCity() {
        return city;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    @Override
    public String getDataString() {
        return name + " " + teamName + " " + city + " " + Integer.toString(yearsExperience);
    }
}
