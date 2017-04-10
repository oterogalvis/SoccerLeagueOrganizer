package model;

import java.util.*;


/**
 * Created by jorgeotero on 4/7/17.
 */
public class Team implements Comparable<Team>{
    private String teamName;
    private String coach;
    private List<Player> players;


    public Team(String teamName, String coach) {
        this.teamName = teamName;
        this.coach = coach;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<String> getPlayersByNameAndStats() {
        List<String> playersByName = new ArrayList<>();
        for (Player player : getPlayers()) {
            playersByName.add(player.getLastName() + ", " + player.getFirstName() +
                    "\tHeight: " + player.getHeightInInches() + "\tExperience: " + player.isPreviousExperience());
        }
        Collections.sort(playersByName);
        return playersByName;
    }

    public void deletedPlayer(Player player) {
        players.remove(player);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public int compareTo(Team otherTeam) {
        return teamName.compareTo(otherTeam.teamName);
    }
}
