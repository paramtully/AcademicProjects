package database;

import model.LeagueAgeModel;
import model.TeamProjectionModel;
import util.PrintablePreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamQueryHandler extends DatabaseConnectionHandler {
    // projections
    public TeamProjectionModel[] getTeamNamesInLeague(String leagueName) {
        ArrayList<TeamProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT name, city FROM TeamsContainedInLeague WHERE leagueName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, leagueName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeamProjectionModel tpm = new TeamProjectionModel(
                        rs.getString("name"),
                        rs.getString("city")
                );
                result.add(tpm);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new TeamProjectionModel[result.size()]);
    }

    // Aggregation with Having
    public LeagueAgeModel[] getLeaguesAtLeastNYearsOld(int numYears) {
        ArrayList<LeagueAgeModel> result = new ArrayList<>();
        try {
            String query = "SELECT leagueName, SUM(championships) AS numYears FROM TeamsContainedInLeague GROUP BY leagueName HAVING SUM(championships) >= ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, numYears);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeagueAgeModel lam = new LeagueAgeModel(
                        rs.getString("leagueName"),
                        rs.getInt("numYears")
                );
                result.add(lam);
            }
            rs.close();
            ps.close();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new LeagueAgeModel[result.size()]);
    }

    // Division
    public TeamProjectionModel[] getTeamsThatHavePlayedInAllArenasInLeague() {
        ArrayList<TeamProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT T.name, T.city FROM TeamsContainedInLeague T WHERE NOT EXISTS" +
                    "( (SELECT H.arenaName, H.arenaCity FROM Has H, TeamsContainedInLeague T2 WHERE H.teamName = T2.name AND H.teamCity = T2.city AND T2.leagueName = T.leagueName) " +
                    "MINUS " +
                    "(SELECT P.arenaName, P.arenaCity FROM Play P WHERE P.teamName = T.name AND P.teamCity = T.city) )";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeamProjectionModel tpm = new TeamProjectionModel(
                        rs.getString("name"),
                        rs.getString("city")
                );
                result.add(tpm);
            }
            rs.close();
            ps.close();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new TeamProjectionModel[result.size()]);
    }
}
