#!/bin/bash
cmd=''
if [ "$1" = "" ] && [ "$2" = "" ];
then
  cmd="mvn clean install"
elif [ "$1" != "" ] && [ "$2" = "" ];
then
   cmd="mvn clean install -DsuiteXmlFile=$1"
elif [ "$1" != "" ] && [ "$2" != "" ];
then
   cmd="mvn clean install -DsuiteXmlFile=$1 -Dgroups=$2"
elif [ "$1" = "" ] && [ "$2" != "" ];
then
   cmd="mvn clean install -Dgroups=$2"
fi
sh "$cmd"
