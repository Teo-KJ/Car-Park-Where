# Car-Park-Where

*Car Park Where* is an application developed as part of a school project on Software Engineering.

This project is jointly developed by the following members.
* [Kai Jie](https://github.com/Teo-KJ)
* [Jaslyn](https://github.com/tjaslyn)
* [Swee Sen](https://github.com/sweesenkoh)
* [Kevin](https://github.com/leady53)
* [Jordon](https://github.com/jordpkl)

## Data Support
This app is being developed by using various data from Singapore's public data portal, [Data.gov.sg](Data.gov.sg). The following are links where the data are obtained from to support the development of this application.
* [Data.gov.sg HDB Carpark Availability API](https://data.gov.sg/dataset/carpark-availability)
* [Data.gov.sg HDB Carpark Information](https://data.gov.sg/dataset/hdb-carpark-information)
* [URA Carpark Availability API](https://www.ura.gov.sg/maps/api/#car-park-available-lots)
* [URA Carpark Information](https://www.ura.gov.sg/maps/api/#car-park-list-and-rates)

## About *Car Park Where*
Want to find a lot to park, but not sure where has sufficient lots? How about planning a trip and unsure if there are enough parking spaces? The app *Car Park Where* aims to provide information on carparking availability, both live and in other days, and the carparks itself. In addition, *Car Park Where* is also a platform for motorists to save frequently visited carparks and provide reviews on carparks for other users in the community.

In summary, the application *Car Park Where* has the following purposes:
1.	Provides carpark information to allow users to better plan their journey.   
2.	Provides ease of access to frequently visited carparks.
3.	Provides platform for users to review carparks.

The following are main features of the application which will be elaborated below.
* Provide Nearby Carparks from Location
* Provide Carpark Details
* Directions to the Carpark
* Get Predicted Carpark Availability
* User Carpark Bookmark
* Carpark Reviews

## Main Features

### Provide Nearby Carparks from Location
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530042-d41cc780-80a0-11ea-90ce-caf407e2d12d.png>

When user uses phone current location feature or manually search an address of a location, the app returns a list of nearby carparks around the location for the user to select and choose from. 

### Provide Carpark Details
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/82046558-bc5a5280-96e3-11ea-833b-d8f1ef974b59.png>

User can see more detailed carpark information, with regards to its address, parking rates, full and live capacity, and the reviews made for the carpark.

### Directions to the Carpark
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530348-aa17d500-80a1-11ea-8350-5441ef866bcd.png>   <img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530429-df242780-80a1-11ea-897a-22401b784342.png>

The user can check the directions to go to the carpark from the current location. This feature is supported by Google Maps, where user can also manually adjust the departure and arrival timings, and the source location at Google Maps.

### Get Predicted Carpark Availability
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530623-75f0e400-80a2-11ea-8f5d-b6311d75a85a.png>   <img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530630-7a1d0180-80a2-11ea-8084-8ecb6277f82f.png>

With the machine learning model trained on historic carpark availability data, it is used to make predictions for the carpark at every 30 mins interval on every day of the week.

### User Carpark Bookmark
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530551-2d392b00-80a2-11ea-905f-e352598fbcdd.png>   <img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530561-36c29300-80a2-11ea-8c2c-b01bdb280084.png>

Registered user may bookmark carparks that they deem to be frequently visited. The bookmark carparks feature in the app allows the user an ease of access to the carpark information, including the live availability, and the directions.

### Carpark Reviews
<img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530603-5e196000-80a2-11ea-9afd-7f4b95079a8c.png>   <img width="260" alt="image" src=https://user-images.githubusercontent.com/48685014/79530606-62de1400-80a2-11ea-9c03-bea1e74f0ef4.png>

Registered user may make reviews about carparks, including giving a rating of 1 to 5 stars on anything they feel of the carpark. Carpark reviews can be seen by all users, both registered users and users on guest mode, so that they can take into account their parking decisions and trip planning.
