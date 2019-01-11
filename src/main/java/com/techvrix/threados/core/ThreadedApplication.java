package com.techvrix.threados.core;

import java.util.HashMap;
import java.util.Map;

import com.techvrix.threados.stage.ThreadAppStage;
import com.techvrix.threados.stage.ThreadedAppStage;
import com.techvrix.threados.io.IOHandle;
import com.techvrix.threados.io.StageIOHandle;
import com.techvrix.threados.io.StageIOSystem;
import com.techvrix.threados.scheduler.ThreadAssigner;

public class ThreadedApplication implements IOHandle {
	
	Integer numThreads;
	Map<Integer, ThreadedAppStage> stages; /* Change this to sorted list. */
	Map<Integer, StageIOHandle> stageIOs;
	ThreadAssigner threadAssigner;
	
	public ThreadedApplication(Integer numThreads) {
		
		this.numThreads = numThreads;
		stages = new HashMap<Integer, ThreadedAppStage>();
		stageIOs = new HashMap<Integer, StageIOHandle>();
		threadAssigner = new ThreadAssigner(numThreads);
	}

	public void addStage(ThreadAppStage tappStage) {
		StageIOSystem inSys = null;
		StageIOSystem outSys =null;
		
		stages.put(tappStage.getStageId(), new ThreadedAppStage(tappStage, threadAssigner));
		outSys = tappStage.getSios();
		
		Integer previousStageId = tappStage.getStageId() - 1;
		if (stages.get(previousStageId) != null) {
			inSys = stages.get(previousStageId).getThAppStage().getSios();
		}
		stageIOs.put(tappStage.getStageId(), new StageIOHandle(inSys, outSys));
		tappStage.setIohandle(this.getIOHandle());
	}
	
	public StageIOSystem getInputSystem(Integer stageId) {
		return stageIOs.get(stageId).getInputSystem();
	}

	public StageIOSystem getOutputSystem(Integer stageId) {
		return stageIOs.get(stageId).getOutputSystem();
	}

	public IOHandle getIOHandle() {
		return this;
	}
	
	public void start () {
		threadAssigner.start(stages);
	}
	
	public void awaitTermination() {
		threadAssigner.stop();
	}
	
	public void printStageIOs() {
		for (Integer key: stageIOs.keySet()) {
			System.out.println(key);
			System.out.println(stageIOs.get(key).getInputSystem());
			System.out.println(stageIOs.get(key).getOutputSystem());
		}
	}
}
