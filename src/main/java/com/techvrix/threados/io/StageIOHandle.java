package com.techvrix.threados.io;

public class StageIOHandle {
	private StageIOSystem inputSystem;
	private StageIOSystem outputSystem;
	
	public StageIOHandle(StageIOSystem inputSystem, StageIOSystem outputSystem) {
		this.inputSystem = inputSystem;
		this.outputSystem = outputSystem;
	}
	
	public StageIOSystem getInputSystem() {
		return inputSystem;
	}
	
	public void setInputSystem(StageIOSystem inputSystem) {
		this.inputSystem = inputSystem;
	}
	
	public StageIOSystem getOutputSystem() {
		return outputSystem;
	}
	
	public void setOutputSystem(StageIOSystem outputSystem) {
		this.outputSystem = outputSystem;
	}	
}
