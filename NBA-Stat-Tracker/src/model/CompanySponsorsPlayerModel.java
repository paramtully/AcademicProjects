package model;

public class CompanySponsorsPlayerModel {
    private final String companyName;
    private final String playerName;
    private final int jerseyNumber;
    private final String position;
    private final int amount;

    public CompanySponsorsPlayerModel(String companyName, String playerName, int jerseyNumber, String position, int amount) {
        this.companyName = companyName;
        this.playerName = playerName;
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.amount = amount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public String getPosition() {
        return position;
    }

    public int getAmount() {
        return amount;
    }
}
