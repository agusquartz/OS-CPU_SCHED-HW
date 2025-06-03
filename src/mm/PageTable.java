package mm;

import java.util.HashMap;
import java.util.Map;

public class PageTable {

    // Map to store presence status for each page (1 = RAM, 0 = Virtual Memory)
    private Map<Page, Integer> pagePresence;

    public PageTable() {
        pagePresence = new HashMap<>();
    }

    /**
     * Adds or updates the presence status of a page in the page table.
     *
     * If the page (identified by processId and pageNumber) already exists in the table,
     * its presence value is updated. If it does not exist, it is added with the specified value.
     *
     * @param processId  The ID of the process that owns the page.
     * @param pageNumber The number of the page.
     * @param value      1 if the page is in RAM, 0 if it is in Virtual Memory.
     * @throws IllegalArgumentException if the value is not 0 or 1.
     */
    public void updateBit(int processId, int pageNumber, int value) {
        if (value != 0 && value != 1) {
            throw new IllegalArgumentException("Value must be 1 (RAM) or 0 (Virtual Memory)");
        }
        Page page = new Page(pageNumber, processId);
        pagePresence.put(page, value);
    }
    
    /**
     * Gets the value (1 or 0) for a specific page using processId and pageNumber.
     *
     * @param processId  The process ID.
     * @param pageNumber The page number.
     * @return The associated value (1 = RAM, 0 = Virtual Memory), or null if not found.
     */
    public Integer getPresenceValue(int processId, int pageNumber) {
        Page targetPage = new Page(pageNumber, processId);
        return pagePresence.get(targetPage); // May return null if not present
    }

}