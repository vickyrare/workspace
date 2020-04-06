#include <iostream>
#include <functional>

using namespace std;
using namespace placeholders;

// partial functional application using std::bind
int fn(int first, int second, int third)
{
    return first + second + third;
}

int main()
{
    int first = 100;
    auto f2 = bind(fn, first, _1, _2);
    int second = 200;
    auto f3 = bind(f2, second, _1);
    int third = 300;
    auto result = f3(third);
    cout << result << endl;
    return 0;
}