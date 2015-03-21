package core;

/**
 * TODO: input is memory access. Store address in cache if its not there already
 * (this is a miss). If it is then that is a hit.
 * 
 * @author Rodney
 *
 */
public class DirectMappedCache extends Cache {

	public DirectMappedCache(int cacheSize, int blockSize) {
		super(cacheSize, blockSize);
	}

	@Override
	public void access(MemoryAddress address) {
		super.access(address);
	}

	@Override
	protected void hit() {
		super.hit();
	}

	@Override
	protected void miss() {
		super.miss();
	}

}
