//
// Created by tahom on 7/17/2025.
//

#ifndef SCHEDULER_H
#define SCHEDULER_H
#include <string>
#include <vector>


//.h: declare what is in the class (?)
class Scheduler {
    private:
    std::string teamNames[];

    //idfk if this is good practice :(
    //i don't think it was even good practice in java
    //note: inner classes are NOT hoisted
    class Team {
        public:
        int numMatchesPlayed = 0;
        int teamNumber;
        std::string toString() {
            return "Team[num: " + std::to_string(teamNumber) + ", matches: " + std::to_string(numMatchesPlayed) + "]";
        }

        Team(int num) {
            teamNumber = num;
        }
    };

    std::vector<Team> sortAscending();

    public:
    void changeTeamNames();
    void prepareTeamCSV();
    void prepareScheduleCSV();
};



#endif //SCHEDULER_H
