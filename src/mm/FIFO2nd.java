package mm;
import core.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Map;

public class FIFO2nd implements PagingAlgorithm {

	public FIFO2nd(Memory ram, Memory virtual, PageTable table){
		this.ram = ram;
		this.virtual = virtual;
		this.table = table;

		this.queue = new LinkedList<>();
		this.referenceBits = new HashMap<>();
        for(Page p : ram.getPages().toArray(new Page[0])){
            queue.offer(p);
            referenceBits.put(p,false);
        }
	}

	@Override
	public void pageHit(Page p){
		referenceBits.put(p,true);	//This is the second chance, you earned it, little page.
	}

	@Override
	public void pageFault(Page p){
		if(ram.isFull()){
			while(true){
				Page cand = queue.poll();	//get the oldest page
				if(cand == null) break;	//shouldn't happen ever, but poll is able to return null on an empty queue, so we prepare
				if(referenceBits.getOrDefault(cand, false)){ //check if our page has a second chance
					referenceBits.put(cand,false); //you just got saved from death, no more chances for you now
					queue.offer(cand); //requeue
				}else{
					ram.remove(cand);
					virtual.add(cand);
					table.updateBit(cand.getProcessId(), cand.getPageNumber(), 0);	//is in virtual mem now
					referenceBits.remove(cand);
					break;
				}
			}
		}
		
		ram.add(p);
		virtual.remove(p);
		table.updateBit(p.getProcessId(), p.getPageNumber(), 1);	//the new page has been brought to ram now
		queue.offer(p);	//put it in the ordering for rotation
		referenceBits.put(p,false);	//when a new page enters ram it still hasn't earned the second chance.
	}
	
	private final Memory ram;
	private final Memory virtual;
	private final PageTable table;
	private final Queue<Page> queue;
	private final Map<Page,Boolean> referenceBits;
}


