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

#### Launching the application
After compile to package we got \*.war file, but in them, we have Apache Tomcat Embedded. That's mean we can run 
an application from command line using command: `java -jar koturno-*.war`, where \* is version compiled application. 
By default, the application will start on port 8080.

### User interface
The application delivers four main and four additional pages. 

#### Main pages
1. dashboard - where we have simple summary od inaccessibility;
2. hosts - where we have a list of all host with basic information about them and where we can add new host;
3. groups - where we have a list of all groups with information about the number of hosts in each group and where we 
can add new group;
4. history - where we have a history of all inaccessibility.

#### Additional pages
1. host details - where we can see more details and we can edit host;
2. group detail - where we can see more detail and we can edit group;
3. inaccessibility detail - where we can see more detail and we can edit description of inaccessibility;
4. ping page - where we can see the output ping program, which is run on machine, where koturno is running.

***Notes to the adding functions***

>The host has two required fields, address and group (when group isn't chosen, then host will go to default group). 
Address can be in IP format or address that can be resolved by the DNS server (if the DNS server is configured on system, 
where application is running). In group required is only name.

***Notes to the removing functions***

>Hosts can be removed anytime. Group can be removed only if is empty. If you want to remove the group with hosts you need 
to move hosts from this group to another group to make group meant to remove empty.