package test;

import model.PlayersPlayForTeamModel;

import java.util.ArrayList;

public class ExampleData {
    public static PlayersPlayForTeamModel newLakersPlayer = new PlayersPlayForTeamModel("Anthony Davis", 12, "C", "Lakers", "Los Angeles", 240, 220, (float)28.6, (float)8.2, (float)8.4, 20, 2, 50540000);
    public static PlayersPlayForTeamModel updatedLakersPlayer = new PlayersPlayForTeamModel("Anthony Davis", 12, "C", "Lakers", "Los Angeles", 240, 280, (float)28.6, (float)8.2, (float)8.4, 20, 2, 1);

    public static PlayersPlayForTeamModel shortLakersCenter = new PlayersPlayForTeamModel("Anthony shortDavis", 12, "C", "Lakers", "Los Angeles", 100, 280, (float)28.6, (float)8.2, (float)8.4, 20, 2, 1);
    public static PlayersPlayForTeamModel shortishLakersCenter = new PlayersPlayForTeamModel("Anthony shortishDavis", 12, "C", "Lakers", "Los Angeles", 120, 280, (float)28.6, (float)8.2, (float)8.4, 20, 2, 1);
    public static PlayersPlayForTeamModel tallishLakersCenter = new PlayersPlayForTeamModel("Anthony tallishDavis", 12, "C", "Lakers", "Los Angeles", 200, 280, (float)28.6, (float)8.2, (float)8.4, 20, 2, 1);

    public static ArrayList<String> selectLA = new ArrayList<String>() {{
        add("city = 'Los Angeles'");
    }};
    public static ArrayList<String> selectHighSalaryWithJerseyNumGT6 = new ArrayList<String>() {{
        add("annualSalary > '12400000'");
        add("jerseyNum > '6'");
    }};


}
