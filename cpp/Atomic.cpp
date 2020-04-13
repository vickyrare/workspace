#include <boost/atomic.hpp>
#include <thread>
#include <iostream>

boost::atomic<int> a{0};
//int a{0};

void thread()
{
    ++a;
}

int main()
{
    std::thread t1{thread};
    std::thread t2{thread};
    std::thread t3{thread};
    std::thread t4{thread};
    std::thread t5{thread};
    std::thread t6{thread};
    std::thread t7{thread};

    t1.join();
    t2.join();
    t3.join();
    t4.join();
    t5.join();
    t6.join();
    t7.join();
    std::cout << a << '\n';
  return 0;
}