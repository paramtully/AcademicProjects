package model;

public class CoachManagesModel extends PrintableData {
    private final String name;
    private final String city;
    private final String teamName;
    private final int yearsExperience;
    private final int coachOfYears;

    public CoachManagesModel(String name, String city, String teamName, int yearsExperience, int coachOfYears) {
        this.name = name;
        this.city = city;
        this.teamName = teamName;
        this.yearsExperience = yearsExperience;
        this.coachOfYears = coachOfYears;
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

    public int getCoachOfYears() {
        return coachOfYears;
    }

    @Override
    public String getDataString() {
        return name + " " + city + " " + teamName + " " + Integer.toString(yearsExperience) + " " + Integer.toString(coachOfYears);
    }
}
