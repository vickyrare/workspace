cmake_minimum_required(VERSION 3.2)
project(cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
set(Boost_USE_STATIC_LIBS ON) # only find static libs
set(BOOST_LIBRARYDIR "C:/local/boost_1_58_0/stage/lib")

add_executable(Bind Bind.cpp)
add_executable(CRTP CRTP.cpp)

find_package(Boost 1.58.0)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    link_directories(${Boost_LIBRARY_DIRS})
    add_executable(Atomic Atomic.cpp)
    message("${BOOST_LIBRARYDIR}")
    target_link_libraries(Atomic ${Boost_LIBRARIES})
endif()
