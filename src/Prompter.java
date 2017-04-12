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
    private static Prompter instance;

    private Prompter() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        league = League.getLeague();
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

    private String getInput(String output) {
        System.out.println(output);
        return getInput();
    }

    private int getInputInt() {
        int input = 0;
        while (input < 1) {
            try {
                input = Integer.parseInt(getInput());
            } catch (NumberFormatException nfe) {
                System.out.println("Input must be numeric.");
                input = 0;
            }
            if (input < 1) {
                System.out.println("Input must be equal or higher than 1.");
            }
        }
        return input;
    }

    private int promptForIndex(List<String> options, String optionsTitle) {
        System.out.println(optionsTitle);
        int index = 1;
        for (String option : options) {
            System.out.printf("\t%d) %s\n", index, option);
            index++;
        }
        return (getInputInt() - 1);
    }

    public void menu() {
        boolean done = false;
        List<String> menuOptions = Arrays.asList(
                "Create new team",
                "Add player to a Team",
                "Remove player from a Team",
                "League Balance Report",
                "Exit");
        while (!done) {
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
                case "Exit":
                    System.out.printf("Exiting...");
                    done = true;
                    break;
                default:
                    System.out.println("Unkown option. Please try again.\n");
                    break;
            }
        }
    }

    private void createdNewTeamIfPossible() {
        if (league.isPossibleCreatedTeam()) {
            Team team = createNewTeam();
            System.out.printf("The team %s under %s have been created.%n%n", team.getTeamName(), team.getCoach());
        } else {
            System.out.printf("Is not possible to created more teams than players availables.\n\n", league.getTeams().size(), league.getPlayers().size());
        }
    }

    private void addPlayerMenu() {
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
                System.out.println("Unkown option. Please try again.\n");
                break;
        }

    }

    private void removePlayerMenu() {
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
                System.out.println("Unkown option. Please try again.\n");
                break;
        }
    }

    private void leagueBalanceReport() {
        if (league.getAllPlayersIsideTeams().size() <= 0) {
            System.out.println("There is no players inside any team.\n");
        } else {
            for (Team team : league.getTeams()) {
                if (team.getPlayers().size() <= 0) {
                    System.out.printf("There is not players on team: %s\n\n", team.getTeamName());
                } else {
                    System.out.printf("League Balance Report for team %s\n", team.getTeamName());
                    viewHeightReport(team);
                    viewExperienceReport(team);
                }
            }
        }
    }

    private void viewExperienceReport(Team team) {
        Map<String, Integer> teamExperience = team.playersByExperience();
        System.out.printf("\tExperience report: \n");
        System.out.printf("\t\tThe %d %% of the players are experienced.\n" , team.percentageOfExperiencedPlayers(teamExperience));
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
        if (league.getTeams().size() <= 0) {
            System.out.println("There are no teams, please created one.\n");
        } else if (league.getPlayers().size() <= 0) {
            System.out.println("There is no more players to add.\n");
        } else if (browseMethod.equals("player")) {
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
        if (league.getTeams().size() <= 0) {
            System.out.println("There are no teams, please created one.\n");
        } else if (league.getAllPlayersIsideTeams().size() <= 0) {
            System.out.println("There is no players inside any team.\n");
        } else if (browseMethod.equals("team")) {
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
            System.out.println("Error in addPlayerIfPossible().");
        }
    }

    private Team createNewTeam() {
        String teamName = getInput("Introduce the name of the team: ");
        String coach = getInput("Introduce the name of the coach: ");
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


// PENDING: IndexOutOfBoundsException.
// PENDING: DRY. Create generic for prompt methods. <T>
// PENDING: promptForTeam() should have the same parameters that promptForPlayer().
// PENDING: find out why the the fancy hash method is better.