cmake_minimum_required(VERSION 3.2)
project(cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
#set(Boost_USE_STATIC_LIBS ON) # only find static libs

add_executable(Atomic Atomic.cpp)
add_executable(Bind Bind.cpp)
add_executable(CRTP CRTP.cpp)

add_executable(SmartPointer smart_pointers/SmartPointer.cpp)
add_executable(UniquePointer smart_pointers/UniquePointer.cpp)

add_executable(Client Client.cpp)
add_executable(Server Server.cpp)

add_executable(BitManipulation bit_manipulation/BitManipulation.cpp)

find_package(Boost COMPONENTS thread system REQUIRED)
if(Boost_FOUND)
    add_executable(Test Test.cpp)
    target_link_libraries(Test Boost::thread)
endif()
