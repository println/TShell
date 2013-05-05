package com;

import java.io.IOException;
import java.util.concurrent.Callable;

class Task implements Callable<Boolean> {
	private String command;
	private int timeout;

	private static int index = 0;

	public Task(String command, int timeout) {
		this.command = command;
		this.timeout = timeout;

		index++;
		if (index > 9)
			index = 0;
	}

	public Boolean call() {
		try {
			Process process = Runtime.getRuntime().exec(this.command);

			long now = System.currentTimeMillis();
			long timeout = 1000 * this.timeout;
			long finish = now + timeout;

			while (this.isAlive(process)) {
				Thread.sleep(100);
				if (System.currentTimeMillis() >= finish)
					process.destroy();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
	}

	private boolean isAlive(Process process) {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	
}
