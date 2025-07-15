//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.Random;

public class Scheduler {
    private String[] teamNames;

    public Scheduler(String[] teamNames) {
        this.teamNames = teamNames;
        this.prepareTeamCSV(teamNames);
    }

    public Scheduler() {}

    //changes names and updates the csv
    public void changeTeamNames(String[] newNames) {
        this.teamNames = newNames;
        this.prepareTeamCSV(teamNames);
    }

    public void prepareTeamCSV(String[] teamNames) {
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
    }

    public void prepareScheduleCSV() {
        String[] settings = MatchMaker.pollUISettings();

        //what's optimization?
        int NUM_TEAMS = Integer.parseInt(settings[0]);
        int MATCH_SIZE = 4;
        //todo: always exceeds the match limit by one for some reason?
        //my fix was to just make MATCH_CAP one less than the inputted cap, but thats a total bandaid
        //im sorry to whoever has to rewrite this later
        int MATCH_CAP = Integer.parseInt(settings[1]) - 1;



        ArrayList<Team> allTeams = new ArrayList<>();
        for (int a = 1; a < NUM_TEAMS + 1; a++) {
            allTeams.add(new Team(a));
        }

        //copy all teams to candidates (i.e. every team is a candidate when starting selection)
        ArrayList<Team> candidates = new ArrayList<>(allTeams);


        ArrayList<Team[]> matches = new ArrayList<>();
        Team[] onCooldown = new Team[MATCH_SIZE];

        int MATCH_COUNT = 0;
        //go until there isn't anyone who is a candidate (a candidate is a team that isn't on cooldown and has played all their games)
        //todo: what if a team is the only one who hasn't played all their games, but they're on cooldown rn? have another match of teams who HAVE played all their games or ignore the cooldown?
        while (!candidates.isEmpty()) {
            Team[] thisMatch = new Team[MATCH_SIZE];

            //this loop populates a match
            for (int j = 0; j < thisMatch.length; j++) {
                //roll team w/ random id from 0 to # candidates
                Team chosenTeam = candidates.get((int) (Math.random() * candidates.size()));

                //while the rolled team isn't a candidate or the team is already in the match, keep rolling
                while (!candidates.contains(chosenTeam) || teamListContains(thisMatch, chosenTeam)) {
                    //todo: what if a team has only played 4 matches after 49 while everyone else has played 6? ignore cooldown?

                    //Math.random() is cubed to make it trend towards smaller indices (the candidates who have played the least games!)
                    chosenTeam = candidates.get((int) (Math.pow(Math.random(), 3) * candidates.size()));
                }

                //increment # matches
                chosenTeam.numMatchesPlayed++;

                //if a team has played all their matches, remove them from candidates
                if (chosenTeam.numMatchesPlayed >= MATCH_CAP) {
                    candidates.remove(chosenTeam);
                }

                //add the chosen team to this match
                thisMatch[j] = chosenTeam;
            }

            //add this newly populated match to matches
            matches.add(thisMatch);

            //now that we've populated the new match, replace who's on cooldown cuz cooldown only lasts one match
            for (int b = 0; b < MATCH_SIZE; b++) {
                Team temp = onCooldown[b];
                onCooldown[b] = thisMatch[b];
                //make teams on cooldown no longer candidates
                candidates.remove(onCooldown[b]);
                //let teams that are being taken off of cooldown be candidates again
                //IFF they aren't null (cuz no candidates will be taken off after the first match) and haven't hit their match max
                if (temp != null && !(temp.numMatchesPlayed > MATCH_CAP)) {
                    candidates.add(temp);
                }
            }

            //if nobody needs to hit their match quota, just end it
            if (candidates.isEmpty()) break;

            //if most but not all teams have hit the match cap, force some more in

            //there must ALWAYS be at least 4 candidates if we're playing more matches. if there aren't, add random teams who have played their matches (they'd be over their quota, but it's better than no game)
            //(there's only really going to be less than enough candidates on the last couple matches, so this whole block of code will usually be skipped)
                while (candidates.size() < MATCH_SIZE) {
                    //assemble a list of teams who are not on cooldown AND have exactly hit their match limit AND are not already candidates. if there aren't enough that are at their match limit, increase the match count requirement by one until we find enough candidates
                    ArrayList<Team> forcedCandidates = new ArrayList<>();

                    //starts at zero to assemble forcedCandidates
                    //this value is how much are we willing to fudge the match # cutoff to let all candidates play at least MATCH_CAP games, starting with one
                    int thresholdShift = 0;
                    //if, even after including those who have hit their limit, we don't have enough candidates, keep fudging the threshold until we do.
                    //if we do have enough candidates, this is skipped so it's not too strenuous
                    while (!(forcedCandidates.size() + candidates.size() >= MATCH_SIZE)) {
                        for (int d = 0; d < allTeams.size(); d++) {
                            Team elm = allTeams.get(d);
                            if (!candidates.contains(elm) && elm.numMatchesPlayed == MATCH_CAP + thresholdShift && !teamListContains(onCooldown, elm)) {
                                forcedCandidates.add(elm);
                            }
                        }
                        //increase threshold shift and go again
                        thresholdShift++;
                    }

                    //add as many random forced candidates THAT HAVE MET THEIR MATCH QUOTA as needed to make a match
                    for (int f = 0; f < MATCH_SIZE - candidates.size(); f++) {
                        int index = (int) (Math.random() * forcedCandidates.size());
                        candidates.add(forcedCandidates.get(index));
                        forcedCandidates.remove(index);
                    }
                }

                //after everything, sort the candidates by matches played in ascending order cuz the randomizer prefers lower indexes
                candidates = sortAscending(candidates);
                MATCH_COUNT++;
            }

        MATCH_COUNT++;

            String[][] output = new String[MATCH_COUNT + 1][5];
            output[0][0] = "Match Number";
            output[0][1] = "Red 1";
            output[0][2] = "Red 2";
            output[0][3] = "Blue 1";
            output[0][4] = "Blue 2";

            for (int i = 1; i <= MATCH_COUNT; ++i) {
                output[i][0] = "" + i;
            }

            //fill data table [match][team]
            //starts at the second row and column cuz the first row and column are labels
            //row
            for (int a = 1; a < MATCH_COUNT + 1; a++) {
                //column
                for (int b = 1; b <= MATCH_SIZE; b++) {
                    //matches is an ArrayList of Team[]. match a, team b, then turn team b's teamNumber into a string
                    output[a][b] = String.valueOf(matches.get(a-1)[b-1].teamNumber);
                }

            }

            MatchMaker.callCSV(output, "MatchSchedule.csv");
            MatchMaker.statusUPD("Done!");
        }


    private static ArrayList<Team> sortAscending(ArrayList<Team> arr) {
        for (int i = 1; i < arr.size(); ++i) {
            Team temp = arr.get(i);
            int j = i - 1;

            while (j >= 0 && arr.get(j).numMatchesPlayed > temp.numMatchesPlayed) {
                arr.set(j+1, arr.get(j));
                j = j - 1;
            }
            arr.set(j+1, temp);
        }

        return arr;
    }

    private static boolean teamListContains(Team[] arr, Team val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (arr[i].equals(val)) return true;
            }
        }

        return false;
    }

//private inner class
private static class Team {
    public int numMatchesPlayed = 0;
    public final int teamNumber;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    @Override
    public String toString() {
        return "Team[num: " + teamNumber + ", matches: " + numMatchesPlayed + "]";
    }
}
}