package core;

public abstract class Cache {

	private final int cacheSize; // the number of bytes in the cache
	private final int blockSize; // the number of bytes in a block
	private final int numberOfBlocks; // the number of blocks in the cache
	private final MemoryAddress[] addresses;

	public Cache(int log2CacheSize, int log2BlockSize) {
		cacheSize = (int) Math.pow(2d, log2CacheSize);
		blockSize = (int) Math.pow(2d, log2BlockSize);
		numberOfBlocks = cacheSize / blockSize;
		addresses = new MemoryAddress[numberOfBlocks];
		System.out.println("Cache Created: " + this);
	}

	@Override
	public String toString() {
		return String.format("[cacheSize=%d, blockSize=%d, numberOfBlocks=%d]", cacheSize, blockSize, numberOfBlocks);
	}
}
