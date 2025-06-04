package mm;
public interface PagingAlgorithm {

	public void pageHit(Page p);

	public void pageFault(Page p);

}
