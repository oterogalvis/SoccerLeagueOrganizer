import model.League;
import model.Player;
import model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by jorgeotero on 4/7/17.
 */
public class Prompter {
    private BufferedReader bufferedReader;
    private League league;
    private boolean exit;
    private static Prompter instance;

    private Prompter() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        league = League.getLeague();
        exit = false;
    }

    private String getInput() {
        String input = "";
        try {
            input = bufferedReader.readLine();
        } catch (IOException ioe) {
            System.out.println("Problem with input, try again.%s");
            ioe.printStackTrace();
        }
        return input;
    }

    private int getInputInt() {
        try {
            int input = Integer.parseInt(getInput());
            return input;
        } catch (NumberFormatException nfe) {
            System.out.println("Input must be an integer, please try again.");
        }
        return getInputInt();
    }

    private int promptForIndex(List<String> options, String optionsTitle) throws IllegalArgumentException {
        System.out.println(optionsTitle);
        int index = 1;
        for (String option : options) {
            System.out.printf("\t%d) %s\n", index, option);
            index++;
        }
        int input = getInputInt();
        if (input > options.size() || input < 1) {
            throw new IllegalArgumentException("Unknown option.");
        }
        return input -1 ;
    }

    public void menu() {
        try {
            List<String> menuOptions = Arrays.asList(
                    "Create new team",
                    "Add player to a Team",
                    "Remove player from a Team",
                    "League Balance Report",
                    "Get roster from a team",
                    "Exit");
            while (!exit) {
                String option = menuOptions.get(promptForIndex(menuOptions, "Choose your option: "));
                switch (option) {
                    case "Create new team":
                        createdNewTeamIfPossible();
                        break;
                    case "Add player to a Team":
                        addPlayerMenu();
                        break;
                    case "Remove player from a Team":
                        removePlayerMenu();
                        break;
                    case "League Balance Report":
                        leagueBalanceReport();
                        break;
                    case "Get roster from a team":
                        rosterMenu();
                        break;
                    case "Exit":
                        System.out.printf("Exiting...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Error, check menu()\n");
                        break;
                }
            }
        } catch (IllegalArgumentException iae) {
            System.out.printf("%s please try again.\n\n", iae.getMessage());
            menu();
        }
    }

    private void createdNewTeamIfPossible() {
        try {
            league.validateisPossibleCreatedTeam();
            Team team = createNewTeam();
            System.out.printf("The team %s under %s have been created.%n%n", team.getTeamName(), team.getCoach());
        } catch (IllegalArgumentException iae) {
            System.out.printf("%s\n\n", iae.getMessage());
        }
    }

    private void addPlayerMenu() {
        try {
            league.validateIfPlayersAvailable();
            league.validateIfTeamsExist();
            List<String> browseMenu = Arrays.asList("Browse by player", "Browse by team");
            int index = promptForIndex(browseMenu, "Choose your option: ");
            String option = browseMenu.get(index);
            switch (option) {
                case "Browse by player":
                    addPlayerIfPossible("player");
                    break;
                case "Browse by team":
                    addPlayerIfPossible("team");
                    break;
                default:
                    System.out.println("Error, check addPlayerMenu()\n");
                    break;
            }
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().contains("Unknown")) {
                System.out.printf("%s please try again.\n\n", iae.getMessage());
                addPlayerMenu();
            } else {
                System.out.printf("%s\n\n", iae.getMessage());
                menu();
            }
        }
    }

    private void removePlayerMenu() {
        try {
            league.validatePlayersInsideTeams();
            List<String> browseMenu = Arrays.asList("Browse by player", "Browse by team");
            int index = promptForIndex(browseMenu, "Choose your option: ");
            String option = browseMenu.get(index);
            switch (option) {
                case "Browse by player":
                    removePlayerIfPossible("player");
                    break;
                case "Browse by team":
                    removePlayerIfPossible("team");
                    break;
                default:
                    System.out.println("Error, check RemovePlayerMenu().\n");
                    break;
            }
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().contains("Unknown")) {
                System.out.printf("%s please try again.\n\n", iae.getMessage());
                removePlayerMenu();
            } else {
                System.out.printf("%s\n\n", iae.getMessage());
                menu();
            }
        }
    }

    private void leagueBalanceReport() {
        try {
            league.validatePlayersInsideTeams();
            for (Team team : league.getTeams()) {
                System.out.printf("League Balance Report for team %s\n", team.getTeamName());
                try {
                    team.validatePlayersInsideThisTeam();
                    viewHeightReport(team);
                    viewExperienceReport(team);
                } catch (IllegalArgumentException iae) {
                    System.out.printf("%s\n\n", iae.getMessage());
                }
            }
        } catch (IllegalArgumentException iae) {
            System.out.printf("%s\n\n", iae.getMessage());
        }
    }

    private void rosterMenu() {
        try {
            league.validatePlayersInsideTeams();
            Team team = promptForTeam();
            team.validatePlayersInsideThisTeam();
            rosterList(team);
        } catch (IllegalArgumentException iae) {
            System.out.printf("%s\n\n", iae.getMessage());
        }
    }

    private void rosterList(Team team) {
        System.out.printf("Coach: %s\n", team.getCoach());
        System.out.printf("Players of the team %s:\n", team.getTeamName());
        int index = 1;
        for (Player player : team.getPlayers()) {
            System.out.printf("%d) %s, %s\n", index, player.getLastName(), player.getFirstName());
            index++;
        }
        System.out.printf("\n");
    }

    private void viewExperienceReport(Team team) {
        Map<String, Integer> teamExperience = team.playersByExperience();
        System.out.printf("\tExperience report: \n");
        System.out.printf("\t\tThe %d %% of the players are experienced.\n", team.percentageOfExperiencedPlayers(teamExperience));
        System.out.printf("\t\t\t%d experienced players.\n", teamExperience.get("experienced"));
        System.out.printf("\t\t\t%d inexperienced players.\n\n", teamExperience.get("inexperienced"));
    }

    private void viewHeightReport(Team team) {
        System.out.printf("\tHeight report: \n");
        for (Map.Entry<String, List<Player>> listEntry : team.playersByHeight().entrySet()) {
            System.out.printf("\t\tThere are %d players with %s inches of height \n", listEntry.getValue().size(), listEntry.getKey());
            for (Player player : listEntry.getValue()) {
                System.out.printf("\t\t\t%s, %s\n", player.getLastName(), player.getFirstName());
            }
        }
    }

    private void addPlayerIfPossible(String browseMethod) {
        if (browseMethod.equals("player")) {
            Player playerToAdd = promptForPlayer(league.getPlayersByNameAndStats(league.getPlayers()), league.getPlayers());
            Team teamToAdd = promptForTeam();
            league.addPlayerToTeam(playerToAdd, teamToAdd);
            System.out.printf("You added %s, %s to %s.\n\n", playerToAdd.getLastName(), playerToAdd.getFirstName(), teamToAdd.getTeamName());
        } else if (browseMethod.equals("team")) {
            Team teamToAdd = promptForTeam();
            Player playerToAdd = promptForPlayer(league.getPlayersByNameAndStats(league.getPlayers()), league.getPlayers());
            league.addPlayerToTeam(playerToAdd, teamToAdd);
            System.out.printf("You added %s, %s to %s.\n\n", playerToAdd.getLastName(), playerToAdd.getFirstName(), teamToAdd.getTeamName());
        } else {
            System.out.println("Error in addPlayerIfPossible().");
        }
    }

    private void removePlayerIfPossible(String browseMethod) {
        if (browseMethod.equals("team")) {
            Team teamToRemove = promptForTeam();
            Player playerToRemove = promptForPlayer(league.getPlayersByNameAndStats(teamToRemove.getPlayers()), teamToRemove.getPlayers());
            league.removePlayerFromTeam(playerToRemove, teamToRemove);
            System.out.printf("You remove %s, %s from %s.\n\n", playerToRemove.getLastName(), playerToRemove.getFirstName(), teamToRemove.getTeamName());
        } else if (browseMethod.equals("player")) {
            Player playerToRemove = promptForPlayer(league.getPlayersByNameAndStats(league.getAllPlayersIsideTeams()), league.getAllPlayersIsideTeams());
            Team teamToRemove = promptForTeam();
            league.removePlayerFromTeam(playerToRemove, teamToRemove);
            System.out.printf("You remove %s, %s from %s.\n\n", playerToRemove.getLastName(), playerToRemove.getFirstName(), teamToRemove.getTeamName());
        } else {
            System.out.println("Error in removePlayerIfPossible().");
        }
    }

    private Team createNewTeam() {
        System.out.printf("Introduce the name of the team: ");
        String teamName = getInput();
        System.out.printf("Introduce the name of the coach: ");
        String coach = getInput();
        return league.newTeam(teamName, coach);
    }

    private Team promptForTeam() {
        int index = promptForIndex(league.getTeamsByName(), "Choose the team:");
        List<Team> listOfTeams = league.getTeams();
        Collections.sort(listOfTeams);
        Team team = null;
        while (team == null) {
            try {
                team = listOfTeams.get(index);
            } catch (IndexOutOfBoundsException ioobe) {
                System.out.println("That option don't exist.\n");
                team = promptForTeam();
            }
        }
        return team;
    }

    private Player promptForPlayer(List<String> playersByName, List<Player> players) {
        int index = promptForIndex(playersByName, "Choose the player:");
        List<Player> listOfPlayers = players;
        Collections.sort(listOfPlayers);
        Player player = null;
        while (null == player) {
            try {
                player = listOfPlayers.get(index);
            } catch (IndexOutOfBoundsException ioobe) {
                System.out.println("That option don't exist.\n");
                player = promptForPlayer(playersByName, players);
            }
        }
        return player;
    }

    public static Prompter getPrompter() {
        if (null == instance) {
            instance = new Prompter();
        }
        return instance;
    }
}


// PENDING: DRY. Create generic for prompt methods. <T>