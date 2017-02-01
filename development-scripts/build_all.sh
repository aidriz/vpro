#!/bin/bash
GREEN='\e[42m\e[1;30m'
YELLOW='\e[93m'
RED='\e[31m'
NC='\033[0m' # No Color
CURDIR=$(pwd);



function _exitOnError {
	printf "${RED}Installation script exit on ERROR ${YELLOW} $1 ${NC}\n";
	printf "${RED}Press [ENTER] to exit${NC}\n";
	read -p "" empty
	exit -1;
}
           
function _warn {
	printf "${YELLOW}Non critical error: $1 ${NC}\n";
	printf "${YELLOW}Press [ENTER] to continue${NC}\n";
	read -p "" empty
}

function _goToCWD {
	cd ${CURDIR};
}

_goToCWD;

# Building Modules
cd ..
for project in vpro-utils/java vpro-sr vpro-userinterface vpro-VU SpeechAPIDemo
do
    printf "${GREEN}Building  ${project}${NC}\n"
    cd ${project}
    if [[ $? != 0 ]]; then _warn "Fail to locate ${project}"; 
    else
	mvn -q clean
	mvn -q $1 install
	if [[ $? != 0 ]]; then _warn "Fail to build ${project}"; fi
	if [ -e ./postBuild.sh ]; then
	    printf "${GREEN}Running post-building script for ${project}${NC}\n"
	    chmod a+x ./postBuild.sh
	    ./postBuild.sh
	    if [[ $? != 0 ]]; then _warn "Fail to complete post-installation ${project}"; fi
	fi    
	_goToCWD;
	cd ..
    fi
done
	
printf "${GREEN}FINISHED${NC}\n"

