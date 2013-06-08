package com.uff.sc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;

/**
 * Classe para a terafa de executar um unico comando, com timeout
 * 
 * @author Felipe
 *
 */
class Task implements Callable<String> {
	/**
	 * Comando a ser executado
	 */
	private String command;
	/**
	 * Tempo maximo para a execucao do comando
	 */
	private int timeout;
	
	/**
	 * Construtor
	 * @param command - comando a ser executado
	 * @param timeout - tempo maximo para a execucao do comando
	 */
	public Task(String command, int timeout) {
		this.command = command;
		this.timeout = timeout;
	}
	
	/**
	 * Metodo abstrato da interface callable
	 * Similar ao Run() do runnable, mas contem a possibilidade de retorno
	 * Nucleo da thread, executa o comando(processo externo)
	 * @return retorna o resumo da execucao para ser impresso no final
	 */
	@Override
	public String call(){
		//tempo de execucao do comando(processo externo)
		long elapsedTime = 0;
		//comando(processo externo) foi abortado?
		boolean killed = false;
		//erro ao executar o comando(processo externo)?
		boolean error = false;

		try {
			//Execução do comando - abre como um novo processo externo filho da thread
			Process process = Runtime.getRuntime().exec(this.command);
			
			//contador de tempo
			long now = System.currentTimeMillis();
			long timeout = 1000 * this.timeout;
			long finish = now + timeout;
			
			//thread auxiliar utilizada para a impressao da saida do processo externo
			Thread consoleInput = this.inputScanning(process);
			consoleInput.start();
			
			//loop para verficar o tempo decorrido de acordo com o timeout estipulado
			//se o processo externo tiver vivo, verifica
			//se o processo externo acabar antes do timeout, sai do loop
			while (this.isAlive(process)) {
				Thread.sleep(100);
				if (System.currentTimeMillis() >= finish) {
					elapsedTime = timeout;
					killed = true;					
					consoleInput.interrupt();
					process.destroy();
				}
			}
			
			//grava o tempo decorrido pelo processo externo, caso o mesmo termine antes do timeout
			if (elapsedTime == 0)
				elapsedTime = System.currentTimeMillis() - now;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			//erro ao tentar executar o comando
			error = true;
		}
		//gera o sumario e retorna a string para Controller
		return this.generateSummary(command, elapsedTime, killed,error);
	}

	/**
	 * Metodo para a criacao de uma thread auxiliar de entrada de dados para um processo externo
	 * Implementado mas nao utilizado para evitar conflitos
	 * @param process
	 * @return thread leitora
	 */
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
	
	/**
	 * Metodo para a criacao de uma thread auxiliar de impressao de um processo externo
	 * @param process - processo externo
	 * @return thread impressora
	 */
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
	
	/**
	 * Metodo para verificar se o processo externo esta em execucao
	 * @param process - processo externo
	 * @return verdadeiro se estiver em execucao, falso caso tenha terminado
	 */
	private boolean isAlive(Process process) {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
	
	/**
	 * Metodo para gerar sumario com informacoes sobre a execucao do processo externo
	 * @param command - comando executado
	 * @param length - tempo de execucao
	 * @param killed - verdadeiro, foi encerrado
	 * @param err - verdadeiro, ocorreu algum erro ao executar
	 * @return String com todas as informacoes
	 */
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
