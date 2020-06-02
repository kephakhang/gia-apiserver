#!/bin/sh

/bin/rm -rf ./build/*

gradle -Pprofile=local build

#export PWD =`pwd`

#zip -r gia.zip Dockerfile Dockerrun.aws.json jks/key.jks build/libs/gia.jar run.sh stop.sh .ebextensions


