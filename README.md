eri# Points Service

# General Info 
This is a Points web service that accepts HTTP requests and returns responses. This web service provides routes that allow users to: 
* Add transactions for a specific payer and date
* Spend points and return a list of { "payer": <string>, "points": <integer> } for each call
* Return all payer point balances.
  
# Problem Statement
  Our users have points in their accounts. Users only see a single balance in their accounts. But for reporting purposes we actually track their
points per payer/partner. In our system, each transaction record contains: payer (string), points (integer), timestamp (date).
For earning points it is easy to assign a payer, we know which actions earned the points. And thus which partner should be paying for the points.
When a user spends points, they don't know or care which payer the points come from. But, our accounting team does care how the points are
spent. There are two rules for determining what points to "spend" first:
● We want the oldest points to be spent first (oldest based on transaction timestamp, not the order they’re received)
● We want no payer's points to go negative.
We expect your web service to
Provide routes that:
● Add transactions for a specific payer and date.
● Spend points using the rules above and return a list of { "payer": <string>, "points": <integer> } for each call.
● Return all payer point balances.
Note:
● We are not defining specific requests/responses. Defining these is part of the exercise.
● We don’t expect you to use any durable data store. Storing transactions in memory is acceptable for the exercise.
Example
Suppose you call your add transaction route with the following sequence of calls:
● { "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
● { "payer": "UNILEVER", "points": 200, "timestamp": "2020-10-31T11:00:00Z" }
● { "payer": "DANNON", "points": -200, "timestamp": "2020-10-31T15:00:00Z" }
● { "payer": "MILLER COORS", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" }
● { "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }
Then you call your spend points route with the following request:
{ "points": 5000 }
The expected response from the spend call would be:
[
{ "payer": "DANNON", "points": -100 },
{ "payer": "UNILEVER", "points": -200 },
{ "payer": "MILLER COORS", "points": -4,700 }
]
A subsequent call to the points balance route, after the spend, should returns the following results:
{
"DANNON": 1000,
"UNILEVER": 0,
"MILLER COORS": 5300
}
# Screenshots 

![Zip Archiver](https://user-images.githubusercontent.com/49923044/124396802-d08f5380-dcd9-11eb-808d-db34fbcc4023.jpg)

![Screenshot from 2021-06-23 16-21-43](https://user-images.githubusercontent.com/49923044/123165900-cc924480-d442-11eb-8767-a86a84af1140.png)

![Screenshot from 2021-06-23 16-41-43](https://user-images.githubusercontent.com/49923044/123165920-d1ef8f00-d442-11eb-8e48-71ae08c32874.png)

![Screenshot from 2021-06-23 16-44-27](https://user-images.githubusercontent.com/49923044/123165938-d6b44300-d442-11eb-9ae8-6f9e9854ba16.png)

![Screenshot from 2021-06-23 16-45-35](https://user-images.githubusercontent.com/49923044/123165949-d87e0680-d442-11eb-90e8-387b806f37d0.png)

![Screenshot from 2021-06-23 16-46-22](https://user-images.githubusercontent.com/49923044/123165956-dae06080-d442-11eb-8c84-1c4d38e1f0c7.png)

![Screenshot from 2021-06-23 16-47-03](https://user-images.githubusercontent.com/49923044/123165961-dcaa2400-d442-11eb-9e21-98a2d5d499c5.png)

![Screenshot from 2021-06-23 16-47-22](https://user-images.githubusercontent.com/49923044/123165980-dfa51480-d442-11eb-8681-b1304e7491f0.png)

# Technologies
  1) Java programming language
  2) REST API
  3) Spring Boot
  4) JUnit
  5) Hamcrest
  6) Maven

# Setup
  1) Install Maven 
    * For Windows: Download maven and extract it in a location of your choice. Set the M2_HOME and MAVEN_HOME variable to maven installation folder. Update the PATH variable with 'Maven-installation/bin' directory. Verify maven in console by typing mvn -version.
    * For Mac: 
    * For Ubuntu: In the terminal type either sudo apt-get install mvn or sudo apt-get install maven

# Features
* Main
  * Main class which starts up the Points Service
* Points
  * A JavaBean which represents points
* PointsAfterSpending
  * The PointsAfterSpending class is a JavaBean that contains the payer and points. It is used to store the number of points left after the spendPoints endpoint is called.
* PointsManager
  * Contains all the logic for spending points and getting point balance for all payers.
  * Add transaction: A method used to add the specified transaction to the list of transactions
  * Spend Points: This method contains the logic for spending points. It takes into account the requirements which state that the oldest points must be spend first and that no payer's points go negative.
  * Get balance: This method returns a map. The key is payer and the value is the point balance for the payer.
* File Properties
  * Represents the properties of a file such as name, size, compressed size, and compression method
  * Primarily used by the ZipContentCommand class to display file properties for each file in the archive
* Zip File Manager
  * Controller class that contains the logic for each of the zip commands. For example ZipAddCommand.java adds an entry into the archive by instantiating a ZipFileManager object and calling the addFile() method. 
* Commands
  * There is a command for each user intended operation. All commands create a zip file manager and execute the appropriate method from the ZipFileManager class to perform the operation
  * Zip add command: Prompts the user for the full path of the file to be added and adds the file to the archive
  * Zip content command: Gets a list of all the files in the archive and uses the FileProperties class to output the file properties to the console
  * Zip create command: Prompts the user for the file or directory to be archived and creates a new archive at that destination
  * Zip extract command: Prompts the user for the path where the archive will be unpacked and extracts the contents of the archive into the destination
  * Zip remove command: Prompts the user for the full path of the file to be removed from the archive and removes the file from the archive
  * Zip exit command: Exits the program

# Status
Completed

# Inspiration


