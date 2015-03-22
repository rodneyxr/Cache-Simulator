package core;

public abstract class Cache {

	private final int cacheSize; // the number of bytes in the cache
	private final int blockSize; // the number of bytes in a block
	private final int numberOfBlocks; // the number of blocks in the cache
	protected final int[] tags;

	protected int tagBits;
	protected int indexBits;
	protected int offsetBits;

	protected int hits;
	protected int misses;
	protected int accesses;

	private boolean wasLastHit;
	private int lastTag;

	public Cache(int log2CacheSize, int log2BlockSize) {
		cacheSize = (int) Math.pow(2, log2CacheSize);
		blockSize = (int) Math.pow(2, log2BlockSize);
		numberOfBlocks = cacheSize / blockSize;
		tags = new int[numberOfBlocks];
		indexBits = log(numberOfBlocks, 2);
		offsetBits = log2BlockSize;
		tagBits = 32 - indexBits - offsetBits;
		hits = 0;
		misses = 0;
		accesses = 0;

		System.out.printf("Cache Created: tag=%d, index=%d, offset=%d\n", tagBits, indexBits, offsetBits);
	}

	{
		lastTag = -1;
	}

	protected void access(MemoryAddress address) {
		accesses++;
		lastTag = Integer.valueOf(address.getBitString().substring(0, tagBits), 2);
	}

	protected void hit() {
		hits++;
		wasLastHit = true;
	}

	protected void miss() {
		misses++;
		wasLastHit = false;
	}

	public boolean wasLastHit() {
		return wasLastHit;
	}

	public int getLastTag() {
		return lastTag;
	}

	public float getHitRatio() {
		if (accesses == 0)
			return 1.0f;
		return (float) misses / (float) accesses;
	}

	private int log(int x, int base) {
		return (int) Math.ceil((Math.log(x) / Math.log(base)));
	}

	@Override
	public String toString() {
		return String.format("[cacheSize=%d, blockSize=%d, numberOfBlocks=%d]", cacheSize, blockSize, numberOfBlocks);
	}
}
