# Groupomania backend
  
## How to run the code on your system:  
  
- Clone the repository with the following command: `git clone https://github.com/nanoandrew4/groupomania-web.git`
- Change directories into the cloned repository
- Change directories into the 'scripts/' directory
- Run the build script for the environment you want to start (either dev or prod)

#### Running the development instance of the backend
The development instance simply starts creates a database container for 
the server to store the data in, and starts the server on the host machine 
(not in a container). This database is also loaded with a script containing 
sample data, located at 'src/main/resources/data-postgres.sql'. 
Starting the server on the local host and not in a container allows for
shorter start times, and remote debugging, which are two important 
elements to consider when developing locally.

#### Running a production ready instance of the backend
The startup process is as follows: the database container is started, the server 
container is started, and once both are live, a script containing sample data is 
loaded into the database. The script is located at 'src/main/resources/data-postgres.sql'

To stop the production setup, change directories to the root of the project, and then `docker/prod`.
Once here, run `docker-compose stop` in order to stop the containers, so they can later be restarted
with `docker-compose start`. To stop and delete the containers, run `docker-compose down`.

## Container security hardening

Security hardening features are setup when using the production build and
run script. Optionally, they can be run manually using the 'setupDockerSecurity.sh' script.
The following is an explanation of the contents of the script.

#### auditd
First we make sure we have auditd installed, which is used to audit records 
to the disk, which can be inspected at a later date using ausearch: 
`sudo apt-get install auditd`

Then we have to make sure that auditd is set up to audit docker containers:

`echo $'-w /usr/bin/docker -p wa\n -w /var/lib/docker -p wa\n -w /etc/docker -p wa\n -w /lib/systemd/system/docker.service
       -p wa\n -w /lib/systemd/system/docker.socket -p wa\n -w /etc/default/docker -p wa\n -w /etc/docker/daemon.json -p wa\n
       -w /usr/bin/docker-containerd -p wa\n -w /usr/bin/docker-runc -p wa' | sudo tee /etc/audit/audit.rules`

Then we restart the service, so that the changes take effect:
`sudo systemctl restart auditd`

#### daemon.json

Next, we are going to set up some restrictions on our docker daemon. Run the following command
to generate the daemon.json file that we will be using:

`echo $'{\n "icc": false,\n "userns-remap": "default",\n "disable-legacy-registry": true,\n "live-restore": true,\n
        "userland-proxy": false,\n "no-new-privileges": true,\n "storage-driver": "overlay2"\n}' | sudo tee /etc/docker/daemon.json`

A short explanation of each of the flags we are setting:
- icc: Inter container communication: We disable it so that containers 
can't freely talk to each other, but have to be explicitly linked to one another in order to communicate
- userns-remap: Remaps the user namespace so that processes run as root 
inside the container (which should never happen anyways), and run as a standard user outside the container
- disable-legacy-registry: Disables the use of legacy image registries
- live-restore: Allows containers to keep running even if the docker 
daemon is not, which allows them to keep running during system updates, for example
- userland-proxy: Disables the userland proxy which handles port forwarding 
between containers and the host, and replaces it with iptables rules
- no-new-privileges: Prevents privilege escalation inside the containers
- storage-driver: Specifies the storage driver to use, 'overlay2' is 
preferred over the default one by the Docker documentation

Lastly, we restart the docker daemon, for the changes to take effect:
`sudo service docker restart`