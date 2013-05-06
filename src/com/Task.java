package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

class Task implements Callable<String> {
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

	public String call() {
		long elapsedTime = 0;
		boolean killed = false;
		
		try {
			Process process = Runtime.getRuntime().exec(this.command);
			InputStreamReader ins = new InputStreamReader(process.getInputStream());
			BufferedReader in = new BufferedReader(ins);

			long now = System.currentTimeMillis();
			long timeout = 1000 * this.timeout;
			long finish = now + timeout;

			Thread console = this.consoleText(process,in);
			console.start();			
			
			while (this.isAlive(process)) {
				Thread.sleep(100);				 
				if (System.currentTimeMillis() >= finish){
					elapsedTime = timeout;
					killed = true;
					console.interrupt();
					process.destroy();					
				}
			}
			
			if(elapsedTime == 0)
				elapsedTime = System.currentTimeMillis() - now;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.generateSummary(command, elapsedTime, killed);

	}

	private Thread consoleText(final Process process, final BufferedReader in) {
		return (new Thread() {
			@Override
			public void run() {
				try {
					String output;

					while ((output = in.readLine()) != null) {
						System.out.println(output);
					}

					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private boolean isAlive(Process process) {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
	
	private String generateSummary(String command, long duration, boolean killed){
		StringBuilder summary = new StringBuilder();
		summary.append('*');
		summary.append(" ");
		summary.append(command);		
		summary.append(" ");
		summary.append(duration);
		summary.append("ms");
		summary.append(" ");
		summary.append(killed ? "timeout" : "executou");
		return summary.toString();
	}

}
