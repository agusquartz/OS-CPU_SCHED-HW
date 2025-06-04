package mm;
public interface PagingAlgorithm {

	void pageHit(Page p);

	void pageFault(Page p);

}
