#!/bin/bash
cd build/classes
java barbearia/Clientes -ORBInitialPort 2500 -ORBInitialHost localhost $1
