package database;

import model.GamesJoinModel;
import util.PrintablePreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GamesQueryHandler extends DatabaseConnectionHandler {

    // Join
    public GamesJoinModel[] getTeamGames(String teamName, String teamCity) {
        ArrayList<GamesJoinModel> result = new ArrayList<>();
        try {
            String query = "SELECT P1.teamName, P1.teamCity, P2.teamName, P2.teamCity, P1.gameDate, homeWon " +
                    "FROM Play P1, Play P2, Has H, GamesInArenas G" +
                    "WHERE ( (P1.teamName = ? AND P1.teamCity = ?) OR (P2.teamName = ? AND P2.teamCity = ?) ) " +                                                                // get specified team
                    "AND P1.gameDate = P2.gameDate AND P1.arenaName = P2.arenaName AND P1.arenaCity = P2.arenaCity AND P1.teamName <> P2.teamName AND P1.teamCity <> P2.teamCity " +    // natural join 2 teams playing each other
                    "AND P1.arenaName = H.arenaName AND P1.arenaCity = H.arenaCity " +  // P1 is home team
                    "AND P1.arenaName = G.arenaName AND P1.arenaCity = P2.arenaCity AND P1.gameDate = G.gameDate"; // map results of game to get results
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, teamName);
            ps.setString(2, teamCity);
            ps.setString(3, teamName);
            ps.setString(4, teamCity);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                GamesJoinModel gjm = new GamesJoinModel(
                        rs.getString("P1.teamName"),
                        rs.getString("P1.teamCity"),
                        rs.getString("P2.teamName"),
                        rs.getString("P2.teamCity"),
                        rs.getDate("P1.gameDate"),
                        ( rs.getInt("homeWon") == 1 )
                );
                result.add(gjm);
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GamesJoinModel[result.size()]);
    }
}
