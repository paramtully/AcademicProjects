package database;

import model.PlayerProjectionModel;
import model.PlayersPlayForTeamModel;
import util.PrintablePreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerQueryHandler extends DatabaseConnectionHandler {
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
                            "WHERE name = ?, jerseyNum = ?, position = ?";
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
        return result.toArray(new PlayerProjectionModel[result.size()]);
    }
}
