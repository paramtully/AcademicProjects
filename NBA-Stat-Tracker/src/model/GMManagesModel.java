package model;

public class GMManagesModel {
    private final String name;
    private final String city;
    private final String teamName;
    private final int yearsExperience;
    private final int gmOfYears;

    public GMManagesModel(String name, String city, String teamName, int yearsExperience, int gmOfYears) {
        this.name = name;
        this.city = city;
        this.teamName = teamName;
        this.yearsExperience = yearsExperience;
        this.gmOfYears = gmOfYears;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public int getGmOfYears() {
        return gmOfYears;
    }
}
