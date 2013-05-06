package com;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Controller {
	StringBuilder sumary = new StringBuilder();

	public void process(Profile profile) {
		if (profile.isConcurrent())
			this.concurrent(profile.getTask());
		else
			this.sequential(profile.getTask());

		System.out.println(this.sumary.toString());
		this.sumary.setLength(0);
	}

	private void concurrent(Task[] tasks) {
		ExecutorService executor = Executors.newFixedThreadPool(tasks.length);
		List<Task> list = Arrays.asList(tasks);

		try {
			List<Future<String>> futures = executor.invokeAll(list);
			for (Future<String> future : futures)
				addResult(future.get());
			executor.shutdownNow();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sequential(Task[] tasks) {
		for (Task task : tasks)
			addResult(task.call());
	}

	private void addResult(String result) {

		if (this.sumary.length() == 0)
			this.sumary.append("\n\n");

		this.sumary.append(result);
		this.sumary.append('\n');
	}
}
