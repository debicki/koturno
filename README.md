[![Build Status](https://travis-ci.org/sacull/koturno.svg?branch=master)](https://travis-ci.org/sacull/koturno) 

[![koturno](https://user-images.githubusercontent.com/9057882/72901019-46dc0800-3d29-11ea-8e32-43004957ad6f.png)](https://github.com/sacull/koturno)

#### *"We will check for you if your host is working."* ;)
Koturno is a host availability checker. Simple web interface, possibility to add, modify, grouping and removing hosts, 
viewing history of inaccessibility are powers of that application. 

#### Restrictions
At the moment the user interface and user manual are only in Polish.

### Usage
After running the application we can connect with it via an internet browser. The first opened page, after register 
and login, will by a simple dashboard, but the interface offers three other main pages. First from them is a list of 
all hosts in the database, where we can manage hosts. Second, give us the possibility to view and manage host groups. 
Third shows the history of all inaccessibility.

#### Database
On default, application use H2 in-memory database, but it can use MySQL database too. To use MySQL comment H2 row 
and uncomment MySQL rows, with your login and password to database before compile.

#### Launching the application
After compile to package we got \*.war file, but in them, we have Apache Tomcat Embedded. That's mean we can run 
an application from command line using command: `java -jar *.war`, where \* is name of the application. 
By default, the application will start on port 8080.

### User interface
The application delivers four main and four additional pages. 

#### Main pages
1. dashboard - where we have simple summary od inaccessibility;
2. hosts - where we have a list of all host with basic information about them and where we can add new hosts;
3. groups - where we have a list of all groups with information about the number of hosts in each group and where we 
can add new groups;
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

>Hosts can be removed anytime. Group can be removed only if is empty.

***Notes to the import functions***

>Hosts can be import from text file. Application can use two formats of import files, TXT and CSV files. Look to 
'examples' folder to check how to use them.

For more information look to 'manuals' folder, where you can find user manual. Unfortunately user manual already is only 
in Polish.