cmake_minimum_required(VERSION 3.2)
project(cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
add_definitions( -DBOOST_ALL_NO_LIB )
set(Boost_USE_STATIC_LIBS ON)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME OFF)

add_executable(Atomic Atomic.cpp)
add_executable(Bind Bind.cpp)
add_executable(CRTP CRTP.cpp)

add_executable(SmartPointer smart_pointers/SmartPointer.cpp)
add_executable(UniquePointer smart_pointers/UniquePointer.cpp)

add_executable(Client Client.cpp)
add_executable(Server Server.cpp)

add_executable(BitManipulation bit_manipulation/BitManipulation.cpp)

find_package(Boost 1.58.0 REQUIRED COMPONENTS system thread)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    add_executable(Test Test.cpp)
    add_executable(Serial comms/serial.cpp)
    target_link_libraries(Serial ${Boost_LIBRARIES})
endif()
