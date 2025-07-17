cmake_minimum_required(VERSION 3.31)
project(scheduler_rewrite_c__)

set(CMAKE_CXX_STANDARD 20)

add_executable(scheduler_rewrite_c__ main.cpp
        Scheduler.cpp
        Scheduler.h
        CSVWriter.cpp
        CSVWriter.h
        UI.cpp
        UI.h)
