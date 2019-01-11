package com.techvrix.threados.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.techvrix.threados.io.StageIOSystem;
import com.techvrix.threados.stage.ThreadAppStage;
import com.techvrix.threados.core.ThreadedApplication;


class FileReaderThread2 extends ThreadAppStage<String> {
	
	public FileReaderThread2(Integer stageId, Integer nParallel) {
		super(stageId, nParallel);
	}

	public void execute() {
		StageIOSystem<String> outSystem = (StageIOSystem<String>) getIohandle().getOutputSystem(getStageId());
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("D:\\python\\testdata.txt"));
		
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String [] words = line.split("\\s+");
				for (String aword : words) {
					outSystem.add(aword);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		outSystem.setDone();
		System.out.println(Thread.currentThread().getName() + "Done.");
	}

}

class WordCountThread2 extends ThreadAppStage<Map<String, Integer>> {
	
	public WordCountThread2(Integer stageId, Integer nParallel) {
		super(stageId, nParallel);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		StageIOSystem<String> inSystem = (StageIOSystem<String>) getIohandle().getInputSystem(getStageId());
		StageIOSystem<Map<String, Integer>> outSystem = (StageIOSystem<Map<String, Integer>>) getIohandle().getOutputSystem(getStageId());
		
		System.out.println(Thread.currentThread().getName());
		
		while (inSystem.hasMoreInput()) {
			String word = inSystem.read();
			if (word != null) {
				Integer count = countMap.get(word);
				if (count == null)
					countMap.put(word, 1);
				else
					countMap.put(word, count+1);
			}
		}
		outSystem.add(countMap);
		outSystem.setDone();	 
	}

}

class WordCountAggregator extends ThreadAppStage<Map<String, Integer>> {
	
	public WordCountAggregator(Integer stageId, Integer nParallel) {
		super(stageId, nParallel);
		// TODO Auto-generated constructor stub
	}

	
	public void execute() {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		StageIOSystem<Map<String, Integer>> inSystem = (StageIOSystem<Map<String, Integer>>) getIohandle().getInputSystem(getStageId());
		StageIOSystem<Map<String, Integer>> outSystem = (StageIOSystem<Map<String, Integer>>) getIohandle().getOutputSystem(getStageId());
		
		System.out.println(Thread.currentThread().getName());
		
		while (inSystem.hasMoreInput()) {
			Map<String, Integer> wordCountMap = inSystem.read();
			if (wordCountMap != null) {
				for (String key : wordCountMap.keySet()) {
					Integer count = countMap.get(key);
					if (count == null)
						countMap.put(key, wordCountMap.get(key));
					else
						countMap.put(key, count+wordCountMap.get(key));
				}
			}
		}
		outSystem.add(countMap);
		outSystem.setDone();
		System.out.println(Thread.currentThread().getName() + "Done.");
		
		Map<String, Integer> wordCount = outSystem.read();
		for (String key : wordCount.keySet()) {
			System.out.println(key + " : " + wordCount.get(key));
		}
	 
	}

}
public class ThreadOSClient2 {	
	
	public static void main(String[] args) throws InterruptedException {
		
		ThreadedApplication myapp = new ThreadedApplication(5);
		
		ThreadAppStage worker = new FileReaderThread2(1, 2);
		myapp.addStage(worker);
		
		ThreadAppStage worker2 = new WordCountThread2(2, 3);
		myapp.addStage(worker2);
		
		ThreadAppStage worker3 = new WordCountAggregator(3, 1);
		myapp.addStage(worker3);
		
		myapp.start();
		myapp.awaitTermination();
	}

}
