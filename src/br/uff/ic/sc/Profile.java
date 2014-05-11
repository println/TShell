package br.uff.ic.sc;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para armazenar informcoes sobre a execucao das tarefas
 * Realiza validacao e processamento dos dados entrados pelo usuario
 * @author Felipe
 *
 */
class Profile {
	/**
	 * Numero de duplicacao de um unico comando
	 */
	private int loop;
	/**
	 * Tempo maximo para a execucao de todos os comandos
	 */
	private int timeout;
	/**
	 * Verdadeiro para execucao concorrente, falso pra sequencial
	 */
	private boolean mode;
	/**
	 * lista de comandos para a execucao
	 */
	private String[] commands;
	
	/**
	 * Metodo para definir os comandos a serem executados
	 * Realiza processamento e verificação da entrada
	 * @param command -  String com um comando ou uma lista de comandos separados por ponto-e-virgula(;)
	 */
	public void setCommand(String command) {
		if (this.commands == null) {
			List<String> commands = new ArrayList<String>();
			for (String temp : command.split(";")) {
				temp = temp.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
				if (temp.length() > 0)
					commands.add(temp);
			}
			if (commands.size() > 0)
				this.commands = (String[]) commands.toArray(new String[commands
						.size()]);
		}
	}
	
	/**
	 * Metodo para a consulta se os camandos definidos sao validos
	 * @return verdadeiro, se validos. Falso, se invalidos
	 */
	public boolean isValid() {
		return this.commands != null;
	}
	
	/**
	 * Metodo para a consulta se os camandos devem ser executados em paralelo(concorrentemente)
	 * @return verdadeiro, para paralelo. Falso, para sequencial
	 */
	public boolean isConcurrent() {
		return mode;
	}
	
	/**
	 * Metodo para a consulta se os camandos definidos sao multiplos
	 * Multiplos em relação a varios comandos diferentes, ou em relacao a inumeros comandos iguais
	 * @return verdadeiro, se multiplo. Falso, se unico
	 */
	public boolean isMulti() {
		if (this.commands.length > 1)
			return true;
		if (this.loop > 1)
			return true;
		return false;
	}
	
	/**
	 * Metodo para a definir se a execucao deve ser sequencial ou concorrente
	 * @param mode - verdadeiro para concorrente, falso para sequencial
	 */
	public void setMode(boolean mode) {
		this.mode = mode;
	}
	
	/**
	 * Metodo para a definir a duplicacao de um unico comando
	 * @param loop
	 */
	public void setLoop(int loop) {
		if (loop > 10)
			this.loop = 10;
		else
			this.loop = loop;
	}
	
	/**
	 * Metodo para definir o tempo maximo de execucao de todos os comandos
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Metodo gerador de Tasks para serem executados pelo Controller
	 * Reseta todos os dados no Profile apos a execucao desse metodo para uma nova entrada de dados
	 * @return array de Tasks
	 */
	public Task[] getTask() {
		String[] cmds;

		//verifica a necessidade de aplicar a duplicacao de comandos
		if (this.commands.length == 1 && loop > 1) {
			cmds = new String[loop];
			//gera duplicatas
			for (int i = 0; i < loop; i++)
				cmds[i] = this.commands[0];
		} else {
			cmds = this.commands;
		}

		Task[] tasks = new Task[cmds.length];

		for (int i = 0; i < cmds.length; i++)
			tasks[i] = new Task(cmds[i], this.timeout);

		//reset
		this.reset();

		return tasks;
	}
	
	/**
	 * Metodo para resetar os dados do Profile para os valores default
	 */
	private void reset() {
		this.loop = 0;
		this.timeout = 0;
		this.mode = false;
		this.commands = null;
	}

}
