package core;

import java.util.Arrays;

public class BaseCache {

	public static final byte EMPTY = -1;

	private final int cacheSize; // the number of bytes in the cache
	private final int blockSize; // the number of bytes in a block
	private final int setSize; // the number of bytes in a set
	private final int associativity;
	private final ReplacementPolicy replacementPolicy;

	protected final int numberOfBlocks; // the number of blocks in the cache
	protected final int numberOfSets; // the number of sets in the cache
	protected final MemoryAddress[] addresses;

	protected int tagBits;
	protected int indexBits;
	protected int offsetBits;

	protected int hits;
	protected int misses;
	protected int accesses;

	private boolean wasLastHit;

	public BaseCache(int log2CacheSize, int log2BlockSize, int log2Associativity, ReplacementPolicy replacementPolicy) {
		this.cacheSize = (int) Math.pow(2, log2CacheSize);
		this.blockSize = (int) Math.pow(2, log2BlockSize);
		this.associativity = (int) Math.pow(2, log2Associativity);
		this.replacementPolicy = replacementPolicy;
		this.numberOfBlocks = cacheSize / blockSize;
		this.setSize = blockSize * associativity;
		this.numberOfSets = cacheSize / setSize;

		this.addresses = new MemoryAddress[numberOfBlocks];
		Arrays.fill(addresses, null);
		this.indexBits = log(numberOfBlocks, 2);
		this.offsetBits = log2BlockSize;
		this.tagBits = 32 - indexBits - offsetBits;
		this.hits = 0;
		this.misses = 0;
		this.accesses = 0;
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

	protected int getBlockPosition(MemoryAddress address) {
		if (tagBits + indexBits == 0)
			return 0;
		int blockPositition = EMPTY;
		blockPositition = Integer.valueOf(address.getBitString().substring(0, tagBits + indexBits), 2);
		return blockPositition;
	}

	protected int getTag(MemoryAddress address) {
		if (tagBits == 0)
			return 0;
		int tag = EMPTY;
		tag = Integer.valueOf(address.getBitString().substring(0, tagBits), 2);
		return tag;
	}

	protected int getIndex(MemoryAddress address) {
		if (indexBits == 0)
			return 0;
		int index = EMPTY;
		int start = tagBits;
		int end = start + indexBits;
		index = Integer.valueOf(address.getBitString().substring(start, end), 2);
		return index;
	}

	protected int getOffset(MemoryAddress address) {
		if (offsetBits == 0)
			return 0;
		int offset = EMPTY;
		int start = tagBits + indexBits;
		int end = start + offsetBits;
		offset = Integer.valueOf(address.getBitString().substring(start, end), 2);
		return offset;
	}

	private int log(int x, int base) {
		return (int) Math.ceil((Math.log(x) / Math.log(base)));
	}

	@Override
	public String toString() {
		return String.format("[cacheSize=%d, blockSize=%d, numberOfBlocks=%d]", cacheSize, blockSize, numberOfBlocks);
	}
}
