#!/bin/bash

# delete billing.*
rm -f billing*

# execute create database
java -jar hsqldb.jar file-billing schema-hsqldb.sql
