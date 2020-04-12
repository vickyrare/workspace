//
// Created by Waqqas Sharif on 12/4/20.
//

//CRTP is used for compile time polymorphism

#include <iostream>

using namespace std;

class Base
{
public:
    virtual void fn()
    {
        cout << "Base fn" << endl;
    }
};

class Derived : public Base
{
public:
    virtual void fn()
    {
        cout << "Derived fn" << endl;
    }
};

template<typename type>
class BaseCRTP
{
public:
    void fn()
    {
        static_cast<type*>(this)->fn();
    }
};

class DerivedCRTP : public BaseCRTP<DerivedCRTP>
{
public:
    void fn()
    {
        cout << "DerivedCRTP fn" << endl;
    }
};

class DerivedCRTP2 : public BaseCRTP<DerivedCRTP2>
{
public:
    void fn()
    {
        cout << "DerivedCRTP2 fn" << endl;
    }
};

void callFn(Base *b)
{
    b->fn();
}

void callFn(Base b)
{
    b.fn();
}

template<typename type>
void callFn(BaseCRTP<type> b)
{
    b.fn();
}

int main()
{
    //Runtime polymorphism using VTable
    Base *b = new Base;
    Derived *d = new Derived;
    callFn(b);
    callFn(d);

    //Runtime polymorphism is only supported dynamically
    Base bb;
    Derived dd;
    callFn(bb);
    callFn(dd);

    //Compile time polymorphism
    DerivedCRTP dc;
    DerivedCRTP2 dc2;
    callFn(dc);
    callFn(dc2);
}
