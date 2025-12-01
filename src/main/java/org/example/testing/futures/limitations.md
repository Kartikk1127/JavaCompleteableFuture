# Java Future Limitations
In most cases, programming with Java Futures works fine as long as you do imperative style(**blocking**) of software development.  
But if your architecture is based on Reactive style(**non-blocking**) programing, then Java Futures have a limited support.  
Reactive style of software development is heavily based on **asynchronous** coding.  
Purpose of this is to improve the scalability of the application.


### Imperative Style - Pseudo Code (Blocking)
```java
// Pseudo code for handling user request

// Fetch some data from DB
data1 = FetchDataFromDB(dbUrl)

// Fetch some data from microservice 1
data2 = FetchDataFromService1(url1)

// process all data
combinedData = ProcessAndCombine(data1, data2)

// send data to user
SendData(combinedData)
```
1. The downside here is the thread blocks till all methods have finished processing.
2. In highly scalable applications, we cannot afford the threads to block as it affects scalability.
3. To rectify this, reactive style programming has become quite popular.
4. The main purpose of reactive style programming is to avoid blocking in our code.


### Reactive Style - Pseudo Code (Non-Blocking)
```java
// Reactive pseudocode for handling user request
// the user thread does not block.

Pipeline
        .Run(FetchDataFromDb(dbUrl))
        .Run(FetchDataFromService1(url1))
        .Combine(dataResult, serviceResult))
        .sendData(combinedData)

// method exits before database and service operations are completed
```
1. Reactive style pipeline is started for a user request.
2. The thread which handles the user request simply specifies what needs to be done as part of the pipeline to fulfill the user request.
3. The thread then exits without waiting for the database or the service request to complete.
4. In other words, the sole purpose of the user request thread is simply to construct the pipeline.
5. The individual parts of the pipeline would be running on separate threads and it will be the job of the pipeline manager(whoever that may be) to orchestrate these asynchronous parts and then send the combined result back to the user.
6. That's pretty much the reactive style of programming.


### Futures
1. The biggest problem of Java Futures is that it is not designed to handle reactive style of programming.
2. We cannot create a pipeline of asynchronous activity in a nice fluent way.
3. Another downside is that, there is no way to complete the future using its api.
4. The future interface does not have a method to complete it.
5. There are just a handful of methods in Future interface as compared to the CompletableFuture