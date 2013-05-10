package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;

class Task implements Callable<String> {
	private String command;
	private int timeout;

	private static int index;

	public Task(String command, int timeout) {
		this.command = command;
		this.timeout = timeout;

		if (++index > 9)
			index = 0;
	}

	public String call(){
		long elapsedTime = 0;
		boolean killed = false;
		boolean error = false;

		try {
			Process process = Runtime.getRuntime().exec(this.command);

			long now = System.currentTimeMillis();
			long timeout = 1000 * this.timeout;
			long finish = now + timeout;

			Thread consoleInput = this.inputScanning(process);
			//Thread consoleOutput = this.outputScanning(process);
			consoleInput.start();
			//consoleOutput.start();

			while (this.isAlive(process)) {
				Thread.sleep(100);
				if (System.currentTimeMillis() >= finish) {
					elapsedTime = timeout;
					killed = true;
					//consoleOutput.interrupt();
					consoleInput.interrupt();

					process.destroy();

				}
			}

			if (elapsedTime == 0)
				elapsedTime = System.currentTimeMillis() - now;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			error = true;
		}	
		return this.generateSummary(command, elapsedTime, killed,error);
	}

	@SuppressWarnings("unused")
	private Thread outputScanning(Process process) {
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		return (new Thread("writer") {
			@Override
			public void run() {
				while (!this.isInterrupted()) {
					try {
						while (!br.ready()) {
							if (this.isInterrupted())
								return;
							Thread.sleep(200);

						}
						String input;
						input = br.readLine();
						input += "\n";
						writer.write(input);
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						return;
					}
				}
			}

		});
	}

	private Thread inputScanning(Process process) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		return (new Thread("reader") {
			@Override
			public void run() {
				try {
					String input;

					while ((input = reader.readLine()) != null)
						System.out.println(input);

					reader.close();
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

	private String generateSummary(String command, long length, boolean killed, boolean err) {
		StringBuilder summary = new StringBuilder();
		summary.append('*');
		summary.append(" ");
		summary.append(command);
		summary.append(" ");
		if(!err){
			summary.append(length);
			summary.append("ms");
	
			if (killed)
				summary.append(" timeout");
		}else{
			summary.append("error!");
		}	

		return summary.toString();
	}

}
