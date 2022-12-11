# INFO5100_Final_NewsAgg2022

## Intro

There will be three independent parts of the project, and they locate in separate directories

## crawler

```shell
cd crawler
mvn clean package
java -jar target/crawler-1.0-SNAPSHOT.jar {dbPath} 
```
Notice: {dbPath} depend on where the sqlite database is , here I believe that it's `../newsAggregatorModule/src/main/resources/news.db`

then you should see the output from terminal:
```shell
022-12-09 21:52:15 [main] org.xiao.Timer.start()
INFO: after every network connection we will sleep for 1 seconds
2022-12-09 21:52:16 [main] org.xiao.db.DataHandler.connect()
INFO: Opened database successfully
2022-12-09 21:52:16 [main] org.xiao.Timer.startOnce()
INFO:
check and update channel: 1
```

It means the crawler is working! Congratulations!


## News Aggregator

It is an independent application called News Aggregator, which is developed on IntelliJ Idea and JDK18. Here is some instructions to help you import and run it:
+ Import the project in the folder called  `newsAggregator`  into your ide. IntelliJ Idea is recommended.
	+ If there is a popup window asking you to choose the type of the project when you try to import the project, please choose it as a  `JavaFX`  project.
+ The project will take some time when it is being imported, as it was developed basded on JavaFx and hence jar libraries are placed in a directory called **lib**.
+ After the process of importing is finished, please Right-click the directory  `lib`, and choose  `Add As Library`.
+ Ide would automatically download libraries from **Maven** which have been set up in the `pom.xml`. If some libraries could not downloaded successfully, please **Right-click** the `pom.xml` and choose these options: `Maven >> Reload project`, it should work normally.
+ Finally, when you move to this step, that means all required libraries are settled. Then, please **Right-click** the main class, `MainApplication` , to run the application, here is its whole path for your conference, `src >> main >> java >> com >> example >> newsaggregator >> ui >> MainApplication`.  Now, it should work successfully!



