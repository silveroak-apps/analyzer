#!/bin/bash

# Check if Analyzer is running
if ps -ef | grep "Analyzer" > /dev/null
then
    echo "0"
else
    echo "Stopped exiting bot"
    kill -s SIGTERM 1
    kill -s SIGKILL 1
fi
