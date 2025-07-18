//
// Created by tahom on 7/17/2025.
//

#include "Scheduler.h"

#include <string>
#include <vector>

#include "CSVWriter.h"
//.cpp: fill out what was outlined in .h
Scheduler::Scheduler(std::vector<std::string> names) {
    for (int i = 0; i < names.size(); i++) {
        teamNames[i].assign(names[i]);
    }
    //update all CSVs with new names.
    prepareTeamCSV();
}

void Scheduler::prepareTeamCSV() {
    /*
    String[][] output = new String[25][2];
    output[0][0] = "Team Number";
    output[0][1] = "Team Name";

    for (int i = 1; i < 25; ++i) {
    output[i][0] = "" + i;
    }

    for (int i = 0; i < 24; ++i) {
    output[i + 1][1] = teamNames[i];
    }

    MatchMaker.statusUPD("Exporting Matches");
    MatchMaker.callCSV(output, "teams.csv");
    this.prepareScheduleCSV();
    */

    //to match the type of data parameter in CSVWriter.exportCSV()
    std::vector<std::vector<std::string>> output;
    output[0][0] = "Team Number";
    output[0][1] = "Team Name";

    for (int i = 1; i < 25; i++) {
        output[i][0] = "" + i;
    }

    for (int i = 0; i < 24; i++) {
        output[i+1][1] = teamNames[i];
    }

    //TODO: MatchMaker.statusUPD
    //objects arent pointers here i guess...
    CSVWriter writer;
    writer.exportCSV(output, "teams.csv");
}

void Scheduler::changeTeamNames(std::vector<std::string> names) {
    for (int i = 0; i < names.size(); i++) {
        teamNames[i].assign(names[i]);
    }
}

std::vector<Team> Scheduler::sortAscending(std::vector<Team> arr) {
    for (int i = 1; i < arr.size(); i++) {
        Team temp = arr[i];
        int j = i-1;

        while (j >= 0 && arr[j].matchesPlayed > temp.matchesPlayed) {
            arr[j+1] = arr[j];
            j = j-1;
        }
        arr[j+1] = temp;
    }

    return arr;
}

static bool teamListContains(std::vector<Team> arr, Team val) {
    for (int i = 0; i < arr.size(); i++) {\
        //todo: CANNOT CHECK FOR NULL.
        if (arr[i] == val) return true;
    }
}