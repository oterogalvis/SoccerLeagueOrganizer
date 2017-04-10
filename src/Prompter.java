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
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));;
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

    private int getInputInt(String output) {
        int input = 0;
        while (input < 1) {
            try {
                input = Integer.parseInt(getInput(output));
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

    private int getInputInt() {
        return getInputInt("");
    }

    public void menu() {
        boolean done = false;
        while (!done){
            String option = promptForString(Arrays.asList(
                    "Create new team",
                    "Add player to a Team",
                    "Exit"),
                    "Choose your option: ");
            switch (option) {
                case "Create new team":
                    createdNewTeamIfPossible();
                    break;
                case "Add player to a Team":
                    addPlayerIfPossible();
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
            System.out.printf("You can only created up to %d with %d players.", league.getTeams().size(), league.getPlayers().size());
        }
    }

    private void addPlayerIfPossible() {
        if (league.getTeams().size() <= 0) {
            System.out.println("There are no teams, please created one.\n");
        } else if (league.getPlayers().size() <= 0) {
            System.out.println("There is no more players to add.\n");
        } else {
//            Player playerToAdd = promptForPlayer(league.getPlayers(), "Choose the player");
            String playerName = promptForString(league.getPlayersByName(), "Choose the player:");
            Player playerToAdd;
            for (Player player : league.getPlayers()) {
                if (player.getFullName() == playerName) {
                    playerToAdd = player;
                }
            }
            Team teamToAdd = promptForTeam(league.getTeams(), "Choose the team: ");
            league.addPlayerToTeam(playerToAdd, teamToAdd);
            System.out.printf("You added %s to %s.\n\n", playerToAdd.getFirstName(), teamToAdd.getTeamName());
        }
    }

//    private Player choosePlayer() {
//        Player playerChose = promptForPlayer(league.getPlayers(), "Choose the player");
//        System.out.println("You chose " + playerChose.getLastName() + ", " + playerChose.getFirstName());
//        return playerChose;
//    }


//    private Team chooseTeam() {
//        Team teamChose = promptForTeam(league.getTeams(), "Choose the team: ");
//        System.out.println("You chose " + teamChose.getTeamName());
//        return teamChose;
//    }

    private List<String> getTeamsByName() {
        List<String> teamsByName = new ArrayList<>();
        for (Team team : league.getTeams()) {
            teamsByName.add(team.getTeamName());
        }
        return teamsByName;
    }

    private Team createNewTeam() {
        String teamName = getInput("Introduce the name of the team: ");
        String coach = getInput("Introduce the name of the coach: ");
        return league.newTeam(teamName, coach);
    }

    private String promptForString(List<String> options, String optionsTitle) {
        System.out.println(optionsTitle);
        Integer index = 1;
        Map<Integer, String> stringsByIndex = new HashMap<>();
        for (String option : options) {
            System.out.printf("\t%d) %s%n", index, option);
            stringsByIndex.put(index, option);
            index++;
        }
        return stringsByIndex.get(Integer.valueOf(getInput()));
    }

    private Team promptForTeam(List<Team> teamList, String optionTitle) {
        System.out.println(optionTitle);
        Integer index = 1;
        Map<Integer, Team> teamsByIndex = new HashMap<>();
        for (Team team : teamList) {
            System.out.printf("\t%d) %s%n", index, team.getTeamName());
            teamsByIndex.put(index,team);
            index++;
        }
        return teamsByIndex.get(getInputInt());
    }

    private Player promptForPlayer(List<Player> playerList, String optionTitle) {
        System.out.println(optionTitle);
        Integer index = 1;
        Map<Integer, Player> teamsByIndex = new HashMap<>();
        for (Player player : playerList) {
            System.out.printf("\t%d) %s\n", index, player.getFullName());
            teamsByIndex.put(index,player);
            index++;
        }
        return teamsByIndex.get(getInputInt());
    }

    public static Prompter getPrompter() {
        if (instance == null) {
            instance = new Prompter();
        }
        return instance;
    }
}


// PENDING: Simplify getImput methods.
// PENDING: Create generic promptFor method. DRY.
// PENDING: Fix method promptForIndex to be prompForString.