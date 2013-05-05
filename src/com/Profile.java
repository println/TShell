package com;

class Profile {
	private String command;
	private int loop;
	private int timeout;
	private boolean mode;

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isConcurrent() {
		return mode;
	}
	public boolean isMulti() {
		return (this.getCommands().length > 1);
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Task[] getTask() {

		String[] cmdtemp = this.getCommands();

		String[] cmds;

		if (cmdtemp.length == 1 && loop > 1) {
			cmds = new String[loop];
			for (int i = 0; i < loop; i++)
				cmds[i] = cmdtemp[0];
		} else {
			cmds = cmdtemp;
		}

		Task[] tasks = new Task[cmds.length];

		for (int i = 0; i < cmds.length; i++)
			tasks[i] = new Task(cmds[i], this.timeout);

		this.reset();

		return tasks;
	}
	
	private String[] getCommands(){
		return this.command.split(";");
	}
	private void reset() {
		this.command = null;
		this.loop = 0;
		this.timeout = 0;
		this.mode = false;
	}

}
