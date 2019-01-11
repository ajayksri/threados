package com.techvrix.threados.stage;

import com.techvrix.threados.io.IOHandle;
import com.techvrix.threados.io.StageIOSystem;

abstract public class ThreadAppStage<outputT> implements Actionable {
	private Integer stageId;
	private StageIOSystem<outputT> sios;
	private Integer nParallel;
	private IOHandle iohandle;
	
	public ThreadAppStage(Integer stageId, Integer nParallel) {
		this.stageId = stageId;
		this.sios = new StageIOSystem<outputT>(nParallel);
		this.nParallel = nParallel;
		this.iohandle = null;
	}

	public Integer getStageId() {
		return stageId;
	}

	public StageIOSystem<outputT> getSios() {
		return sios;
	}

	public IOHandle getIohandle() {
		return iohandle;
	}
	
	public void setIohandle(IOHandle iohandle) {
		this.iohandle = iohandle;
	}
	
	public Integer getnParallel() {
		return nParallel;
	}	
}
