package model;

import java.util.*;

/**
 * Created by jorgeotero on 4/7/17.
 */
public class League {
    private List<Team> teams;
    private Set<Player> players;
    private static League instance;

    private League() {
        teams = new ArrayList<>();
        players = new HashSet<>(Arrays.asList(Players.load()));
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

    public  void removePlayerFromTeam(Player player, Team team) {
        team.removePlayer(player);
        players.add(player);
    }

    public List<Player> getAllPlayersIsideTeams() {
        List<Player> listOfPlayersInsideTeams = new ArrayList<>();
        for (Team team : getTeams()) {
            listOfPlayersInsideTeams.addAll(team.getPlayers());
        }
        Collections.sort(listOfPlayersInsideTeams);
        return listOfPlayersInsideTeams;
    }

    public boolean isPossibleCreatedTeam() {
        return (teams.size() < (players.size() / 11));
    }

    public List<String> getPlayersByNameAndStats(List<Player> players) {
        List<String> playersByName = new ArrayList<>();
        for (Player player : players) {
            playersByName.add(player.getLastName() + ", " + player.getFirstName() +
                    "\t\tHeight: " + player.getHeightInInches() + "\t\tExperience: " + player.isPreviousExperience());
        }
        Collections.sort(playersByName);
        return playersByName;
    }

    public List<String> getTeamsByName() {
        List<String> teamsByName = new ArrayList<>();
        for (Team team : getTeams()) {
            teamsByName.add(team.getTeamName());
        }
        Collections.sort(teamsByName);
        return teamsByName;
    }

    public List<Team> getTeams() {
        Collections.sort(teams);
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Player> getPlayers() {
        return  new ArrayList(players);
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public static League getLeague() {
        if (instance == null) {
            instance = new League();
        }
        return instance;
    }
}
