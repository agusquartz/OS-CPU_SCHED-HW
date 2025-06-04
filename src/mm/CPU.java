package mm;

import core.Process;

public class CPU {

	public CPU(Memory ram, Memory virtual, PageTable table, PagingAlgorithm algo, LogManager logger){
		this.ram = ram;
		this.virtual = virtual;
		this.table = table;
		this.algo = algo;
		this.logger = logger;
	}

	public void usePage(Process p){
		boolean fault = false;	//en principio, no hay una page fault
		int pid = p.getId();	//dado el proceso, generamos pagina aleatoria a pedir
		int pageNum = (int)(Math.random() * p.getPages());
		Page page = new Page(pid, pageNum);
		//nos fijamos si la página aleatoria esta en ram mediante la PageTable
		if(table.getPresenceValue(pid, pageNum) == 0){	 //si la pagina no está en ram viene lo interesante
			fault = true;	//avisamos a nuestro generador que hubo una pageFault
			//invocamos a PFH, que va a correr el algoritmo y nos va a modificar la ram y la pagetable
			algo.pageFault(page);
		} else{
			algo.pageHit(page);
		}
		//hacemos una snapshot de ram y lo mandamos al logger
		Page[] ramSnapshot = ram.getPages().toArray(new Page[0]);
		logger.addEntry(ramSnapshot, fault);
	}

	public LogManager getLogManager() {
		return logger;
	}

	//private vars
	Memory ram;
	Memory virtual;
	PageTable table;
	PagingAlgorithm algo;
	LogManager logger;
}
