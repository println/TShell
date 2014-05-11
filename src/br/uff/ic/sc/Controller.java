package br.uff.ic.sc;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class para executar os Tasks
 * @author Felipe
 *
 */
class Controller {
	/**
	 * Atributo para manter(cache) do sumario de execucoes a ser impresso
	 */
	private StringBuilder summary = new StringBuilder();
	
	/**
	 * Metodo utlizado para a escolha dos modelos de execucao: sequencial ou concorrente
	 * @param profile - perfil de execucao dos comandos
	 */
	public void process(Profile profile) {

		if (profile.isConcurrent())
			this.runConcurrent(profile.getTask());
		else
			this.runSequential(profile.getTask());

		//impressao do sumario com as informacoes de execucao dos Tasks
		System.out.println(this.summary.toString());
		
		//limpeza do sumario
		this.summary.setLength(0);
	}

	/**
	 * Metodo para a execucao de Tasks concorrentemente, em Threads
	 * @param tasks - tarefas a serem executadas
	 */
	private void runConcurrent(Task[] tasks) {
		//thread pool para enviar os Tasks
		ExecutorService executor = Executors.newFixedThreadPool(tasks.length);
		//lista de Tasks a serem executados
		List<Task> list = Arrays.asList(tasks);

		try {
			//invocacao da execucao dos Tasks
			//aguarda a execucao de todos os Tasks terminarem
			//retorna a lista com sumarios de cada Task executado
			List<Future<String>> futures = executor.invokeAll(list);
			
			//adicao de cada item de sumario da lista para o cache de impressao(this.summary)
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
	
	/**
	 * Metodo para a execucao de Tasks sequencialmente
	 * @param tasks - tarefas a serem executadas
	 */
	private void runSequential(Task[] tasks){
		for (Task task : tasks)
			addResult(task.call());
	}
	
	/**
	 * Metodo para adicionar uma String para o cache de impressao(this.summary)
	 * Metodo realiza uma formatacao parcial
	 * @param result
	 */
	private void addResult(String result) {

		if (this.summary.length() == 0)
			this.summary.append("\n\n");

		this.summary.append(result);
		this.summary.append('\n');
	}
}
