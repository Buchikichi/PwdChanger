#!/bin/sh
#
# Every monday 5:55
# 55 5 * * 1 /.../PwdChanger.sh
cd `dirname $0`
export JAVA_HOME=/usr/java/default
export PATH=$JAVA_HOME/bin:$PATH
java -jar PwdChanger.jar > PwdChanger.log 2>&1
