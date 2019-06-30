# Groupomania production website  
  
## How to run the code on your system:  
  
- Clone the repository with the following command: 'git clone https://github.com/nanoandrew4/groupomania-web.git'
- Change directories into the cloned repository
- Run the following command to compile the code and create a docker image from it: 'mvn clean package docker:build'
- Once the image has been successfully created, change directories into 'docker/' which is in the project root
- Once there, to start up the application and database, simply run 'docker-compose build' followed by 'docker-compose up -d'

### Application startup in one command:
Run the following command from the root of the application:
`mvn clean package docker:build && cd docker/ && docker-compose build && docker-compose up -d`

### Import sample data:
Optionally, to include some sample data, after the server has start up, run the 'importSampleData.sh' script, under 
the 'scripts/' directory at the root of the project, which will load some sample data into the database, such as users and sample campaigns.
This uses the psql client, which must be installed for the script to work. Otherwise, you can execute the script manually, it is located under
src/main/resources/data-postgres.sql