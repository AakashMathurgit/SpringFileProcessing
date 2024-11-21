
@Instructions to compile in case we face any issue.
The project is made using springBoot, please install maven and pass maven home if any issue is seen while running the start_driver.sh and start_engine.sh.

start_driver.sh 9001, 9002 --mh="PATH OF MAVEN HOME" --jh="PATH OF JAVA HOME"

Please use the Mod_test_runner.py if needed to specify the mh and jh values.


@Project Architecture
The student detail's analyzing program contains following components :

Driver : 
The Driver is responsible of distributing the data on which the engine should work on. 
The Driver tries to distribute the fileName in a balanced way, If an engine goes down, driver takes care of that also by shifting load to other servers and also keep trying for the engine which is down incase it is available later.
The Driver finally receives the response for each file and merge the data and create the final output.
The final output is then written to the file.

Engine : 
The Engine is responsible to process the file specified by the Driver.
The Engine has a request Queue in which all the pending requests are stored and processed from.
The Engine reads the csv files line by line and get important information from it.
Finally it returns the information back to the driver.


@Source Code Layout:
.
└── Aakash_Mathur/
    └── sourceCode/
        ├── DriverProjectE6/
        │   ├── src/main/java/
        │   │   └── com.aakasmat.driverProjectE6/
        │   │       ├── DriverApiService.java #Bean to send data to engines
        │   │       ├── DriverLogic.java #Bean responsilbe for all calculations, merging etc.
        │   │       ├── DriverController.java #RestApi endpoints used by Engines to send data
        │   │       ├── DriverProjectE6Application.java #Entry point to the program
        │   │       └── FileData.java #A FileData entity
        │   └── src/main/resources/
        │       └── aaplication.properties
        └── EngineProjectE6-1/
            ├── src/main/java/
            │   └── com.aakasmat.EngineProjectE6/
            │       ├── EngineApiService.java #Bean to send data to driver.
            │       ├── RequestQueue.java #RequestQueue saves all pending requests
            │       ├── EngineController.java #RestApi endpoints used by driver to send data
            │       ├── EngineProjectE61Application.java #Entry point to the program
            │       ├── FileData.java # FileData entity
            │       └── EngineProcessor.java #Main processing entity
            └── src/main/resources/
                └── aaplication.properties

@Design Notes:

A. Driver
1. DriverProjectE6Application.java => 
	Entry point to the program.
	Processes the port details passed.
	Calls the driver decision making method from DriverLogic.
2. DriverLogic.java =>
	Bean responsible to manage all decision making.
	Splits the total files in a balanced manner and pass the request to process to the engines.
	Decide which engine to use, Handles cases if a certain engine is not responding. (Diverts the traffic to healthy engines.)
	Merges the final output and writes to the file.
3. DriveApiService.java =>
	Serivce with methods to pass information to the Engines.
4. FileData.java =>
	Entity containing the analysis of a file.
5. DriveController => 
	RestApi endpoints used by engines to send data.

B. Engine:
1. EngineProjectE61Application.java=>
	Entry point to the program
	Calles the engine processor method 
2. EngineProcessor.java =>
	Consumes the requestQueue.
	For each request, reads the assositated file.
	Generates the analysis of it.
	Sends data to driver.
3. EngineApiSerivce.java =>
	Serivce with methods to call api of driver.
4. FileData.java =>
	Entity containing the analysis of a file.
5. RequestQueue.java =>
	A queue  holds the pending requests.
6. EngineController.java =?
	Contains rest api used by drive to create a request.

Design Decisions:
Driver: 
1. The driver uses concurrent hashMap filesProcessed and filesNotProcessed to keep track of files.
2. filesProcessed stores fileData information of each file.
3. fileData is the summary of file useful to driver.
4. filesNotProcessed keeps track of files which are to be processed yet.
5. Implemented balancing.
6. Incase a port is down, traffic is moved to available engines.
7. A faulttolreance is defined, if more requests fails than this, the server will be considered down and less/no traffic will go to it.

Engine:
1. The engine has a hashMap filesProcessed which keeps track of files processed by it.
2. Incase a file was processed but fails to be recived by driver, driver requests again and we can directly send it from filesProcessed hashMap. Saving computation power.
3. Process the csv file and loop on each row, calculating useful information as required.

Using rest api for inter service communication, websocket could also be used but the communication is not that high demanding, processing is more demanding.



