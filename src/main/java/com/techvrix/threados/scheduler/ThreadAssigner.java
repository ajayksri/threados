package com.techvrix.threados.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.techvrix.threados.stage.ThreadedAppStage;
import com.techvrix.threados.io.StageIOSystem;

public class ThreadAssigner {
	private StageIOSystem<ThreadedAppStage> threadQueue;
	private ExecutorService executor;
	private List<Future<Boolean>> resList;
	private Integer numThreads;
	
	public ThreadAssigner(Integer numThreads) {
		this.numThreads = numThreads;
		this.executor = Executors.newFixedThreadPool(numThreads);		
		threadQueue = new StageIOSystem<ThreadedAppStage>(1);
		resList = new ArrayList<Future<Boolean>>();
	}
	
	public void start(Map<Integer, ThreadedAppStage> stages) {
		/* numThreads >= nParallel in stage 1 + number of stages - 1 */
		int threadsConsumed = 0;
		
		for (Integer stageId : stages.keySet()) {
			Integer nParallel = stages.get(stageId).getThAppStage().getnParallel();
			if (stageId == 1) {
				startAllThreads(nParallel, stages.get(stageId));
				threadsConsumed += nParallel;
			} else {
				startAllThreads(1, stages.get(stageId));
				nParallel -= 1;
				threadsConsumed += 1;
				while (nParallel > 0) {
					threadQueue.add(stages.get(stageId));
					nParallel -= 1;
				}
			}
		}
		threadQueue.setDone();
		
		while ((threadsConsumed < numThreads) && threadQueue.hasMoreInput()) {
			call();
			threadsConsumed += 1;
		}
	}

	private void startAllThreads(Integer numParallel, ThreadedAppStage thAppStage) {
		while (numParallel > 0) {
			Future<Boolean> res = executor.submit(thAppStage);
			resList.add(res);
			numParallel -= 1;
		}
	}

	public void call() {
		if (threadQueue.hasMoreInput()) {
			ThreadedAppStage tAppStage = threadQueue.poll(1, TimeUnit.MILLISECONDS);
			if (tAppStage != null) {
				Future<Boolean> res = executor.submit(tAppStage);
				resList.add(res);
			}
		}
	}
	
	public void stop() {
		while (threadQueue.hasMoreInput()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Future<Boolean> res : resList) {
			try {
				res.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executor.shutdown();
	}
}
