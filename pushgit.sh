#!/bin/bash

git status
read -p "continue?(y/n)" if_commit
if [ ${if_commit} == "y" ];then
	git pull
	git add -A
	git commit -m "edit code"
	git push origin master
	exit 0
else
	echo "exiting"
	exit 0
fi

