# AmadeusProject
AmadeusFlightAvailabilityAPI

Overview: 
This project is built to access flight availabilty API of amadeus and get the response from the API , store it in an excel so that we can use that data for visualization. 
Amadeus is a third party API provider which usually provides API for travel, booking and hotel and I used Amadeus FlightAvailability API for travel. This API returns all the seats that are available to travel in a flight for a particular date chosen for a source and destination. I have captured this data for set of dates between two different destinations. This API generates client credentials which we use to communicate with the API when we pass each request. I have used Java concepts to implement this.

Prerequisites: 

You need to register in amadeus as a self-service API user so that you can get Client credentails (client key and client token) which we use to send in each request as a part of authorization.

Steps to import and run the project: 

1. Download the source cpde as a zip file 
2. import the project into eclipse or any other IDE as a gradle project 
3. Build the project as gradle project 
4. Once the build is successful run the class AllAPICalls as a java application 
5. You will be able to see the api response getting inserted into an excel in the specified path. 



