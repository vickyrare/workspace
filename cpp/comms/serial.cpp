#include <string>
#include <iostream>
#include <boost/asio/serial_port.hpp>
#include <boost/asio.hpp>
#include "blocking_reader.h"

using namespace std;
using namespace boost;

char read_char()
{
    asio::io_service io;
    asio::serial_port port(io);

    port.open("COM3");
    port.set_option(asio::serial_port_base::baud_rate(115200));
    port.set_option(boost::asio::serial_port::stop_bits(boost::asio::serial_port::stop_bits::one));
    port.set_option(boost::asio::serial_port::character_size());
    port.set_option(boost::asio::serial_port::parity(boost::asio::serial_port::parity::none));

    char c;

    // Read 1 character into c, this will block
    // forever if no character arrives.
    asio::read(port, asio::buffer(&c,1));

    port.close();

    return c;
}

string read_response()
{

    asio::io_service io;
    asio::serial_port port(io);

    port.open("COM3");
    port.set_option(asio::serial_port_base::baud_rate(115200));

    // A blocking reader for this port that
    // will time out a read after 500 milliseconds.
    blocking_reader reader(port, 5000);

    char c;

    string rsp;

    // read from the serial port until we get a
    // \n or until a read times-out (500ms)
    while (reader.read_char(c) && c != '\n') {
        rsp += c;
    }

    if (c != '\n') {
        // it must have timed out.
        throw std::exception("Read timed out!");
    }

    return rsp;
}

int main()
{
    read_char();

    return 0;
}