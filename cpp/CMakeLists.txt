cmake_minimum_required(VERSION 3.2)
project(cpp)

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
add_definitions( -DBOOST_ALL_NO_LIB )
set(Boost_USE_STATIC_LIBS ON)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME OFF)
#set (Boost_DEBUG ON)
#set (Boost_LIBRARY_DIR  C:/local/boost_1_66_0/stage/lib)

add_executable(Bind Bind.cpp)
add_executable(CRTP CRTP.cpp)

add_executable(SmartPointer smart_pointers/SmartPointer.cpp)
add_executable(UniquePointer smart_pointers/UniquePointer.cpp)

add_executable(Test Test.cpp)

add_executable(BitManipulation bit_manipulation/BitManipulation.cpp)

find_package(Boost COMPONENTS system thread)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    add_executable(Atomic Atomic.cpp)

    add_executable(Serial comms/serial.cpp)
    target_link_libraries(Serial ${Boost_LIBRARIES})

    add_executable(Client Client.cpp)
    target_link_libraries(Client ${Boost_LIBRARIES})
    add_executable(Server Server.cpp)
    target_link_libraries(Server ${Boost_LIBRARIES})
endif()
