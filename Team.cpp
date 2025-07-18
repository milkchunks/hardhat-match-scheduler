//
// Created by catti on 7/17/2025.
//

#include "Team.h"


Team::Team(int num) {
    teamNumber = num;
    matchesPlayed = 0;
}

std::string Team::toString() {
    return "Team[num: " + std::to_string(teamNumber) + ", matches: " + std::to_string(matchesPlayed) + "]";
}