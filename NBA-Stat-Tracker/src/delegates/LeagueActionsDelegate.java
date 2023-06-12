package delegates;

import model.*;
// adapted from Bank sample code (https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project)

public interface LeagueActionsDelegate {

    public void insertPlayer(PlayersPlayForTeamModel player);

    public void deletePlayer(String name, int jerseyNum, String position);

    public void updatePlayer(PlayersPlayForTeamModel player);

    public String[] getAllPlayers();

    public String getGoat();

    public String[] getMostPointsScoredOnATeamByPosition(String teamName, String teamCity);
    public String[] getPlayersWithAboveAverageHeightForTheirPosition();
    public String[] getTeamGames(String teamName, String teamCity);
    public String[] getTeamNamesInLeague(boolean includeName, boolean includeCity, boolean includeLeagueName, boolean includeChampionships, boolean includeOwner, boolean includeWins, boolean includeLosses);
    public String[] getLeaguesAtLeastNYearsOld(int numYears);
    public String[] getTeamsThatHavePlayedInAllArenasInLeague();
}