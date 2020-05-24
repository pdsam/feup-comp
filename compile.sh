#!/usr/bin/env sh

set -x

java -jar comp2020-5b.jar $@ || exit

set +x

while [ $# -gt 1 ];
do
  shift
done

file=$(basename $1)
filename="${file%%.*}"

set -x

java -jar jasmin.jar $filename.jsm