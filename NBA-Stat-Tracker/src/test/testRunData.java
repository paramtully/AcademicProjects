package test;

import database.DatabaseConnectionHandlerStringResults;

public class testRunData {

    private String user = "REDACTED";
    private String pass = "REDACTED";

    public static void main(String[] args) {
        testRunData t = new testRunData();
        t.runDB();
    }

    public void runDB() {
        DatabaseConnectionHandlerStringResults db = new DatabaseConnectionHandlerStringResults();
        boolean Status = db.login(this.user, this.pass);
        System.out.println(Status);

        // TEST: insert player
//        System.out.println("Insert Status: " + db.insertPlayer(ExampleData.newLakersPlayer));
//        System.out.println("Insert Status: " + db.insertPlayer(ExampleData.shortLakersCenter));
//        System.out.println("Insert Status: " + db.insertPlayer(ExampleData.shortishLakersCenter));
//        System.out.println("Insert Status: " + db.insertPlayer(ExampleData.tallishLakersCenter));

        // TEST: update Player
//        System.out.println("Update Status: " + db.updatePlayer(ExampleData.updatedLakersPlayer));

        // TEST: delete Player
//        System.out.println("Delete Status: " + db.deletePlayer("Anthony Davis", 12, "C"));

        // TEST: get all players
        System.out.println("Players in Players Table: ");
        db.getAllPlayers();
        System.out.println();

        // TEST: get selected players
        System.out.println("Players selected in LA: ");
        db.getSelectedPlayers(ExampleData.selectLA);
        System.out.println();

        // TEST: get selected players (NOTE: turn everything into a string ie wrap with '')
        System.out.println("Players selected with high jerseyNum and Salary: ");
        db.getSelectedPlayers(ExampleData.selectHighSalaryWithJerseyNumGT6);
        System.out.println();

        // TEST: Aggregation w Group By
        System.out.println("Best Players Grouped By Position on Lakers: ");
        db.getMostPointsScoredOnATeamByPosition("Lakers", "Los Angeles");
        System.out.println();

        // TEST: Nested Aggregation w Group By
        System.out.println("Above Average Height By Position:" );
        db.getPlayersWithAboveAverageHeightForTheirPosition();
        System.out.println();

        // TEST: Projection
        System.out.println("Projecting Team Names and City: ");
        db.projectTeams(true, true, false, false, false, false, false);
        System.out.println();

        System.out.println("Projecting Team Names: ");
        db.projectTeams(true, false, false, false, false, false, false);
        System.out.println();

        // TEST: Join
        System.out.println("Projecting Team Names and City: ");
        db.getTeamGames("Lakers", "Los Angeles");
        System.out.println();

        // TEST: Group By w Having
        System.out.println("Get Leagues at Least 10 Years Old: ");
        db.getLeaguesAtLeastNYearsOld(10);
        System.out.println();

        // TEST: Division
        System.out.println("Get Teams That Have played in All Arenas in Their League: ");
        db.getTeamsThatHavePlayedInAllArenasInLeague();
        System.out.println();


        db.close();
    }
}
