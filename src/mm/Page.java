package mm;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return pageNumber == page.pageNumber && processId == page.processId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, processId);
    }
}