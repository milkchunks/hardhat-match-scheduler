//
// Created by tahom on 7/17/2025.
//

//TODO: things to fix
//back to backs (cooldown length selector?)
//thing in stream comments

#include "Scheduler.h"

#include <algorithm>
#include <cmath>
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

//not quite done
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

//done
void Scheduler::changeTeamNames(std::vector<std::string> names) {
    for (int i = 0; i < names.size(); i++) {
        teamNames[i].assign(names[i]);
    }
	
	//update CSV
	prepareTeamCSV();
}

//todo: setting for how many matches long cooldown is?
//todo: issue, 1 team 7 matches 1 team 9 matches
//todo: issue in the comment on the livestream
void prepareScheduleCSV() {
	std::string settings[2] = UI.getSettings();
	
	//what's optimization?
	int NUM_TEAMS = std::stoi(settings[0]);
	int MATCH_SIZE = 4;
	int MATCH_CAP = std::stoi(settings[1]) - 1;
	//TODO: make this work
	int COOLDOWN_LENGTH = 1;

	//no delete bc no new keyword?
	//TODO: REMEMBER TO DELETE ALL THESE TEAMS ONCE THE METHOD IS OVER. iterate through allTeams

	//i think i want team pointers? TODO: delete pointers!!! you're allocating a shit ton of pointers in all these lists, go through and delete them all later
	Team* allTeams[NUM_TEAMS];
	//a candidate is a team that both hasn't played all their matches and isn't on cooldown
	std::vector<Team*> candidates;
	//all teams are candidates at first
	for (int a = 1; a < NUM_TEAMS + 1; a++) {
		allTeams[a] = new Team(a);
		candidates[a] = allTeams[a];
	}

	
	std::vector<std::vector<Team*>> matches;

	//does NOT try to initialize all the teams
	//todo: expandable cooldown size
	Team* onCooldown[MATCH_SIZE];

	int MATCH_COUNT = 0;
	while (!candidates.empty()) {
		//vector of size MATCH_SIZE. calls the constructor!
		std::vector<Team*> thisMatch(MATCH_SIZE);
		
		//this loop populates a match
		for (int j = 0; j < thisMatch.size(); j++) {
			//cube root to make it trend towards smaller indices (the candidates who have played the least games!)
			//maybe square root it? test in an ~environment~
			Team* chosenTeam = candidates[std::floor(std::cbrt(std::rand() % static_cast<int>(candidates.size())))];
		
			//increment # matches
			chosenTeam->matchesPlayed++;

			//if a team has played all their matches, remove them from candidates
			if (chosenTeam->matchesPlayed >= MATCH_CAP) {
				//todo: erase chosenTeam from candidates
			}

			//add the chosen team to this match
			thisMatch[j] = chosenTeam;
		}

		//add this newly populated match to the end of matches
		matches.push_back(thisMatch);

		//now that we've populated the new match, replace who's on cooldown
		//TODO: i believe this is where the null is
		for (int b = 0; b < MATCH_SIZE; b++) {
			Team* temp = onCooldown[b];
			onCooldown[b] = thisMatch[b];
			//make teams on cooldown no longer candidates
			//todo: erase onCooldown[b] from candidates
			delete temp;
		}

		//only force if there are candidates who haven't played their matches
		//check again if candidates are empty after filling matches
		if (!candidates.empty()) {
			//assemble a list of teams who are not on cooldown AND have exactly hit their match limit AND are not already candidates
			//if there aren't enough after doing that, increase the match count requirement until we have enough candidates to force
			std::vector<Team*> forcedCandidates;

			//starts at zero to assemble forcedCandidates
			//this value is how much are we willing to fudge the match # cutoff to let all candidates play at least MATCH_CAP games, starting with one
			int thresholdShift = 0;

			//if, even after including those who have hit their limit, we don't have enough candidates, keep fudging the threshold until we do.
			//if we do have enough candidates, this is skipped so it's not too strenuous
			while (forcedCandidates.size() + candidates.size() < MATCH_SIZE) {
				//TODO: this will just add the first teams that can be forced (adds 1, 2, 3, etc.) choose random from allTeams? or grab ALL the forced candidates, increase threshold shift if you don't have enough, then grab randomly from that
				for (int d = 0; d < sizeof(allTeams); d++) {
					Team* elm = allTeams[d];
					bool isCandidate = std::count(candidates.begin(), candidates.end(), elm) > 0;
					bool isOnCooldown = std::find(std::begin(onCooldown), std::end(onCooldown), elm) != std::end(onCooldown);
					//and not on cooldown
					if (!isCandidate && elm->matchesPlayed == MATCH_CAP + thresholdShift && !isOnCooldown) {
						//maybe doesn't work
						forcedCandidates[forcedCandidates.size() - 1] == elm;
					}
				}

				//increase threshold shift and go again
				thresholdShift++;
			}

			//add all the forced candidates
			for (int f = 0; f < forcedCandidates.size(); f++) {
				int index = std::rand() % forcedCandidates.size();
				candidates.push_back(forcedCandidates[index]);
				//TODO: remove the chosen forced candidate
			}
		}

		//after everything, sort the candidates by matches played in ascending order cuz the randomizer prefers lower indices
		candidates = sortAscending(candidates);
		MATCH_COUNT++;
	}
}

	//done
std::vector<Team*> Scheduler::sortAscending(std::vector<Team*> arr) {
		for (int i = 1; i < arr.size(); i++) {
			Team* temp = arr[i];
			int j = i-1;

			while (j >= 0 && arr[j]->matchesPlayed > temp->matchesPlayed) {
				arr[j+1] = arr[j];
				j = j-1;
			}
			arr[j+1] = temp;
		}

		return arr;
	};