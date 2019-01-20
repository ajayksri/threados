package com.techvrix.threados.io;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;

public class StageIOSystem<T> {
	private volatile Integer numProducerThreads;
	private BlockingQueue<T> inputQ;
	
	public StageIOSystem(Integer nParallel) {
		numProducerThreads = nParallel;
		inputQ = new LinkedBlockingQueue<T>();
	}
	
	public Integer getNumProducerThreads() {
		return numProducerThreads;
	}

	public void setNumProducerThreads(Integer numProducerThreads) {
		this.numProducerThreads = numProducerThreads;
	}

	synchronized public void setDone() {
		numProducerThreads -= 1;
	}
	
	public Boolean add(T inRecord) {
		try {
			inputQ.put(inRecord);
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public T read() {
		if (inputQ.isEmpty())
			return null;
		
		try {
			return inputQ.poll(1, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public T poll(long time, TimeUnit tu) {
		try {
			return inputQ.poll(time, tu);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasMoreInput() {
		return !(inputQ.isEmpty() && (numProducerThreads == 0));
	}
}
