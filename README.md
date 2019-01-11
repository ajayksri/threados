# ThreadOS
ThreadOS is a java thread orchestration library.

A java developer can define various stages of a program in different java classes by extending a base class provided by frameowrk. ThreadOS takes degree of parallelism for every stage and total number of threads allocated to program as input. It runs the program by orchestrating these stages by chaining these wih provided parallelism.

ThreadOS hides complexity of thread allocation and scheduling from the developer and makes it very easy to a multi-threaded program in java.

## Example driver code -
''' Code
		ThreadedApplication myapp = new ThreadedApplication(4); /* create thread pool of size 4. */
		
		ThreadAppStage stage1 = new FileReaderThread2(1, 2);    /* Stage1 with stageId 1 and needs to run using 2 threads. */
		myapp.addStage(stage1);
		
		ThreadAppStage stage2 = new WordCountThread2(2, 3);     /* Stage2 with stageId 2 and needs to run using 3 threads. */
		myapp.addStage(stage2);
		
		ThreadAppStage stage3 = new WordCountAggregator(3, 1);  /* Stage3 with stageId 3 and needs to run using 1 thread. */
		myapp.addStage(stage3);
		
		myapp.start();
		myapp.awaitTermination();
    

## Examples
Examples are present in com.techvrix.threados.example package.
