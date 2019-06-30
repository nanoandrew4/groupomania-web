#!/bin/sh

# First we set the password as set in the postgres Dockerfile, so that it doesn't prompt for it
export PGPASSWORD='supersecretpassword'

# Then we run the script to insert data into the database
psql --echo-all --host=localhost --port=5432 --dbname=groupomania --username=greenapperdba -f "../src/main/resources/data-postgres.sql"