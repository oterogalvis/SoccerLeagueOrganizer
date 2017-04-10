package model;

import java.util.*;


/**
 * Created by jorgeotero on 4/7/17.
 */
public class Team implements Comparable<Team>{
    private String teamName;
    private String coach;
    private Set<Player> players;


    public Team(String teamName, String coach) {
        this.teamName = teamName;
        this.coach = coach;
        this.players = new HashSet<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
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
        return  new ArrayList(players);
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public int compareTo(Team otherTeam) {
        return teamName.compareTo(otherTeam.teamName);
    }
}
