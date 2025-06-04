package mm;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class Memory {

	public Memory(int capacity){
		this.capacity = capacity;
		this.pages = new HashSet<>();
	}

			
	public boolean contains(Page p){
		return pages.contains(p);
	}

	public boolean isFull(){
		return pages.size() >= capacity;
	}

	public void add(Page p){
		pages.add(p);
	}

	public void remove(Page p){
		pages.remove(p);
	}

	public int size(){
		return pages.size();
	}

	public Set<Page> getPages(){
		return Collections.unmodifiableSet(pages);
	}

	private int capacity;
	private Set<Page> pages;
}
