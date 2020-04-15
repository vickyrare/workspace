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
    shared_ptr<Value> sp(new Value);
    cout << sp.use_count() << endl;

    shared_ptr<Value> sp2 = sp;
    cout << sp2.use_count() << endl;

    sp->value = 100;

    cout << sp->value << endl;
    cout << sp2->value << endl;

    sp.reset();

    cout << sp.use_count() << endl;
    cout << sp2.use_count() << endl;

    auto sp3 = make_shared<Value>();
}
