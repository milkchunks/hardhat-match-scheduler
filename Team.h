//
// Created by catti on 7/17/2025.
//

#ifndef TEAM_H
#define TEAM_H
#include <string>


class Team {
    public:
        int matchesPlayed;
        static int teamNumber;
        std::string toString();
        Team(int num);
};



#endif //TEAM_H
