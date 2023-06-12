package controller;

import database.DatabaseConnectionHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

import database.DatabaseConnectionHandlerStringResults;
import delegates.LeagueActionsDelegate;
import delegates.LoginWindowDelegate;
import model.*;
import ui.LoginWindow;
import ui.bballGUI;

// adapted from Bank sample code (https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project)
public class Basketball implements LoginWindowDelegate, LeagueActionsDelegate {

    private DatabaseConnectionHandlerStringResults db = null;
    private LoginWindow loginWindow = null;

    public Basketball() {
        db = new DatabaseConnectionHandlerStringResults();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    public void login(String username, String password) {
        boolean didConnect = db.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();
            new bballGUI(this);

//            TerminalTransactions transaction = new TerminalTransactions();
//            transaction.setupDatabase(this);
//            transaction.showMainMenu(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public void insertPlayer(PlayersPlayForTeamModel player) {
        db.insertPlayer(player);
    }

    public void deletePlayer(String name, int jerseyNum, String position) {
        db.deletePlayer(name, jerseyNum, position);
    }

    public void updatePlayer(PlayersPlayForTeamModel player) {
        db.updatePlayer(player);
    }

    public String[] getAllPlayers() {
        return db.getAllPlayers();
    }

    public String getGoat() {
        return db.getGoat();
    }

    public String[] getMostPointsScoredOnATeamByPosition(String teamName, String teamCity) {
        return db.getMostPointsScoredOnATeamByPosition(teamName, teamCity);
    }

    public String[] getPlayersWithAboveAverageHeightForTheirPosition() {
        return db.getPlayersWithAboveAverageHeightForTheirPosition();
    }

    public String[] getTeamGames(String teamName, String teamCity) {
        return db.getTeamGames(teamName, teamCity);
    }

    public String[] getTeamNamesInLeague(boolean includeName, boolean includeCity, boolean includeLeagueName, boolean includeChampionships, boolean includeOwner, boolean includeWins, boolean includeLosses) {
        return db.projectTeams(includeName, includeCity, includeLeagueName, includeChampionships, includeOwner, includeWins, includeLosses);
    }

    public String[] getLeaguesAtLeastNYearsOld(int numYears) {
        return db.getLeaguesAtLeastNYearsOld(numYears);
    }

    public String[] getTeamsThatHavePlayedInAllArenasInLeague() {
        return db.getTeamsThatHavePlayedInAllArenasInLeague();
    }

    public static void main(String[] args) {
        Basketball bball = new Basketball();
        bball.start();
    }
}
