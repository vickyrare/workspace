//
// Created by Waqqas Sharif on 12/4/20.
//

#include <iostream>

using namespace std;

struct Value
{
    int value;
    Value()
    {
        cout << "Constructor called" << endl;
    }

    ~Value()
    {
        cout << "Destructor called" << endl;
    }
};

int main()
{
    unique_ptr<Value> up(new Value);
    //unique_ptr<Value> up2 = up; // won't compile pointer is unique and cannot be assigned
    unique_ptr<Value> up2 = std::move(up);
}
