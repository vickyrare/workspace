//
// Created by Waqqas Sharif on 8/7/20.
//

#include <iostream>
#include <string>
#include <bitset>

using namespace std;
int main()
{
//  100010001
    cout << "read the value at 5th bit" << endl;
    uint8_t input1 = 0x91;
    bitset<8> a(input1);
    cout << a << endl;
    a &= 0x10;
    cout << a << endl;
    cout << "=================" << endl;

//  10000001
    cout << "set the value at 6th bit" << endl;
    uint8_t input2 = 0x81;
    bitset<8> b(input2);
    cout << b << endl;
    b |= 0xA1;
    cout << b << endl;
    cout << "=================" << endl;

//  01000001
    cout << "unset the value at 7th bit" << endl;
    uint8_t input3 = 0x41;
    bitset<8> c(input3);
    cout << c << endl;
    c &= 0xA1;
    cout << c << endl;
    cout << "=================" << endl;

//  10001000
    cout << "toggle the value at 8th bit" << endl;
    uint8_t input4 = 0x88;
    bitset<8> d(input4);
    cout << d << endl;
    d ^= 0x80;
    //d = ~d;
    cout << d << endl;
    cout << "=================" << endl;
    return 0;
}