package model;

public class PlayersPlayForTeamModel extends PrintableData {
    private final String name;
    private final int jerseyNum;
    private final String position;
    private final String teamName;
    private final String city;
    private final int height;
    private final int weightKG;
    private final float ppg;
    private final float rpg;
    private final float apg;
    private final int awards;
    private final int years;
    private final int annualSalary;

    public PlayersPlayForTeamModel(String name, int jerseyNum, String position, String teamName, String city, int height, int weightKG, float ppg, float rpg, float apg, int awards, int years, int annualSalary) {
        this.name = name;
        this.jerseyNum = jerseyNum;
        this.position = position;
        this.teamName = teamName;
        this.city = city;
        this.height = height;
        this.weightKG = weightKG;
        this.ppg = ppg;
        this.rpg = rpg;
        this.apg = apg;
        this.awards = awards;
        this.years = years;
        this.annualSalary = annualSalary;
    }

    public String getName() {
        return name;
    }

    public int getJerseyNum() {
        return jerseyNum;
    }

    public String getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCity() {
        return city;
    }

    public int getHeight() {
        return height;
    }

    public int getWeightKG() {
        return weightKG;
    }

    public float getPpg() {
        return ppg;
    }

    public float getRpg() {
        return rpg;
    }

    public float getApg() {
        return apg;
    }

    public int getAwards() {
        return awards;
    }

    public int getYears() {
        return years;
    }

    public int getAnnualSalary() {
        return annualSalary;
    }


    @Override
    public String getDataString() {
        return  name + " " + Integer.toString(jerseyNum) + " " + position + " " + teamName + " " + city + " " + Integer.toString(height) +
                " " + Integer.toString(weightKG) + " " + Float.toString(ppg)+ " " + Float.toString(rpg)+ " " + Float.toString(apg) + " " + Integer.toString(awards) + " " + Integer.toString(years) + " " + Integer.toString(annualSalary);
    }
}
