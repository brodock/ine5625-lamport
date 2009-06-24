#!/bin/bash
cd build/classes
java barbearia/IniciaCliente -ORBInitialPort 2500 -ORBInitialHost localhost $1
