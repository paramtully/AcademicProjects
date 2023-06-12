package ui;

import delegates.LeagueActionsDelegate;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

// certain methods/lines adapted from https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project/blob/master/src/ca/ubc/cs304/ui/LoginWindow.java
public class bballGUI extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JPanel resultsPane;
    private JLabel tableHeader;
    private LeagueActionsDelegate delegate;

    JTextField pname;
    JTextField pnum;
    JTextField ppos;
    JTextField pteamname;
    JTextField pcity;
    JTextField pheight;
    JTextField pweight;
    JTextField pppg;
    JTextField prpg;
    JTextField papg;
    JTextField pawards;
    JTextField pyears;
    JTextField psal;
    JButton insertPlayerButton;
    JLabel deletePlayerLabel;
    JTextField pnameDel;
    JTextField pnumDel;
    JTextField pposDel;
    JButton deletePlayerButton;
    JLabel updatePlayerLabel;
    JTextField pnameUp;
    JTextField pnumUp;
    JTextField pposUp;
    JTextField pteamnameUp;
    JTextField pcityUp;
    JTextField pheightUp;
    JTextField pweightUp;
    JTextField pppgUp;
    JTextField prpgUp;
    JTextField papgUp;
    JTextField pawardsUp;
    JTextField pyearsUp;
    JTextField psalUp;
    JButton updatePlayerButton;
    JButton listPlayersButton;
    JLabel listPlayersLabel;
    JButton mostPointsByPosButton;
    JLabel mostPointsByPos;
    JTextField teamNameMostPoints;
    JTextField teamCityMostPoints;
    JButton aboveAvgHeightButton;
    JTextField teamNameGames;
    JTextField teamCityGames;
    JButton findTeamGamesButton;
    JButton getTeamsLeagueButton;
    JTextField getTeamsLeague;
    JCheckBox getTeamsLeague1;
    JCheckBox getTeamsLeague2;
    JCheckBox getTeamsLeague3;
    JCheckBox getTeamsLeague4;
    JCheckBox getTeamsLeague5;
    JCheckBox getTeamsLeague6;
    JCheckBox getTeamsLeague7;
    JTextField getLeaguesAbove;
    JButton getLeaguesAboveAgeButton;
    JButton getTeamsAllArenasButton;
    String[] allPlayers;
    String[] mostPointsOnTeam;
    String[] aboveAvgHeights;
    String[] teamGames;
    String[] teamsInLeague;
    String[] leaguesAboveAge;
    String[] teamsPlayedAllArenas;
    JTextArea resultsBody;
    JPanel mainPane;

    public bballGUI(LeagueActionsDelegate delegate) {
        this.delegate = delegate;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        resultsPane = new JPanel(new FlowLayout());
        mainPane = new JPanel();
        this.setContentPane(contentPane);
        initGUI();
        this.setVisible(true);
    }

    private void initGUI() {
        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(mainPane);
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

        tableHeader = new JLabel("RESULTS:");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(tableHeader, c);
        mainPane.add(tableHeader);

        resultsBody = new JTextArea("", 15, 30);
        resultsBody.setLineWrap(true);
        resultsBody.setEditable(false);
        resultsBody.setVisible(true);
        JScrollPane scroll = new JScrollPane (resultsBody);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.pack();
        this.pack();
        // c.gridwidth = GridBagConstraints.REMAINDER;
        // gb.setConstraints(scroll, c);
        // updateResultsBodyAllPlayers(delegate.getAllPlayers());
        String ret = Arrays.toString(delegate.getAllPlayers());
        resultsBody.setText(ret);
        mainPane.add(resultsBody);
        // contentPane.add(resultsBody);

        JLabel insertPlayerLabel = new JLabel("Insert a player:");
        setHeaderLabel(insertPlayerLabel, gb, c);

        pname = new JTextField("Full Name");
        setInputTextField(pname, gb, c);

        pnum = new JTextField("Jersey #");
        setInputTextField(pnum, gb, c);

        ppos = new JTextField("Position");
        setInputTextField(ppos, gb, c);

        pteamname = new JTextField("Team Name");
        setInputTextField(pteamname, gb, c);

        pcity = new JTextField("Team City");
        setInputTextField(pcity, gb, c);

        pheight = new JTextField("Height(cm)");
        setInputTextField(pheight, gb, c);

        pweight = new JTextField("Weight(kg)");
        setInputTextField(pweight, gb, c);

        pppg = new JTextField("Points/game");
        setInputTextField(pppg, gb, c);

        prpg = new JTextField("Rebounds/gm");
        setInputTextField(prpg, gb, c);

        papg = new JTextField("Assists/gm");
        setInputTextField(papg, gb, c);

        pawards = new JTextField("Num Awards");
        setInputTextField(pawards, gb, c);

        pyears = new JTextField("Yrs League");
        setInputTextField(pyears, gb, c);

        psal = new JTextField("Salary");
        setInputTextField(psal, gb, c);

        insertPlayerButton = new JButton("Insert Player");
        setButton(insertPlayerButton, gb, c);

        deletePlayerLabel = new JLabel("Delete a Player:");
        setHeaderLabel(deletePlayerLabel, gb, c);

        pnameDel = new JTextField("Full Name");
        setInputTextField(pnameDel, gb, c);

        pnumDel = new JTextField("Jersey #");
        setInputTextField(pnumDel, gb, c);

        pposDel = new JTextField("Position");
        setInputTextField(pposDel, gb, c);

        deletePlayerButton = new JButton("Delete Player");
        setButton(deletePlayerButton, gb, c);

        updatePlayerLabel = new JLabel("Update a player:");
        setHeaderLabel(updatePlayerLabel, gb, c);

        pnameUp = new JTextField("Full Name");
        setInputTextField(pnameUp, gb, c);

        pnumUp = new JTextField("Jersey #");
        setInputTextField(pnumUp, gb, c);

        pposUp = new JTextField("Position");
        setInputTextField(pposUp, gb, c);

        pteamnameUp = new JTextField("Team Name");
        setInputTextField(pteamnameUp, gb, c);

        pcityUp = new JTextField("Team City");
        setInputTextField(pcityUp, gb, c);

        pheightUp = new JTextField("Height(cm)");
        setInputTextField(pheightUp, gb, c);

        pweightUp = new JTextField("Weight(kg)");
        setInputTextField(pweightUp, gb, c);

        pppgUp = new JTextField("Points/game");
        setInputTextField(pppgUp, gb, c);

        prpgUp = new JTextField("Rebounds/gm");
        setInputTextField(prpgUp, gb, c);

        papgUp = new JTextField("Assists/gm");
        setInputTextField(papgUp, gb, c);

        pawardsUp = new JTextField("Num Awards");
        setInputTextField(pawardsUp, gb, c);

        pyearsUp = new JTextField("Yrs in League");
        setInputTextField(pyearsUp, gb, c);

        psalUp = new JTextField("Salary");
        setInputTextField(psalUp, gb, c);

        updatePlayerButton = new JButton("Update Player");
        setButton(updatePlayerButton, gb, c);

        listPlayersLabel = new JLabel("List all players:");
        setHeaderLabel(listPlayersLabel, gb, c);

        listPlayersButton = new JButton("List Players");
        setButton(listPlayersButton, gb, c);

        mostPointsByPos = new JLabel("Most PPG by pos on team:");
        setHeaderLabel(mostPointsByPos, gb, c);

        teamNameMostPoints = new JTextField("Team name");
        setInputTextField(teamNameMostPoints, gb, c);

        teamCityMostPoints = new JTextField("Team city");
        setInputTextField(teamCityMostPoints, gb, c);

        mostPointsByPosButton = new JButton("Submit Search");
        setButton(mostPointsByPosButton, gb, c);

        JLabel aboveAvgHeight = new JLabel("Above avg height for pos:");
        setHeaderLabel(aboveAvgHeight, gb, c);

        aboveAvgHeightButton = new JButton("Submit Search");
        setButton(aboveAvgHeightButton, gb, c);

        JLabel findTeamGames = new JLabel("View games for a team:");
        setHeaderLabel(findTeamGames, gb, c);

        teamNameGames = new JTextField("Team name");
        setInputTextField(teamNameGames, gb, c);

        teamCityGames = new JTextField("Team city");
        setInputTextField(teamCityGames, gb, c);

        findTeamGamesButton = new JButton("Find Games");
        setButton(findTeamGamesButton, gb, c);

        JLabel getTeams = new JLabel("View all teams (select which attributes to view):");
        setHeaderLabel(getTeams, gb, c);

        getTeamsLeague1 = new JCheckBox("Team Name");
        mainPane.add(getTeamsLeague1);
        getTeamsLeague2 = new JCheckBox("Team City");
        mainPane.add(getTeamsLeague2);
        getTeamsLeague3 = new JCheckBox("League Name");
        mainPane.add(getTeamsLeague3);
        getTeamsLeague4 = new JCheckBox("Championships");
        mainPane.add(getTeamsLeague4);
        getTeamsLeague5 = new JCheckBox("Team Owner");
        mainPane.add(getTeamsLeague5);
        getTeamsLeague6 = new JCheckBox("Team Wins");
        mainPane.add(getTeamsLeague6);
        getTeamsLeague7 = new JCheckBox("Team Losses");
        mainPane.add(getTeamsLeague7);

        getTeamsLeagueButton = new JButton("Find teams");
        setButton(getTeamsLeagueButton, gb, c);

        JLabel getLeaguesAboveAge = new JLabel("View leagues above age:");
        setHeaderLabel(getLeaguesAboveAge, gb, c);

        getLeaguesAbove = new JTextField("League age");
        setInputTextField(getLeaguesAbove, gb, c);

        getLeaguesAboveAgeButton = new JButton("Find leagues");
        setButton(getLeaguesAboveAgeButton, gb, c);

        JLabel getTeamsAllArenas = new JLabel("Teams who play all arenas:");
        setHeaderLabel(getTeamsAllArenas, gb, c);

        getTeamsAllArenasButton = new JButton("Find Teams");
        setButton(getTeamsAllArenasButton, gb, c);

        mainPane.add(Box.createVerticalGlue());
        contentPane.add(scrollPane);
        this.pack();
//        Dimension d = this.getToolkit().getScreenSize();
//        Rectangle r = this.getBounds();
//        this.setLocation( (d.width - r.width), (d.height - r.height) );
    }

    private void updateResultsBodyAllPlayers(PlayersPlayForTeamModel[] allPlayers) {
        StringBuilder ret = new StringBuilder();
        for (PlayersPlayForTeamModel curr : allPlayers) {
            ret.append("Name: ").append(curr.getName().trim()).append("| Jersey #: ").append(curr.getJerseyNum())
                    .append("| Position: ").append(curr.getPosition().trim()).append("| Team Name: ").append(curr.getTeamName().trim())
                    .append("| Team City: ").append(curr.getCity().trim()).append("| Height: ")
                    .append(curr.getHeight()).append("| Weight: ").append(curr.getWeightKG())
                    .append("| PPG: ").append(curr.getPpg()).append("| RPG: ").append(curr.getRpg())
                    .append("| APG: ").append(curr.getApg()).append("| Awards: ").append(curr.getAwards())
                    .append("| Years Experience: ").append(curr.getYears()).append("| Salary: ").append(curr.getAnnualSalary())
                    .append(" |------------------|");
        }
        resultsBody.setText(ret.toString());
    }

    private void setInputTextField(JTextField field, GridBagLayout gb, GridBagConstraints c) {
//        c.gridwidth = GridBagConstraints.CENTER;
//        gb.setConstraints(field, c);
        mainPane.add(field);
    }

    private void setHeaderLabel(JLabel label, GridBagLayout gb, GridBagConstraints c) {
//        c.gridwidth = GridBagConstraints.LINE_START;
//        gb.setConstraints(label, c);
        label.setFont(new Font("Courier", Font.BOLD, 12));
        mainPane.add(label);
    }

    private void setButton(JButton button, GridBagLayout gb, GridBagConstraints c) {
//        c.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(button, c);
        button.setBackground(Color.GREEN);
        button.setForeground(Color.BLACK);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(this);
        mainPane.add(button);
        mainPane.add(new JSeparator());
        mainPane.add(new JSeparator());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertPlayerButton) {
            try {
                String name = pname.getText();
                int jersey = Integer.parseInt(pnum.getText());
                String pos = ppos.getText();
                String team = pteamname.getText();
                String city = pcity.getText();
                int height = Integer.parseInt(pheight.getText());
                int weight = Integer.parseInt(pweight.getText());
                float ppg = Float.parseFloat(pppg.getText());
                float rpg = Float.parseFloat(prpg.getText());
                float apg = Float.parseFloat(papg.getText());
                int awards = Integer.parseInt(pawards.getText());
                int years = Integer.parseInt(pyears.getText());
                int salary = Integer.parseInt(psal.getText());
                PlayersPlayForTeamModel player = new PlayersPlayForTeamModel(name, jersey, pos,
                        team, city, height, weight, ppg, rpg, apg, awards, years, salary);
                delegate.insertPlayer(player);
                // updateResultsBodyAllPlayers(delegate.getAllPlayers());
                String ret = Arrays.toString(delegate.getAllPlayers());
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == deletePlayerButton) {
            try {
                String name = pnameDel.getText();
                int jersey = Integer.parseInt(pnumDel.getText());
                String pos = pposDel.getText();
                delegate.deletePlayer(name, jersey, pos);
                String ret = Arrays.toString(delegate.getAllPlayers());
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == updatePlayerButton) {
            try {
                String name = pnameUp.getText();
                int jersey = Integer.parseInt(pnumUp.getText());
                String pos = pposUp.getText();
                String team = pteamnameUp.getText();
                String city = pcityUp.getText();
                int height = Integer.parseInt(pheightUp.getText());
                int weight = Integer.parseInt(pweightUp.getText());
                float ppg = Float.parseFloat(pppgUp.getText());
                float rpg = Float.parseFloat(prpgUp.getText());
                float apg = Float.parseFloat(papgUp.getText());
                int awards = Integer.parseInt(pawardsUp.getText());
                int years = Integer.parseInt(pyearsUp.getText());
                int salary = Integer.parseInt(psalUp.getText());
                PlayersPlayForTeamModel player = new PlayersPlayForTeamModel(name, jersey, pos,
                        team, city, height, weight, ppg, rpg, apg, awards, years, salary);
                delegate.updatePlayer(player);
                // updateResultsBodyAllPlayers(delegate.getAllPlayers());
                String ret = Arrays.toString(delegate.getAllPlayers());
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == listPlayersButton) {
            allPlayers = delegate.getAllPlayers();
            String ret = Arrays.toString(allPlayers);
            resultsBody.setText(ret);
        } else if (e.getSource() == mostPointsByPosButton) {
            try {
                String team = teamNameMostPoints.getText();
                String city = teamCityMostPoints.getText();
                mostPointsOnTeam = delegate.getMostPointsScoredOnATeamByPosition(team, city);
//                StringBuilder ret = new StringBuilder();
//                for (PlayerProjectionModel curr : mostPointsOnTeam) {
//                    ret.append("Name: ").append(curr.getName().trim()).append("| Jersey #: ").append(curr.getJerseyNum())
//                            .append("| Position: ").append(curr.getPosition().trim()).append("| PPG: ").append(curr.getPpg())
//                            .append(" |----------------|");
//                }
                String ret = Arrays.toString(mostPointsOnTeam);
                resultsBody.setText(ret.toString());
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == aboveAvgHeightButton) {
            try {
                aboveAvgHeights = delegate.getPlayersWithAboveAverageHeightForTheirPosition();
//                StringBuilder ret = new StringBuilder();
//                for (PlayerProjectionModel curr : aboveAvgHeights) {
//                    ret.append("Name: ").append(curr.getName().trim()).append("| Jersey #: ").append(curr.getJerseyNum())
//                            .append("| Position: ").append(curr.getPosition().trim()).append("| PPG: ").append(curr.getPpg())
//                            .append(" |-----------------|");
//                }
                String ret = Arrays.toString(aboveAvgHeights);
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if(e.getSource() == findTeamGamesButton) {
            try {
                String team = teamNameGames.getText();
                String city = teamCityGames.getText();
                teamGames = delegate.getTeamGames(team, city);
                String ret;
//                StringBuilder ret = new StringBuilder();
//                for (GamesJoinModel curr : teamGames) {
//                    ret.append("Home team name: ").append(curr.getHomeTeamName().trim()).append("| Home City: ")
//                            .append(curr.getHomeTeamCity().trim())
//                            .append("Away team name: ").append(curr.getAwayTeamName().trim()).append("| Away City: ")
//                            .append(curr.getAwayTeamCity().trim()).append("| Home team won?: ").append(curr.isHomeWon())
//                            .append(" |--------------|");
//                }
                ret = Arrays.toString(teamGames);
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == getTeamsLeagueButton) {
            try {
                teamsInLeague = delegate.getTeamNamesInLeague(getTeamsLeague1.isSelected(),
                        getTeamsLeague2.isSelected(), getTeamsLeague3.isSelected(),
                        getTeamsLeague4.isSelected(), getTeamsLeague5.isSelected(),
                        getTeamsLeague6.isSelected(), getTeamsLeague7.isSelected());
                String ret;
//                StringBuilder ret = new StringBuilder();
//                for (TeamProjectionModel curr : teamsInLeague) {
//                    ret.append("Team name: ").append(curr.getName().trim()).append("| City: ").append(curr.getCity().trim())
//                            .append(" |---------------|");
//                }
                ret = Arrays.toString(teamsInLeague);
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == getLeaguesAboveAgeButton) {
            try {
                int age = Integer.parseInt(getLeaguesAbove.getText());
                leaguesAboveAge = delegate.getLeaguesAtLeastNYearsOld(age);
                String ret;
//                StringBuilder ret = new StringBuilder();
//                for (LeagueAgeModel curr : leaguesAboveAge) {
//                    ret.append("League name: ").append(curr.getLeagueName().trim()).append("| Age: ").append(curr.getLeagueAge())
//                            .append(" |-----------------|");
//                }
                ret = Arrays.toString(leaguesAboveAge);
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        } else if (e.getSource() == getTeamsAllArenasButton) {
            try {
                teamsPlayedAllArenas = delegate.getTeamsThatHavePlayedInAllArenasInLeague();
                String ret;
//                for (TeamProjectionModel curr : teamsPlayedAllArenas) {
//                    ret.append("Team name: ").append(curr.getName().trim()).append("| City: ").append(curr.getCity().trim())
//                            .append(" |---------------|");
//                }
                ret = Arrays.toString(teamsPlayedAllArenas);
                resultsBody.setText(ret);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }
}
