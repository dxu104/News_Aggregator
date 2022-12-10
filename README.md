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


