//
// Created by tahom on 7/17/2025.
//

#include "CSVWriter.h"

#include <filesystem>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>

//todo: remember what is stored on the heap
//a namespace is like a variable but for all the bs saying which library is in what i think
//or namespace = scope?
namespace files = std::filesystem;

//data is now the C++ equivalent of a two-dimensional ArrayList of strings
static void exportCSV(std::vector<std::vector<std::string>> data, std::string name) {
    std::string CSV_DIRECTORY = "C:\\match scheduler csvs\\";
    //make a new directory for the CSV file
    //TODO: don't make the directory if it already exists probably
    bool dirFailed = files::create_directory(CSV_DIRECTORY);
    //TODO: if dirFailed, set notif text once the gui is done
    if (dirFailed) {
        std::cout << "Succeeded in creating CSV folder!";
    } else {
        std::cout << "Failed to create the CSV folder. Maybe it already exists?";
    }

    //...sure
    std::ofstream file(CSV_DIRECTORY);
    for (int i = 0; i < sizeof(data); i++) {
        for (int j = 0; j < sizeof(data[i]); j++) {
            file << data[i][j];
            //if we aren't writing the last piece of data, add a ","
            if (j != sizeof(data[i]) - 1) file << ",";
        }

        //if we aren't writing the last line, add \n
        if (i != sizeof(data) - 1) file << "\n";
    }
}