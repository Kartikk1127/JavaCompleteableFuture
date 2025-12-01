Once you start a thread with the current thread, 
you may want to join it with the new thread again at a later point in time. 
Join makes the current thread wait till the new thread terminates.


There are two types of threads:
1. Platform threads :
   1. Expensive resource
   2. By default, a platform threads takes about 1 mb of memory.
   3. Creating platform threads as and when we like is discouraged, solution is to create a thread pool.
   4. Tomcat by default, creates 200 dedicated threads for user requests.
   5. Instead of thinking like - create a new thread for a particular task, think like submit a particular task to the thread pool[Thinking in this way allows us to separate the task from how it will be executed]
   6. This is known as the execution policy of the task which is essentially the idea behind Java Futures.
   7. Future is basically the logical reference returned by the thread pool to the executing task.
   8. The task submission doesn't wait but simply queues the request for the thread pool and returns back immediately with the future.
   9. Any java class that implements (Runnable/Callable) is considered a task. 
   10. Java's ExecutorService is an interface that represents the abstraction of a thread pool.
   11. Applications use Executor Service to submit a runnable/callable task to the thread pool.
   12. Runnable doesn't return anything(void run()) and throws no checked exception whereas callable does return something(V call() throws Exception) and also throws a checked exception.
   13. 
2. Virtual threads# JavaCompleteableFuture
