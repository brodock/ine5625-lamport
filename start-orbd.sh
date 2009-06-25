#!/bin/bash
killall orbd
rm -rf orb.db
orbd -ORBInitialPort 2500
