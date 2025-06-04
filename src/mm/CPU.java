package mm;

public class CPU {

	public CPU(Memory ram, Memory virtual, PageTable table, PagingAlgorithm algo, LogManager logger){
		this.ram = ram;
		this.virtual = virtual;
		this.pageFaultMngr = pageFaultMngr;
		this.table = table;
		this.algo = algo;
		this.logger = logger;
	}

	public void usePage(Process p){
		boolean fault = false;	//en principio, no hay una page fault
		int pid = p.getId();	//dado el proceso, generamos pagina aleatoria a pedir
		int page = (Math.random() * 100) % p.getPages();
		//nos fijamos si la página aleatoria esta en ram mediante la PageTable
		if(table.getPresenceValue(pid, page) == 0){	 //si la pagina no está en ram viene lo interesante
			fault = true;	//avisamos a nuestro generador que hubo una pageFault
			//invocamos a PFH, que va a correr el algoritmo y nos va a modificar la ram y la pagetable
			//pageFaultMngr.procesarFallo(pid, page, ram, virtual, table, algo); 
			algo.pageFault(page);
		} else{
			pageHit(page);
		}
		
		//hacemos una snapshot de ram y lo mandamos al logger
		logger.addEntry(Arrays.copyOf(ram.getFrames(), fault));
	}

	//private vars
	Memory ram;
	Memory virtual;
	PFH pageFaultMngr;
	PageTable table;
	PagingAlgorithm algo;
	LogManager logger;
}
