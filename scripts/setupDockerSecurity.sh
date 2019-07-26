#!/usr/bin/env bash

printf 'Writing auditd config...\n'
echo $'-w /usr/bin/docker -p wa\n -w /var/lib/docker -p wa\n -w /etc/docker -p wa\n -w /lib/systemd/system/docker.service
       -p wa\n -w /lib/systemd/system/docker.socket -p wa\n -w /etc/default/docker -p wa\n -w /etc/docker/daemon.json -p wa\n
       -w /usr/bin/docker-containerd -p wa\n -w /usr/bin/docker-runc -p wa' | sudo tee /etc/audit/audit.rules

printf '\nWriting docker daemon config...\n'
echo $'{\n "icc": false,\n "userns-remap": "default",\n "disable-legacy-registry": true,\n "live-restore": true,\n
        "userland-proxy": false,\n "no-new-privileges": true,\n "storage-driver": "overlay2"\n}' | sudo tee /etc/docker/daemon.json

printf '\nRestarting docker service...\n'
sudo service docker restart