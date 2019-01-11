package com.techvrix.threados.io;

public interface IOHandle {
	
	public StageIOSystem getInputSystem(Integer stageId);
	public StageIOSystem getOutputSystem(Integer stageId);
}
