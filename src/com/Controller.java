package com;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Controller {
	public void process(Profile profile) {
		if (profile.isConcurrent())
			this.concurrent(profile.getTask());
		else
			this.sequential(profile.getTask());
	}

	private void concurrent(Task[] tasks) {
		ExecutorService executor = Executors.newFixedThreadPool(tasks.length);
		List<Task> list = Arrays.asList(tasks);
		
		try {
			List<Future<Boolean>> futures = executor.invokeAll(list);
			for(Future<Boolean> future : futures)
		        future.get();
		        
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sequential(Task[] tasks) {
		for(Task task:tasks)
			task.call();
	}

}
