package com;

import java.util.ArrayList;
import java.util.List;


class Profile {
	private int loop;
	private int timeout;
	private boolean mode;
	private String[] commands;

	public void setCommand(String command) {
		if (this.commands == null) {
			List<String> commands = new ArrayList<String>();
			for (String temp : command.split(";")) {
				temp = temp.replaceAll("^\\s+", "").replaceAll("\\s+$","");				
				if (temp.length() > 0)
					commands.add(temp);
			}
			if(commands.size() > 0)
				this.commands = (String[]) commands.toArray(new String[commands.size()]);
		}		
	}
	
	public boolean isValid(){
		 return this.commands != null;
	}

	public boolean isConcurrent() {
		return mode;
	}

	public boolean isMulti() {
		if (this.commands.length > 1)
			return true;
		if (this.loop > 1)
			return true;
		return false;
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
		String[] cmds;

		if (this.commands.length == 1 && loop > 1) {
			cmds = new String[loop];
			for (int i = 0; i < loop; i++)
				cmds[i] = this.commands[0];
		} else {
			cmds = this.commands;
		}

		Task[] tasks = new Task[cmds.length];

		for (int i = 0; i < cmds.length; i++)
			tasks[i] = new Task(cmds[i], this.timeout);

		this.reset();

		return tasks;
	}	

	private void reset() {
		this.loop = 0;
		this.timeout = 0;
		this.mode = false;
		this.commands = null;
	}

}
