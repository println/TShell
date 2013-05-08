package com;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Controller {
	private StringBuilder summary = new StringBuilder();

	public void process(Profile profile) {

		try {

			if (profile.isConcurrent())
				this.runConcurrent(profile.getTask());
			else
				this.runSequential(profile.getTask());

			System.out.println(this.summary.toString());

		} catch (IOException e) {
			System.out.println("Error ao executar comando!");
		}

		this.summary.setLength(0);
	}

	private void runConcurrent(Task[] tasks) throws IOException {
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

	private void runSequential(Task[] tasks) throws IOException {
		for (Task task : tasks)
			addResult(task.call());
	}

	private void addResult(String result) {

		if (this.summary.length() == 0)
			this.summary.append("\n\n");

		this.summary.append(result);
		this.summary.append('\n');
	}
}
