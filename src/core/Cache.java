package core;

import java.util.Arrays;

public abstract class Cache {

	public static final byte EMPTY = -1;

	private final int cacheSize; // the number of bytes in the cache
	private final int blockSize; // the number of bytes in a block
	protected final int numberOfBlocks; // the number of blocks in the cache
	protected final int[] tags;

	protected int tagBits;
	protected int indexBits;
	protected int offsetBits;

	protected int hits;
	protected int misses;
	protected int accesses;

	private boolean wasLastHit;

	public Cache(int log2CacheSize, int log2BlockSize) {
		cacheSize = (int) Math.pow(2, log2CacheSize);
		blockSize = (int) Math.pow(2, log2BlockSize);
		numberOfBlocks = cacheSize / blockSize;
		tags = new int[numberOfBlocks];
		Arrays.fill(tags, EMPTY);
		indexBits = log(numberOfBlocks, 2);
		offsetBits = log2BlockSize;
		tagBits = 32 - indexBits - offsetBits;
		hits = 0;
		misses = 0;
		accesses = 0;
	}

	protected void access(MemoryAddress address) {
		accesses++;
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

	public double getHitRatio() {
		if (accesses == 0)
			return 1.0d;
		return (double) misses / (double) accesses;
	}

	private int log(int x, int base) {
		return (int) Math.ceil((Math.log(x) / Math.log(base)));
	}

	@Override
	public String toString() {
		return String.format("[cacheSize=%d, blockSize=%d, numberOfBlocks=%d]",
				cacheSize, blockSize, numberOfBlocks);
	}
}
