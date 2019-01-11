package com.techvrix.threados.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.techvrix.threados.io.StageIOSystem;
import com.techvrix.threados.stage.ThreadAppStage;
import com.techvrix.threados.core.ThreadedApplication;


class FileReaderThread extends ThreadAppStage<String> {
	
	public FileReaderThread(Integer stageId, Integer nParallel) {
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

class WordCountThread extends ThreadAppStage<Map<String, Integer>> {
	
	public WordCountThread(Integer stageId, Integer nParallel) {
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
		System.out.println(Thread.currentThread().getName() + "Done.");
		
		Map<String, Integer> wordCount = outSystem.read();
		for (String key : wordCount.keySet()) {
			System.out.println(key + " : " + wordCount.get(key));
		}
	 
	}

}

public class ThreadOSClient {	
	
	public static void main(String[] args) throws InterruptedException {
		
		ThreadedApplication myapp = new ThreadedApplication(5);
		
		ThreadAppStage worker = new FileReaderThread(1, 1);
		myapp.addStage(worker);
		
		ThreadAppStage worker2 = new WordCountThread(2, 1);
		myapp.addStage(worker2);
		
		myapp.start();
		myapp.awaitTermination();
	}

}
