# CompletableFuture in Java

Servlets do not mandate that the user request threads be alive when the final data is sent back to the user.  
This has opened the doors for reactive style of programming.  
Java CompletableFuture is equivalent to Javascript's promises.


### Completable Future Pipeline

```java
import org.example.testing.futures.callable.TaskResult;
import java.util.concurrent.CompletableFuture;

// Tasks to execute in parallel
Supplier<TaskResult> task1 = () -> doTask("task1", 3, false);
Supplier<TaskResult> task2 = () -> doTask("task2", 5, false);

// CompletableFuture static method supplyAsync() will start Task1 asynchronously
// this method will return immediately after initiating the task1 in a separate thread
// task2 is also initiated in the same way. 2 tasks have been started asynchronously.
// once the two tasks have completed successfully, the lambda function specified in the thenCombine method is called to handle the result(result1 and result2)
// the returned data from the lambda function is then fed as input to the next stage i.e. thenApply() stage
// finally the thenAccept consumer is called to handle the final result.
// the final string printed is shown as Result
// each stage adds something to the string
// output of the previous stage is an input to the next stage
// the code below simply specifies the stages of the pipeline but does not trigger all the stages
// for each stage it specifies the code to be executed in the form of a supplier or a function or a by-function or a consumer
// when a stage is triggered, the code associated with the stage is run
// think about this entire pipeline as a specification for orchestrating the results coming from the asynchronous tasks(1 and 2)
// the pipeline itself is broken up into different stages where the data is transformed, combined or accepted
// so when the control passes to the next statement i.e. do something else, the only thing guaranteed is that the two tasks have started executing in their own thread in the pipeline created
// there is no guarantee that the pipeline has been fully executed. In fact, the chances are very less that the pipeline has fully executed.
// the thread which created the pipeline can be released back to the thread pool or terminated. It doesn't have to block on anything(like result of the pipeline)
// in our case when both the tasks have completed(after 5 seconds since both are running concurrently), the thenCombine method is triggered.
// once completed, thenApply is triggered and so on till the whole pipeline finishes execution.
// The final result can then be used to do whatever required.
// If you call the join method on the pipeline, then it will wait for the pipeline to complete(becomes blocking)
// in typical case it's usually a part of the app server, say spring boot controller, then the completable future can be returned to the app server for it to get the result and send back to the user
// completable future implements both Future and CompletionStage interface. 
// since it implements future interface, you also get a get() method to block as well. This of course is not encouraged.
// much of the pipelining ability of the completable future comes from CompletionStage interface
CompletableFuture.supplyAsync(task1))
        .thenCombine(
                CompletableFuture.supplyAsync(task2),
                (result1, result2) -> String.format("Combined (%s : %s)", result1.taskName(), result2.taskName()))
        .thenApply(data -> data + " :: Handled Apply")
        .thenAccept(data -> {
            System.out.println(data + " :: Handled Accept");
        })

// do something else
```
```java
Result = Combined(task1 : task2) :: Handled Apply :: Handled Accept
```