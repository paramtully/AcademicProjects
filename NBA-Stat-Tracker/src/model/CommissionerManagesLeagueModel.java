package model;

public class CommissionerManagesLeagueModel {
    private final String commissionerName;
    private final String leagueName;

    public CommissionerManagesLeagueModel(String commissionerName, String leagueName) {
        this.commissionerName = commissionerName;
        this.leagueName = leagueName;
    }

    public String getCommissionerName() {
        return commissionerName;
    }

    public String getLeagueName() {
        return leagueName;
    }
}
