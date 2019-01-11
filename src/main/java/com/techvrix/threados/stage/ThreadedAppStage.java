package com.techvrix.threados.stage;

import java.util.concurrent.Callable;

import com.techvrix.threados.scheduler.ThreadAssigner;

public class ThreadedAppStage implements Callable<Boolean> {
	private ThreadAppStage thAppStage;
	private ThreadAssigner thAssignerCallback;

	public ThreadedAppStage(ThreadAppStage thAppStage, ThreadAssigner thAssignerCallback) {
		super();
		this.thAppStage = thAppStage;
		this.thAssignerCallback = thAssignerCallback;
	}

	public ThreadAppStage getThAppStage() {
		return thAppStage;
	}

	public void setThAppStage(ThreadAppStage thAppStage) {
		this.thAppStage = thAppStage;
	}

	public ThreadAssigner getThAssignerCallback() {
		return thAssignerCallback;
	}

	public void setThAssignerCb(ThreadAssigner thAssignerCallback) {
		this.thAssignerCallback = thAssignerCallback;
	}

	public Boolean call() throws Exception {
		thAppStage.execute();
		thAssignerCallback.call();
		
		return true;
	}
}
