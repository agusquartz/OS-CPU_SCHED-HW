package mm;

public class PFH {

    /**
     * Handles a page fault for a given process id and page number.
     *
     * @param processId     The id of the process that caused the page fault.
     * @param pageNumber    The page number that caused the fault.
     * @param ram           The RAM memory manager.
     * @param virtMem       The Virtual Memory manager.
     * @param pageTable     The Page Table.
     * @param algorithm    The replacement algorithm to use.
     */
    public void processPageFault(Page page, Memory ram, Memory virtMem, PageTable pageTable, PagingAlgorithm algorithm) {
        if (!ram.isFull()) {

            // There is a free frame available in RAM
            // Get the page from Virtual Memory
            // Insert the page into RAM
            virtMem.remove(page);
            ram.add(page);

            // Update the page table (1 = RAM)
            pageTable.updateBit(page.getProcessId(), page.getPageNumber(), 1);

        } else {
            // No free frame available, use replacement al
            Page victim = new Page(0, 0); // Initialize victim page
            ram.remove(victim);                                                     // Remove the victim page from RAM
            virtMem.remove(page);                                                       // Get the page from Virtual Memory

            virtMem.add(victim);                                                    // Write the victim page back to Virtual Memory
            ram.add(page);                                                          // Insert the page into RAM

            pageTable.updateBit(victim.getProcessId(), victim.getPageNumber(), 0);      // Update page table (0 = Virtual Memory)
            pageTable.updateBit(page.getProcessId(), page.getPageNumber(), 1);          // Update the page table (1 = RAM)
        }
    }
}