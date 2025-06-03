package mm;

public class Page {
    private int pageNumber;
    private int processId;

    public Page(int pageNumber, int processId) {
        this.pageNumber = pageNumber;
        this.processId = processId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getProcessId() {
        return processId;
    }
}