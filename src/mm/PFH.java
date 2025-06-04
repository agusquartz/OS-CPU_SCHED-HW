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
    public void processPageFault(int processId, int pageNumber, Memory ram, Memory virtMem, PageTable pageTable, PagingAlgorithm algorithm) {
        if (ram.freeFrameAvailable()) {

            // There is a free frame available in RAM
            // Get the page from Virtual Memory
            // Insert the page into RAM
            Page page = virtMem.removePage(new Page(processId, pageNumber));
            int frame = ram.putPage(page);

            // Update the page table (1 = RAM)
            pageTable.updateBit(processId, pageNumber, 1);

        } else {
            // No free frame available, use replacement algorithm

            Page victim = algorithm.selectVictim(ram);                                  // Select a victim page using the replacement algorithm

            int frameVictim = ram.getFrame(victim);                                     // Get the frame number of the victim page
            int frameVirtMem = virtMem.getFrame(new Page(processId, pageNumber));       // Get the frame number of the page to be loaded into RAM      

            ram.removePage(victim);                                                     // Remove the victim page from RAM
            Page page = virtMem.removePage(new Page(processId, pageNumber));            // Get the page from Virtual Memory

            virtMem.putPage(victim, frameVirtMem);                                      // Write the victim page back to Virtual Memory
            ram.putPage(page, frameVictim);                                             // Insert the page into RAM

            pageTable.updateBit(victim.getProcessId(), victim.getPageNumber(), 0);      // Update page table (0 = Virtual Memory)
            pageTable.updateBit(processId, pageNumber, 1);                              // Update the page table (1 = RAM)
        }
    }
}