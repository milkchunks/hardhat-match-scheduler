//
// Created by tahom on 7/17/2025.
//

#ifndef CSVWRITER_H
#define CSVWRITER_H
#include <string>
#include <vector>


class CSVWriter {
        public:
                static void exportCSV(std::vector<std::vector<std::string>>, std::string name);
};



#endif //CSVWRITER_H
