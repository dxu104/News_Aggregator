# INFO5100_Final_NewsAgg2022

## Intro

There will be three independent parts of the project, and they locate in separate directories

## crawler

```shell
cd crawler
mvn clean package
java -jar target/crawler-1.0-SNAPSHOT-jar-with-dependencies.jar {dbPath} 
```
Notice: `{dbPath}` depend on where the sqlite database is , here I believe that it's `../news.db`

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



## NewsAggregator_JavaWeb

 NewsAggregator_JavaWeb is an independent module, which is response to receive requests from front end and response JSON object. This module archetype was maven-archetype-webapp and  developed on IntelliJ IDEA and JDK1.8. Please refer to following steps to import and run it:
1.	Please depoly apache-tomcat-9.0.69 into your computer. More details had been instructed in the last section in the INFO5100. 
2.	Import the project  named newsAggregator_JavaWeb in the GitHub root directory into your IDE. 
3.	All external libraries and plugins were downloaded by pom.XML file automatically.
4.	Please **Right-click**  `NewsAggregator` and choose **RUN MAVEN**>>**tomcat7:run** to start this module.
5.	After starting the module,pen your browse, input following three URLs:
	http://localhost:8080/newsAggregatorModule/newsList?channelID=3&pageSize=10&page=10
	http://localhost:8080/newsAggregatorModule/newsEntity?ID=10
	http://localhost:8080/newsAggregatorModule/newsSearch?keyWord=World
        http://localhost:8080/newsAggregatorModule/userFavor?userID=1&newsID=2&collect=2
        http://localhost:8080/newsAggregatorModule/newsViewCount?ID=1
        http://localhost:8080/newsAggregatorModule/isSaved?userID=20221214&newsID=7
        http://localhost:8080/newsAggregatorModule/userFavorListServlet?userID=123

6.	You were alloweed to modify parameters such as channelID, pageSize, page in the first URL, and ID(newsID) in the second URL, and keyWord in the third URL etc;
7.	If you saw JSON objects, Congratulations!



