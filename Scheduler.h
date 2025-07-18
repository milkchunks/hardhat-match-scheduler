//
// Created by tahom on 7/17/2025.
//

#ifndef SCHEDULER_H
#define SCHEDULER_H
#include <string>
#include <vector>

#include "Team.h"


//.h: declare what is in the class (?)
class Scheduler {
    private:
    static std::vector<std::string> teamNames[];

    static std::vector<Team> sortAscending();

    public:
    static void changeTeamNames(std::string newNames[]);
    static void prepareTeamCSV();
    static void prepareScheduleCSV();
    Scheduler(std::vector<std::string> names);
};



#endif //SCHEDULER_H
