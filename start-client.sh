#!/bin/bash
clear
cd build/classes
java barbearia/IniciaCliente -ORBInitialPort 2500 -ORBInitialHost localhost $1
