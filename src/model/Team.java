package model;

import java.util.*;


/**
 * Created by jorgeotero on 4/7/17.
 */
public class Team implements Comparable<Team> {
    private String teamName;
    private String coach;
    private Set<Player> players;


    public Team(String teamName, String coach) {
        this.teamName = teamName;
        this.coach = coach;
        this.players = new HashSet<>();
    }

    public Map<String, List<Player>> playersByHeight() {
        Map<String, List<Player>> playersByHeight = new HashMap<>();
        List<Player> firstRange = new ArrayList<>();
        List<Player> secondRange = new ArrayList<>();
        List<Player> thirdRange = new ArrayList<>();
        for (Player player : getPlayers()) {
            if (player.getHeightInInches() <= 40) {
                firstRange.add(player);
            } else if (player.getHeightInInches() >= 47) {
                thirdRange.add(player);
            } else {
                secondRange.add(player);
            }
        }
        playersByHeight.put("35-40", firstRange);
        playersByHeight.put("41-46", secondRange);
        playersByHeight.put("47-50", thirdRange);
        return playersByHeight;
    }

    public Map<String, Integer> playersByExperience() {
        Map<String, Integer> playersByExperience = new HashMap<>();
        Integer countExperienced = 0;
        Integer countUnexperienced = 0;
        for (Player player : getPlayers()) {
            if (player.isPreviousExperience()) {
                countExperienced++;
            } else {
                countUnexperienced++;
            }
            playersByExperience.put("experienced", countExperienced);
            playersByExperience.put("inexperienced", countUnexperienced);
        }
        return playersByExperience;
    }

    public int percentageOfExperiencedPlayers(Map<String, Integer> playerByExperience) {
        int experiencedPlayers = playerByExperience.get("experienced");
        int inexperiencedPlayers = playerByExperience.get("inexperienced");
        return (experiencedPlayers * 100) / (experiencedPlayers + inexperiencedPlayers);
    }

    public void validatePlayersInsideThisTeam() {
        if (getPlayers().size() <= 0) {
            throw new IllegalArgumentException("There are no players inside this team");
        }
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
        List<Player> playersList = new ArrayList(players);
        Collections.sort(playersList);
        return playersList;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public int compareTo(Team otherTeam) {
        return teamName.compareTo(otherTeam.teamName);
    }
}
