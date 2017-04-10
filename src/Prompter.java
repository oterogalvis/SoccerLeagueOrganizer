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
                "Exit");
        while (!done) {
            String option = menuOptions.get(promptForIndex(menuOptions, "Choose your option: "));
            switch (option) {
                case "Create new team":
                    createdNewTeamIfPossible();
                    break;
                case "Add player to a Team":
                    addPlayerIfPossible();
                    break;
                case "Remove player from a Team":
                    removePlayerIfPossible();
                    break;
                case "Exit":
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
            System.out.printf("You don't have enough players available to created more teams.\n\n", league.getTeams().size(), league.getPlayers().size());
        }
    }

    private void addPlayerIfPossible() {
        if (league.getTeams().size() <= 0) {
            System.out.println("There are no teams, please created one.\n");
        } else if (league.getPlayers().size() <= 0) {
            System.out.println("There is no more players to add.\n");
        } else {
            Player playerToAdd = promptForPlayer(league.getPlayersByNameAndStats(), league.getPlayers());
            Team teamToAdd = promptForTeam();
            league.addPlayerToTeam(playerToAdd, teamToAdd);
            System.out.printf("You added %s, %s to %s.\n\n", playerToAdd.getLastName(), playerToAdd.getFirstName(), teamToAdd.getTeamName());
        }
    }

    private void removePlayerIfPossible() {
        if (league.getTeams().size() <= 0) {
            System.out.println("There are no teams, please created one.\n");
        } else {
            Team teamToRemove = promptForTeam();
            if (teamToRemove.getPlayers().size() <= 0) {
                System.out.println("This team is empty.\n");
            }
            Player playerToRemove = promptForPlayer(teamToRemove.getPlayersByNameAndStats(), teamToRemove.getPlayers());
            league.removePlayerFromTeam(playerToRemove, teamToRemove);
            System.out.printf("You remove %s, %s from %s.\n\n", playerToRemove.getLastName(), playerToRemove.getFirstName(), teamToRemove.getTeamName());
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
        Team team = listOfTeams.get(index);
        return team;
    }

    private Player promptForPlayer(List<String> playersByName, List<Player> players) {
        int index = promptForIndex(playersByName, "Choose the player:");
        List<Player> listOfPlayers = players;
        Collections.sort(listOfPlayers);
        Player player = listOfPlayers.get(index);
        return player;
    }

    public static Prompter getPrompter() {
        if (instance == null) {
            instance = new Prompter();
        }
        return instance;
    }
}


// PENDING: IndexOutOfBoundsException.
// PENDING: DRY. Create generic for prompt methods.
// PENDING: promptForIndex() should return index-1. FIX