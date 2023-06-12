package database;


import model.*;
import util.PrintablePreparedStatement;

import java.sql.*;
import java.util.ArrayList;


public class DatabaseConnectionHandlerStringResults {
    // Use this version of the ORACLE_URL if you are running the code off of the server
    //	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    protected static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    protected static final String EXCEPTION_TAG = "[EXCEPTION]";
    protected static final String WARNING_TAG = "[WARNING]";

    protected Connection connection = null;

    private final ArrayList<String> gamesJoinAttrs = new ArrayList<String>() {{
        add("HOMETEAM");
        add("HOMECITY");
        add("AWAYTEAM");
        add("AWAYCITY");
        add("GAMEDATE");
        add("HOMEWON");
    }};

    public DatabaseConnectionHandlerStringResults() {
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

    public void close() {
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

    private void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
    }

    private void printResults(String[] result) {
        for (String r : result) System.out.println(r);
    }

    private String[] formatResultSetAsStrings(ResultSet rs) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            String item = "";
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) item = item.concat(",  ");
                String columnValue = rs.getString(i);
                item = item.concat(rsmd.getColumnName(i) + ": " + columnValue);
            }
            result.add(item);
        }
        return result.toArray(new String[result.size()]);
    }

    private String[] formatGamesJoinResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        int columnsNumber = gamesJoinAttrs.size();
        while (rs.next()) {
            String item = "";
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) item = item.concat(",  ");
                String columnValue = rs.getString(i);
                item = item.concat(gamesJoinAttrs.get(i - 1) + ": " + columnValue);
            }
            result.add(item);
        }
        return result.toArray(new String[result.size()]);
    }

    protected boolean handleExistingData(PrintablePreparedStatement ps, String warning) throws SQLException {
        boolean status = true;
        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            System.out.println(warning);
            status = false;
        }

        connection.commit();

        ps.close();
        return status;
    }

    // Insert
    public boolean insertPlayer(PlayersPlayForTeamModel player) {
        try {
            String query = "INSERT INTO PlayersPlayForTeam VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            return false;
        }

        return true;
    }

    // Delete
    public boolean deletePlayer(String name, int jerseyNum, String position) {
        try {
            String query = "DELETE FROM PlayersPlayForTeam WHERE name = ? AND jerseyNum = ? AND position = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ps.setInt(2, jerseyNum);
            ps.setString(3, position);

            String warning = WARNING_TAG + " Player " + name + " " + Integer.toString(jerseyNum) + " " + position +  " does not exist!";
            if (!handleExistingData(ps, warning)) return false;
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
        return true;
    }

    // Update
    public boolean updatePlayer(PlayersPlayForTeamModel player) {
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
            if (!handleExistingData(ps, warning)) return false;
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
        return true;
    }

    public String[] getAllPlayers() {
        String[] result = new String[0];
        try {
            String query = "SELECT * FROM PlayersPlayForTeam";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        printResults(result);
        return result;
    }

    // Selection
    public String[] getSelectedPlayers(ArrayList<String> predicates) {
        String[] result = new String[0];
        try {
            String query = "SELECT * FROM PlayersPlayForTeam WHERE";
            for (int i = 0; i < predicates.size(); i++) {
                if (i > 0) query = query.concat(" AND " + predicates.get(i));
                else query = query.concat(" " + predicates.get(i));
            }

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        printResults(result);
        return result;
    }

    public String getGoat() {
        // stub
        return null;
    }


    // Aggregation with GroupBy
    public String[] getMostPointsScoredOnATeamByPosition(String teamName, String teamCity) {
        String[] result = new String[0];
        try {
            String query = "SELECT position, MAX(ppg) AS mppg FROM PlayersPlayForTeam WHERE teamName = ? AND city = ? GROUP BY position";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, teamName);
            ps.setString(2, teamCity);

            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        printResults(result);
        return result;
    }

    // Nested Aggregation with GroupBy
    public String[] getPlayersWithAboveAverageHeightForTheirPosition() {
        String[] result = new String[0];
        try {
            String query0 = "SELECT position FROM PlayersPlayForTeam P";
            String query = "SELECT position, name, jerseyNum, ppg FROM PlayersPlayForTeam P WHERE P.height > " +
                    "(SELECT AVG(P2.height) FROM PlayersPlayForTeam P2 WHERE P2.position = P.position) " +
                    "GROUP BY position, name, jerseyNum, ppg";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        printResults(result);
        return result;
    }

    // Join
    public String[] getTeamGames(String teamName, String teamCity) {
        String[] result = new String[0];
        try {
            String query = "SELECT P1.teamName, P1.teamCity, P2.teamName, P2.teamCity, P1.gameDate, homeWon " +
                    "FROM Play P1, Play P2, GamesInArenas G " +
                    "WHERE ( (P1.teamName = ? AND P1.teamCity = ?) OR (P2.teamName = ? AND P2.teamCity = ?) ) " +  // get specified team
                    "AND P1.gameDate = P2.gameDate AND P1.arenaName = P2.arenaName AND P1.arenaCity = P2.arenaCity " + // natural join 2 teams playing each other
                    "AND ( ( (P1.teamName <> P2.teamName) AND (P1.teamCity <> P2.teamCity) ) OR ( (P1.teamName = P2.teamName) AND  (P1.teamCity <> P2.teamCity) ) OR ( (P1.teamName <> P2.teamName) AND  (P1.teamCity = P2.teamCity) ) )" +  // check both teams are different
                    "AND P1.arenaName = G.arenaName AND P1.arenaCity = G.city AND P1.gameDate = G.gameDate " +  // natural join game
                    "AND EXISTS(SELECT * FROM HAS H WHERE P1.teamName = H.teamName AND P1.teamCity = H.teamCity AND P1.arenaName = H.arenaName AND P1.arenaCity = H.arenaCity)"; // assures p1 is home team

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, teamName);
            ps.setString(2, teamCity);
            ps.setString(3, teamName);
            ps.setString(4, teamCity);

            ResultSet rs = ps.executeQuery();
            result = formatGamesJoinResultSet(rs);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        printResults(result);
        return result;
    }

    // Projection
    public String[] projectTeams(boolean includeName, boolean includeCity, boolean includeLeagueName, boolean includeChampionships, boolean includeOwner, boolean includeWins, boolean includeLosses) {
        String[] result = new String[0];
        try {
            String query = "SELECT " +
                    (includeName ?          "name, "          : "") +
                    (includeCity ?          "city, "          : "") +
                    (includeLeagueName ?    "leagueName, "    : "") +
                    (includeChampionships ? "championships, " : "") +
                    (includeOwner ?         "owner, "         : "") +
                    (includeWins ?          "wins, "          : "") +
                    (includeLosses ?        "losses, "        : "");

            int delimIndex = query.lastIndexOf(',');
            if (delimIndex != -1) {
                query = query.substring(0, delimIndex) + query.substring(delimIndex + 1);
            }
            query = query.concat("FROM TeamsContainedInLeague");
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        printResults(result);
        return result;
    }

    // Aggregation with Having
    public String[] getLeaguesAtLeastNYearsOld(int numYears) {
        String[] result = new String[0];
        try {
            String query = "SELECT leagueName, SUM(championships) AS numYears FROM TeamsContainedInLeague GROUP BY leagueName HAVING SUM(championships) >= ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, numYears);

            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);

            rs.close();
            ps.close();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        printResults(result);
        return result;
    }

    // Division
    public String[] getTeamsThatHavePlayedInAllArenasInLeague() {
        String[] result = new String[0];
        try {
            String query = "SELECT name, city FROM TeamsContainedInLeague T WHERE NOT EXISTS" +
                    "( (SELECT H.arenaName, H.arenaCity FROM Has H, TeamsContainedInLeague T2 WHERE H.teamName = T2.name AND H.teamCity = T2.city AND T2.leagueName = T.leagueName) " +
                    "MINUS " +
                    "(SELECT P.arenaName, P.arenaCity FROM Play P WHERE P.teamName = T.name AND P.teamCity = T.city) )";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            result = formatResultSetAsStrings(rs);
            rs.close();
            ps.close();
        } catch(SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        printResults(result);
        return result;
    }
}
