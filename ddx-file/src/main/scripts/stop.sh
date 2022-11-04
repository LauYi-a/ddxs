#!/bin/sh
logserver=`ps -ef |grep ddx-file-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
kill -9 $logserver
