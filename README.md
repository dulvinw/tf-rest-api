
# Thirdfort coding challenge

### How to run

1. Clone Repo from github
2. Configure MongoDB instance
   - I have used a MongoDB instance from [MongoDB Atlas](https://cloud.mongodb.com/). You can either use that or a MongoDb instance hosted in your local computer.
     If you are using MongoDB Atlas, navigate to CONNECT -> Connect your application -> Java. From there you can copy the MongoClientURI.
   - Navigate to ```/src/main/resources/application.properties```
   - Replace ```spring.data.mongodb.uri``` property.
   - Also replace ```spring.data.mongodb.database``` with the name of your database
3. In the same properties file, update the ```tokenSecret``` property with a random string, which will be used in generating JWT token.
4. To run the server, in the the parent directory of the project, run ```./mvnw spring-boot:run```
5. Following the above steps would start the program and bind tomcat to the port 8080 by default.
   - You can additionally add a ```server.port``` property under <b>App Configurations</b> to bind to a different port. 

### API

There are two major components of the API which are identified by <b>Authentication API</b> and <b>Notes API</b>.
In order to access the Notes API, a user must be successfully authenticated using the Authentication API.

1. #### Authentication API
   Email password authentication
   
   i) Register a user.
      - <b>POST</b> <i>/users</i>
      - email: Required | string: valid email
      - password: Required | string
      
	      ###### Sample JSON
	      ```
	      {
	        "email": "you@you.com",
	        "password": "P@SsW0r>"
	      }
	      ```
    
    ii) Login using registered user. In the response header you will get the authentication token under the ```Authentication``` key. 
	  - <b>POST</b> <i>/users/login</i>
	  - email: Required | string: valid email
      - password: Required | string

		###### Sample JSON
	      ```
	      {
	        "email": "you@you.com",
	        "password": "P@SsW0r>"
	      }
	      ```

		###### Sample Response
		```
		{
			"Authentication": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzZ3J5RFkzM1FoeTRId2w4cWVmYkc2UTZkd3VVMlYiLCJleHAiOjE2MDExMjU5MjN9.6qe8QMSM9yAHCpQdRDdyPTcvuVuCunUDc68F96e8EIbW9uiKa_9_6O_8QiAxKNRUXhH2wOnVJqZhapTlry-qLg"
		}
		```

	iii) Any calls made to the ```Notes API``` must contain the ```Authentication``` header we receive from login API.

2. #### Notes API

	A note resource is identified by ```/notes/{id}```. In order to access any endpoint starting from ```/notes``` you must be authenticated using the Authentication API and the request must contain the ```Authentication``` header.

	i) Save a new note in the data store.
	- <b>POST</b> <i>/notes</i>
	- body: Required | String
	- createdDate: Required | String
	- archived: Required | Boolean
	
		###### Sample JSON
	      
	      {
	        "body": "Buy Pumpkin",
	        "createdDate": "16/09/2020>",
	        "archived": false
	      }
	      

		###### Sample Response
		```
		{
	        "body": "Buy Pumpkin",
	        "createdDate": "16/09/2020>",
	        "archived": false,
	        "notesId": "5f62104f63efe11491610230"
	    }
		```
	ii) Update a previously saved note
	-  <b>PUT</b> <i>/notes/{id}</i>
	- body : Required | String
	- archived : Required | Boolean

		###### Sample JSON
	      
	      {
	        "body": "Buy Cabbage",
	        "archived": false
	      }
	      

		###### Sample Response
		```
		{
	        "body": "Buy Cabbage",
	        "createdDate": "16/09/2020>",
	        "archived": false,
	        "notesId": "5f62104f63efe11491610230"
	    }
		```
	iii) Delete a saved note	
	-  <b>DELETE</b> <i>/notes/{id}</i>

		###### Sample Response
		```
		{
	        "operationResult": "SUCCESS",
	        "operationName": "DELETE>"
	    }
		```
	iv) Toggle archive status 
	- <b>GET</b> <i>/notes/{id}/toggleArchive</i>

		###### Sample Response
		```
		{
	        "operationResult": "SUCCESS",
	        "operationName": "TOGGLE_ARCHIVE>"
	    }
		```

	v) See all notes that aren't archived
	- <b>GET</b> <i>/notes/live</i>

		###### Sample Response
		```
		{
	        "body": "Buy Cabbage",
	        "createdDate": "16/09/2020>",
	        "archived": false,
	        "notesId": "5f62104f63efe11491610230"
	    }
		```
	vi) See all notes that are archived
	- <b>GET</b> <i>/notes/archived</i>

		###### Sample Response
		```
		{
	        "body": "Buy Carrot",
	        "createdDate": "16/09/2020>",
	        "archived": true,
	        "notesId": "5f62104f63efe11532110230"
	    }
		```


### Tech Stack

- SpringBoot - Widely supported by the community. Accredited in production environments.
- SpringSecurity - Custom built  for Spring. Easy to configure. Guided
- Spring Data Mongo -Custom built for Spring. Guided. Easy configurations
- Bcrypt - Encrypt passwords for secure storing
- MongoDB 

### Usability

- Postman collection is available [here](https://www.getpostman.com/collections/16a2375ffd34b7a0b946)

### Possible Improvements

- Implement logging in back end.
- Improve error messages.  
- Use a proper API documentation tool like [SWAGGER]([https://swagger.io/](https://swagger.io/))
- Improve unit testing
- Improve integration testing using [Rest Assured]([https://rest-assured.io/](https://rest-assured.io/))
