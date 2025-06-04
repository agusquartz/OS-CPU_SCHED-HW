package mm;
import java.util.LinkedHashMap;

public class LRU implements PagingAlgorithm {

    public LRU(Memory ram, Memory virtual, PageTable pageTable) {
        this.ram = ram;
        this.virtual = virtual;
        this.pageTable = pageTable;
    }

    @Override
    public void pageHit(Page p) {
        // Accessing it updates recency
        usageOrder.get(p); // read to trigger access-order update
    }

    @Override
    public void pageFault(Page p) {

        if (ram.isFull()) {
            Page leastUsed = usageOrder.keySet().iterator().next(); // first = LRU
            ram.remove(leastUsed);
            virtual.add(leastUsed);
            pageTable.updateBit(leastUsed.getProcessId(), leastUsed.getPageNumber(), false);
            usageOrder.remove(leastUsed);
        }

        ram.add(p);
        virtual.remove(p);
        pageTable.setInRAM(p, true);
        usageOrder.put(p, true); // insert or update

    }

    private final Memory ram;
    private final Memory virtual;
    private final PageTable pageTable;
    private final LinkedHashMap<Page, Boolean> usageOrder = new LinkedHashMap<>(20, 0.75f, true);	//turns out for using accessing order we need this extremely specific constructor. You can't just say "new LinkedHashMap<>(true) and have accessing order enabled
}

