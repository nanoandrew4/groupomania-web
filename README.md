# Groupomania production website  
  
## How to run the code on your system:  
  
- Clone the repository with the following command: `git clone https://github.com/nanoandrew4/groupomania-web.git`
- Change directories into the cloned repository
- Run the following command to compile the code and create a docker image from it: `mvn clean package docker:build`
- Once the image has been successfully created, change directories into 'docker/' which is in the project root
- Once there, to start up the application and database, simply run `docker-compose build` followed by `docker-compose up -d`

### Application startup in one command:
Run the following command from the root of the application:
`mvn clean package docker:build && cd docker/ && docker-compose build && docker-compose up -d`

### Startup scripts:
Alternatively, you can use the startup scripts in the 'scripts/' directory to set up
and run the application. Once the application is up and running, simply Ctrl+C to close it,
and wait for docker compose to clean up and return you to the shell.

### Import sample data:
Optionally, to include some sample data, after the server has start up, run the 'importSampleData.sh' script, under 
the 'scripts/' directory at the root of the project, which will load some sample data into the database, such as users and sample campaigns.
This uses the psql client, which must be installed for the script to work. Otherwise, you can execute the script manually, it is located under
src/main/resources/data-postgres.sql

## Container security hardening


#### auditd
First we make sure we have auditd installed, which is used to audit records to the disk, which can be inspected at a later date using ausearch: 
`sudo apt-get install auditd`

Then we have to make sure that auditd is set up to audit docker containers:

`echo $'-w /usr/bin/docker -p wa
-w /var/lib/docker -p wa
-w /etc/docker -p wa
-w /lib/systemd/system/docker.service -p wa
-w /lib/systemd/system/docker.socket -p wa
-w /etc/default/docker -p wa
-w /etc/docker/daemon.json -p wa
-w /usr/bin/docker-containerd -p wa
-w /usr/bin/docker-runc -p wa' | sudo tee /etc/audit/audit.rules`

Then we restart the service, so that the changes take effect:
`sudo systemctl restart auditd`

#### daemon.json

Next, we are going to set up some restrictions on our docker daemon. Run the following command
to generate the daemon.json file that we will be using:

`echo $'{
    "icc": false,
    "userns-remap": "default",
    "disable-legacy-registry": true,
    "live-restore": true,
    "userland-proxy": false,
    "no-new-privileges": true,
    "storage-driver": "overlay2"
}' | sudo tee /etc/docker/daemon.json`

A short explanation of each of the flags we are setting:
- icc: Inter container communication: We disable it so that containers can't freely talk to each other, but have to be explicitly linked to one another in order to communicate
- userns-remap: Remaps the user namespace so that processes run as root inside the container (which should never happen anyways), and run as a standard user outside the container
- disable-legacy-registry: Disables the use of legacy image registries
- live-restore: Allows containers to keep running even if the docker daemon is not, which allows them to keep running during system updates, for example
- userland-proxy: Disables the userland proxy which handles port forwarding between containers and the host, and replaces it with iptables rules
- no-new-privileges: Prevents privilege escalation inside the containers
- storage-driver: Specifies the storage driver to use, 'overlay2' is preferred over the default one by the Docker documentation

Lastly, we restart the docker daemon, for the changes to take effect:
`sudo service docker restart`