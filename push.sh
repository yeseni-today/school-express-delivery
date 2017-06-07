#!/bin/sh
git add .
git commit -m a
git push

if [ $? -eq 0 ]
then echo ok
else echo "git push eror, could you poll to try"
	exit 1
fi

echo push ok
ssh root@ticknick.me ./sed_deploy
