# Loyalty Rewards Service

# General Info 
This is a loyalty rewards web service that accepts HTTP requests and returns responses. This web service provides routes that allow users to: 
* Add transactions for a specific payer/brand and date
* Spend points and return a list of { "payer": <string>, "points": <integer> } for each call
* Return all payer point balances.
  
# Screenshots 

  ![Screen Shot 2021-08-25 at 7 30 44 PM](https://user-images.githubusercontent.com/78180667/130877541-bfcc6173-3c06-4f73-8623-5485f29b3f68.png)
  
  ![Screen Shot 2021-08-25 at 7 33 28 PM](https://user-images.githubusercontent.com/78180667/130877860-108cb983-14e7-42d3-a32e-e58c89bfe51e.png)

  ![Screen Shot 2021-08-25 at 7 34 32 PM](https://user-images.githubusercontent.com/78180667/130877866-e742742e-53f8-4903-82b3-9720303cf03f.png)

  ![Screen Shot 2021-08-25 at 7 35 26 PM](https://user-images.githubusercontent.com/78180667/130877877-9ca9bf26-b967-40bd-84e0-ac2a2d121c25.png)

  ![Screen Shot 2021-08-25 at 7 35 43 PM](https://user-images.githubusercontent.com/78180667/130877880-5f44ba2f-8067-4d92-89e7-a53843df0c0f.png)

# Technologies
  1) Java programming language
  2) REST API
  3) Spring Boot
  4) JUnit
  5) Hamcrest
  6) Maven

# Setup
* git clone https://github.com/AcronicalStar/points-service.git
* Open up a shell of your choice
* Install maven: this link has clear instructions for each OS - https://maven.apache.org/install.html
* Make sure to have maven in your path: Type mvn -v
* Build and install the project jar. At the command prompt, type: mvn install
* Make sure that you have the artifact in the target folder: Type ls target and you should see rest-service-initial-0.0.1-SNAPSHOT.jar
* Start up the server:  Type. java  -jar target/rest-service-initial-0.0.1-SNAPSHOT.jar
* Install postman
* Start postman, skip the signing in part.
* Create collection
* Add request
* Label your request so you. can save it for more requests
* Test all the api's, just as supplied in the exercise
  
To add a new transaction:
 * Select POST for method type
 * Type in http://localhost:8080/add
 * Select JSON for data type
 * In the body type: { "payer": "NARS", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
 * Click send

Additional data:
* { "payer": "GUERLAIN", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
* { "payer": "NARS", "points": 200, "timestamp": "2020-10-31T11:00:00Z" }
* { "payer": "NARS", "points": -200, "timestamp": "2020-10-31T15:00:00Z" }
* { "payer": "YSL", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" }
* { "payer": "GUERLAIN", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }

To spend points:
* Select POST for method type
* Type in http://localhost:8080/spend-points for. URL
* Select JSON. for data type
* In the body type. { "points": 5000 }
  
To get balance:
* Select GET for method type
* Type http://localhost:8080/get-balance

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
* Transaction
  * A JavaBean that represents transactions. Each transaction contains a payer, points, and timestamp for the transaction.
* TransactionController
  * The controller class for the REST API endpoints. 

# Status
Completed


