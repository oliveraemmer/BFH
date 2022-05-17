#!/bin/bash


# if argument docker is given
if [ "$1" == "docker" ]; then
   # check if the user is root
   if [ "$(id -u)" != "0" ]; then
      echo "This script must be run as root" 1>&2
      exit 1
   fi

   # check if docker is installed
   if [ ! -x "$(command -v docker)" ]; then
      echo "Docker is not installed" 1>&2
      # check operating system and install docker
      if [ "$(uname -s)" == "Darwin" ]; then
         echo "Installing Docker on Mac OS X"
         brew cask install docker
      elif [ "$(uname -s)" == "Linux" ]; then
         echo "Installing Docker on Linux"
         sudo apt-get update
         sudo apt-get install -y docker.io
      elif [ "$(uname -s)" == "FreeBSD" ]; then
         echo "Installing Docker on FreeBSD"
         sudo pkg install -y docker
      elif [ "$(uname -s)" == "SunOS" ]; then
         echo "Installing Docker on Solaris"
         sudo pkg install -y docker
      elif [ "$(uname -s)" == "AIX" ]; then
         echo "Installing Docker on AIX"
         sudo pkg install -y docker
      else
         echo "Unsupported operating system, please install Docker manually"
         exit 1
      fi

      # check if docker is installed
      if [ ! -x "$(command -v docker)" ]; then
         echo "Docker is not installed" 1>&2
         exit 1
      fi
   fi

   # check if docker-compose is installed
   if [ ! -x "$(command -v docker-compose)" ]; then
      echo "Docker-compose is not installed" 1>&2
      # check operating system and install docker-compose
      if [ "$(uname -s)" == "Darwin" ]; then
         echo "Installing Docker-compose on Mac OS X"
         # check if brew is installed
         if [ ! -x "$(command -v brew)" ]; then
            echo "Installing brew on Mac OS X"
            ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
         fi
         brew install docker-compose
      elif [ "$(uname -s)" == "Linux" ]; then
         echo "Installing Docker-compose on Linux"
         sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
         sudo chmod +x /usr/local/bin/docker-compose
      else 
         echo "Unsupported operating system, please install Docker-compose manually"
         exit 1
      fi

      # check if docker-compose is installed
      if [ ! -x "$(command -v docker-compose)" ]; then
         echo "Docker-compose is not installed" 1>&2
         exit 1
      fi
   fi

   # if argument stop is given, stop the containers
   if [ "$1" == "stop" ]; then
      docker-compose down
      exit 0
   fi

   # start containters
   docker-compose up -d

   # insert test data
   docker cp src/etc/db-create.sql academia-09_db_1:/
   docker cp src/etc/db-init.sql academia-09_db_1:/

   docker exec -it academia-09_db_1 psql -U postgres -d academia -f /db-create.sql
   docker exec -it academia-09_db_1 psql -U postgres -d academia -f /db-init.sql
else
   # if yarn is installed
   if [ -x "$(command -v yarn)" ]; then
      cd src/main/webapp
      yarn
      yarn build
      yarn start &
   else
      # run tests
      npm add
      npm run build
      npm run start &
   fi
fi





echo "=========================================================="
echo "Test data has been inserted into the database"
echo "=========================================================="

echo "=========================================================="
echo "Started all containers"
echo "=========================================================="