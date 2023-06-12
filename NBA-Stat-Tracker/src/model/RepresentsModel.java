package model;

public class RepresentsModel {
    private final String agency;
    private final String agentName;
    private final String playerName;
    private final int jerseyNum;
    private final String position;

    public RepresentsModel(String agency, String agentName, String playerName, int jerseyNum, String position) {
        this.agency = agency;
        this.agentName = agentName;
        this.playerName = playerName;
        this.jerseyNum = jerseyNum;
        this.position = position;
    }

    public String getAgency() {
        return agency;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getJerseyNum() {
        return jerseyNum;
    }

    public String getPosition() {
        return position;
    }
}
