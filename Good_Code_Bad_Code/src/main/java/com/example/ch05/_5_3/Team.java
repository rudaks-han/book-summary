package com.example.ch05._5_3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {
    Set<String> playerNames = new HashSet<>();
    int score = 0;

    private boolean containsPlayer(String playerName) {
        return playerNames.contains(playerName);
    }

    private int getScore() {
        return this.score;
    }

    private int getTeamScoreForPlayer(List<Team> teams, String playerName) {
        for (Team team : teams) {
            if (team.containsPlayer(playerName)) {
                return team.getScore();
            }
        }

        return -1;
    }
}
