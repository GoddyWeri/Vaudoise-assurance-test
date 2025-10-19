# HOW TO RUN PROJECT

After cloning the repository, open a terminal in the project directory. If Docker is installed, run:
- docker build -t clientapi .
- docker run -v h2data:/app/data -p 8080:8080 clientapi 
- This builds the image and runs the app, using a Docker volume (h2data) to persist the H2 database.

---

# TO TEST PROJECT(If needed)

I added a postman export to the project with the name Vaudoise-client.postman_collection.json 
(that contains a workspace for the project API testing) that can be imported in a postman app or postman website after logging in.

---

## PROOF MY API WORKS

Unit and integration tests to validate that all core functionalities work as expected were added. These tests are automatically ran when we run the docker image with command
docker run -v h2data:/app/data -p 8080:8080 clientapi, via the integrated mvn clean install, when starting the project. - The status of tests can be viewed on the console.
I created tests in the following test classes:

NB: The tests in each test class are created in the same following order

# ClientControllerTests 
- Successfully creates a client (HTTP 201)  
- Handles invalid input with a 400 Bad Request  
- Returns 404 Not Found when searching for a non-existent client  
- Returns all expected client fields when reading a client  

# ClientServiceTests  
- Validates correct client creation logic  
- Ensures a company cannot have a birthdate  
- Ensures a person cannot have a company identifier  
- Prevents duplicate client creation  
- Prevents changing the client type on update  

# ContractServiceTests  
- Sets default startDate to today if missing during contract creation  
- Returns correct contract results with update date filters  
- Returns correct contract results without update date filters  
- Validates invalid date range: updateAfter > today  
- Validates invalid date range: updateAfter > updateBefore  

# ContractRepositoryTests  
- Ensures only valid contracts (endDate > today) are returned  
- Correctly calculates total contract cost using native SQL query  
- Returns 0 cost when a client has no contracts  

# ClientRepositoryTests  
- Validates persistence of clients in the database  
- Custom finder works for exact client matches (positive cases)  
- Custom finder works for exact client matches (negative cases)  

---

## ARCHITECTURE

- I used an MVC (Model-View-Controller) architecture, suitable for managing client data simply and clearly. 
- The project is organized into folders: controllers expose APIs, services handle business logic, repositories interact with the database, and models represent entities. 
- DTOs separate input/output data to keep things clean. I applied OOP principles for modularity and maintainability.
- I used a custom annotated Exception handler to improve clarity, and cleaner coding.  
- For persistence, I used an H2 database stored locally to keep data between runs. 
- Git branches follow a feature-based workflow, enabling organized development and testing. I subdivided to show the prod->test->dev->features CI environments for development.

---

## NOTES
- For the returning of all contracts associated to client, I preferred to pass them through parameters, given there are just two filters. Could be more scalable, by using a json body instead.
- Duplicate contracts are allowed because a client can have two or more similar contracts when created the same day(startDate, endDate, updateDate and cost) but just of different types(i.e. health & car insurance). 

---