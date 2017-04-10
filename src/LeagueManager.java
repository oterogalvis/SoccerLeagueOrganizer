import model.League;
import model.Player;
import model.Players;

/**
 * Created by jorgeotero on 4/7/17.
 */
public class LeagueManager {
    public static void main(String[] args) {
        Player[] players = Players.load();
        System.out.printf("There are currently %d registered players.%n", players.length);
        // Your code here!
        League.getLeague().setPlayers(players);
        Prompter.getPrompter().menu();
    }
}
