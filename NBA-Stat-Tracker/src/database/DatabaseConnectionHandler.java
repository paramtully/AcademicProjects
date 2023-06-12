package database;


import model.*;
import util.PrintablePreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
    //	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    protected static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    protected static final String EXCEPTION_TAG = "[EXCEPTION]";
    protected static final String WARNING_TAG = "[WARNING]";

    protected Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    protected void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    protected void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    protected void handleExistingData(PrintablePreparedStatement ps, String warning) throws SQLException {
        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            System.out.println(warning);
        }

        connection.commit();

        ps.close();
    }

    public void insertPlayer(PlayersPlayForTeamModel player) {
        try {
            String query = "INSERT INTO PlayersPlayForTeam VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, player.getName());
            ps.setInt(2, player.getJerseyNum());
            ps.setString(3, player.getPosition());
            ps.setString(4, player.getTeamName());
            ps.setString(5, player.getCity());
            ps.setInt(6, player.getHeight());
            ps.setInt(7, player.getWeightKG());
            ps.setFloat(8, player.getPpg());
            ps.setFloat(9, player.getRpg());
            ps.setFloat(10, player.getApg());
            ps.setInt(11, player.getAwards());
            ps.setInt(12, player.getYears());
            ps.setInt(13, player.getAnnualSalary());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deletePlayer(String name, int jerseyNum, String position) {
        try {
            String query = "DELETE FROM PlayersPlayForTeam WHERE name = ? AND jerseyNum = ? AND position = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ps.setInt(2, jerseyNum);
            ps.setString(3, position);

            String warning = WARNING_TAG + " Player " + name + " " + Integer.toString(jerseyNum) + " " + position +  " does not exist!";
            handleExistingData(ps, warning);
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updatePlayer(PlayersPlayForTeamModel player) {
        try {
            String query =  "UPDATE PlayersPlayForTeam " +
                    "SET name = ?, jerseyNum = ?, position = ?, teamName = ?, " +
                    "city = ?, height = ?, weightKG = ?, ppg = ?, rpg = ?, apg = ?, " +
                    "awards = ?, years = ?, annualSalary = ? " +
                    "WHERE name = ? AND jerseyNum = ? AND position = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, player.getName());
            ps.setInt(2, player.getJerseyNum());
            ps.setString(3, player.getPosition());
            ps.setString(4, player.getTeamName());
            ps.setString(5, player.getCity());
            ps.setInt(6, player.getHeight());
            ps.setInt(7, player.getWeightKG());
            ps.setFloat(8, player.getPpg());
            ps.setFloat(9, player.getRpg());
            ps.setFloat(10, player.getApg());
            ps.setInt(11, player.getAwards());
            ps.setInt(12, player.getYears());
            ps.setInt(13, player.getAnnualSalary());
            ps.setString(14, player.getName());
            ps.setInt(15, player.getJerseyNum());
            ps.setString(16, player.getPosition());

            String warning = WARNING_TAG + " Player " + player.getName() + " " + Integer.toString(player.getJerseyNum()) + " " + player.getPosition() +  " does not exist!";
            handleExistingData(ps, warning);
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Selection
    public PlayersPlayForTeamModel[] getAllPlayers() {
        ArrayList<PlayersPlayForTeamModel> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM PlayersPlayForTeam";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                PlayersPlayForTeamModel model = new PlayersPlayForTeamModel(
                        rs.getString("name"),
                        rs.getInt("jerseyNum"),
                        rs.getString("position"),
                        rs.getString("teamName"),
                        rs.getString("city"),
                        rs.getInt("height"),
                        rs.getInt("weightKG"),
                        rs.getFloat("ppg"),
                        rs.getFloat("rpg"),
                        rs.getFloat("apg"),
                        rs.getInt("awards"),
                        rs.getInt("years"),
                        rs.getInt("annualSalary"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        for (PrintableData p : result) System.out.println(p.getDataString());

        return result.toArray(new PlayersPlayForTeamModel[result.size()]);
    }

    // this one's for fun
    public String getGoat() {
        String goat = "";
        try {
            // stub
            throw new SQLException();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return goat;
    }

    // Aggregation with GroupBy
    public PlayerProjectionModel[] getMostPointsScoredOnATeamByPosition(String teamName, String teamCity) {
        ArrayList<PlayerProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT position, MAX(ppg) FROM PlayersPlayForTeam WHERE teamName = ? AND city = ? GROUP BY position";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, teamName);
            ps.setString(2, teamCity);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                PlayerProjectionModel ppm = new PlayerProjectionModel(
                        null,
                        null,
                        rs.getString("position"),
                        Float.parseFloat(rs.getString("ppg"))
                );
                result.add(ppm);
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        for (PrintableData p : result) System.out.println(p.getDataString());
        return result.toArray(new PlayerProjectionModel[result.size()]);
    }

    // Nested Aggregation with GroupBy
    public PlayerProjectionModel[] getPlayersWithAboveAverageHeightForTheirPosition() {
        ArrayList<PlayerProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT P.position, P.name, P.jerseyNum, P.ppg FROM PlayersPlayForTeam P WHERE P.height > " +
                    "(SELECT AVG(P2.height) FROM PlayersPlayForTeam P2 WHERE P2.position = P.position) " +
                    "GROUP BY P.position, P.name, P.jerseyNum, P.ppg";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PlayerProjectionModel ppm = new PlayerProjectionModel(
                        rs.getString("P.name"),
                        rs.getInt("P.jerseyNum"),
                        rs.getString("P.position"),
                        Float.parseFloat(rs.getString("P.ppg"))
                );
                result.add(ppm);
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        for (PrintableData p : result) System.out.println(p.getDataString());
        return result.toArray(new PlayerProjectionModel[result.size()]);
    }

    // Join
    public GamesJoinModel[] getTeamGames(String teamName, String teamCity) {
        ArrayList<GamesJoinModel> result = new ArrayList<>();
        try {
            String query = "SELECT P1.teamName, P1.teamCity, P2.teamName, P2.teamCity, P1.gameDate, G.homeWon " +
                    "FROM Play P1, Play P2, Has H, GamesInArenas G " +
                    "WHERE ( (P1.teamName = ? AND P1.teamCity = ?) OR (P1.teamName = ? AND P1.teamCity = ?) ) " +                                                                // get specified team
                    "AND P1.gameDate = P2.gameDate AND P1.arenaName = P2.arenaName AND P1.arenaCity = P2.arenaCity AND P1.teamName <> P2.teamName AND P1.teamCity <> P2.teamCity " +    // natural join 2 teams playing each other
                    "AND P1.teamName = H.teamName AND P1.teamCity = H.teamCity " +  // P1 is home team
                    "AND P1.arenaName = G.arenaName AND P1.arenaCity = G.city AND P1.gameDate = G.gameDate"; // map results of game to get results

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

        for (PrintableData p : result) System.out.println(p.getDataString());
        return result.toArray(new GamesJoinModel[result.size()]);
    }

    public TeamProjectionModel[] getTeamNamesInLeague(String leagueName) {
        ArrayList<TeamProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT T.name, T.city FROM TeamsContainedInLeague T WHERE T.leagueName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, leagueName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeamProjectionModel tpm = new TeamProjectionModel(
                        rs.getString("T.name"),
                        rs.getString("T.city")
                );
                result.add(tpm);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        for (PrintableData p : result) System.out.println(p.getDataString());
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

        for (PrintableData p : result) System.out.println(p.getDataString());
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
        for (PrintableData p : result) System.out.println(p.getDataString());
        return result.toArray(new TeamProjectionModel[result.size()]);
    }
}
