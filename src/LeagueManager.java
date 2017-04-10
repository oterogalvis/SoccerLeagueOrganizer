import model.League;
import model.Player;
import model.Players;
import model.Team;

/**
 * Created by jorgeotero on 4/7/17.
 */
public class LeagueManager {
    public static void main(String[] args) {
        System.out.printf("There are currently %d registered players.%n", League.getLeague().getPlayers().size());
        // Your code here!
        Prompter.getPrompter().menu();
    }
}
