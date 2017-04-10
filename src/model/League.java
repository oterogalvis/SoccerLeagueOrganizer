package model;

import java.util.*;

/**
 * Created by jorgeotero on 4/7/17.
 */
public class League {
    private List<Team> teams;
    private List<Player> players;
    private static League instance;

    private League() {
        teams = new ArrayList<>();
        players = Arrays.asList(Players.load());
    }

    public Team newTeam(String teamName, String coach) throws IllegalArgumentException {
        Team team = new Team(teamName,coach);
        teams.add(team);
        return team;
    }

    public void addPlayerToTeam(Player player, Team team) {
        team.addPlayer(player);
        players.remove(player);
    }

    public boolean isPossibleCreatedTeam() {
        return (teams.size() < (players.size() / 11));
    }

    public List<String> getPlayersByName() {
        List<String> playersByName = new ArrayList<>();
        for (Player player : getPlayers()) {
            playersByName.add(player.getFullName());
        }
        return playersByName;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public static League getLeague() {
        if (instance == null) {
            instance = new League();
        }
        return instance;
    }
}
