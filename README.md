# aline-batch-microservice-SS : Built With Spring Batch & Spring Boot

### Description
There are many tools to use when designing and developing applications for processing transaction data, but when it comes to scalability and efficiency I trust Spring Batch. This application is designed to process millions, or even billions of transaction records. Using this application allows for enrichment of de-identified user and card data from records and even allows for analysis on the data. 

### Table of Contents
* [Installation](#installation)
* [Transactions](#transactions)
* [Usage](#usage)
* [Credits](#credits)
* [License](#license)

### Installation
1. Clone the repository.
2. Modify the application by adding your transaction records as a csv.
3. In order to use with the H2 embedded database.

### Transactions
* Your .csv file of transactions should include a header of the records columns
  * This application designed to use a transaction .csv with a header described below. 
    *  User,Card,Year,Month,Day,Time,Amount,Use Chip,Merchant Name,Merchant City,Merchant State,Zip,MCC,Errors?,Is Fraud?

### Usage
- Once the new file transaction csv file has been added:
  - If you are running from your IDE, make sure maven can build the application, then you may run the main from alinefinancialbatch\src\main\java\com\smoothstack\alinefinancial.
  - If you are running from a console, you can build and run using "mvn spring-boot:run"
  - If you'd rather run from a JAR you can use "mvn package clean" and run the JAR from the target sub directory


### Credits


### License
