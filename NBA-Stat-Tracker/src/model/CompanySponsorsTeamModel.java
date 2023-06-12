package model;

public class CompanySponsorsTeamModel {
    private final String companyName;
    private final String teamName;
    private final String city;
    private final int amount;

    public CompanySponsorsTeamModel(String companyName, String teamName, String city, int amount) {
        this.companyName = companyName;
        this.teamName = teamName;
        this.city = city;
        this.amount = amount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCity() {
        return city;
    }

    public int getAmount() {
        return amount;
    }
}
