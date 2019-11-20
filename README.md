[![Build Status](https://travis-ci.org/sacull/koturno.svg?branch=master)](https://travis-ci.org/sacull/koturno) 
[![codecov](https://codecov.io/gh/sacull/koturno/branch/master/graph/badge.svg)](https://codecov.io/gh/sacull/koturno)

![koturno alpha logo](https://user-images.githubusercontent.com/9057882/69221770-5a77c080-0b78-11ea-867b-5833e1c2e6b5.PNG)

#### *"We will check for you if your host is working."* ;)
Koturno is a host availability checker. Simple web interface, possibility to add, modify, grouping and removing hosts, 
viewing history of inaccessibility are powers of that application. 

#### Restrictions
At the moment the user interface is only in Polish.

### Usage
After running the application we can connect with it via an internet browser. The first opened page will by a simple 
dashboard, but the interface offers three other main pages. First from them is a list of all hosts in the database, 
where we can manage hosts. Second, give us the possibility to view and manage host groups. Third shows the history of 
all inaccessibility.

### Launching the application
After compile to package we got \*.war file, but in them, we have Apache Tomcat Embedded. That's mean we can run 
an application from command line using command: `java -jar koturno-*.war`, where \* is version compiled application.

### Adding new hosts
We can add hosts in two ways. First, we can add a path to file with hosts in the command line, when we starting 
application, but if an application is running we have the second way. On the host's page, we have the possibility 
to add a new host.

In the first case, we can add more than one host, but hosts will be assigned to groups named as that hosts. Of course, 
in application, we can move the host to another group if we want that. In the second case, we can add only one host by 
form, but we can immediately assign to the target group.