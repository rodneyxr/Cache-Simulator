package core;

public class BaseCache {

	public static final byte EMPTY = -1;

	// private final int CACHE_SIZE; // the number of bytes in the cache
	// private final int BLOCK_SIZE; // the number of bytes in a block
	// private final int SET_SIZE; // the number of bytes in a set
	protected final int ASSOCIATIVITY;
	protected final ReplacementPolicy RP;

	// protected final int NUMBER_OF_BLOCKS; // the number of blocks in the cache
	protected final int NUMBER_OF_SETS; // the number of sets in the cache
//	protected final int BLOCKS_PER_SET;
	protected final MemoryAddress[][] addresses;

	protected final int TAG_BITS;
	protected final int INDEX_BITS;
	protected final int OFFSET_BITS;

	protected int hits;
	protected int misses;
	protected int accesses;

	private boolean wasLastHit;

	public BaseCache(int log2CacheSize, int log2BlockSize, int log2Associativity, ReplacementPolicy replacementPolicy) {
		this.ASSOCIATIVITY = (int) Math.pow(2, log2Associativity);
		this.RP = replacementPolicy;
		
		int cacheSize = (int) Math.pow(2, log2CacheSize);
		int blockSize = (int) Math.pow(2, log2BlockSize);
		int numberOfBlocks = cacheSize / blockSize;
		int setSize = blockSize * ASSOCIATIVITY;
		this.NUMBER_OF_SETS = cacheSize / setSize;

		this.addresses = new MemoryAddress[numberOfBlocks][ASSOCIATIVITY];
		this.INDEX_BITS = log(numberOfBlocks, 2);
		this.OFFSET_BITS = log2BlockSize;
		this.TAG_BITS = 32 - INDEX_BITS - OFFSET_BITS;
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
		if (TAG_BITS + INDEX_BITS == 0)
			return 0;
		int blockPositition = EMPTY;
		blockPositition = Integer.valueOf(address.getBitString().substring(0, TAG_BITS + INDEX_BITS), 2);
		return blockPositition;
	}

	protected int getTag(MemoryAddress address) {
		if (TAG_BITS == 0)
			return 0;
		int tag = EMPTY;
		tag = Integer.valueOf(address.getBitString().substring(0, TAG_BITS), 2);
		return tag;
	}

	protected int getIndex(MemoryAddress address) {
		if (INDEX_BITS == 0)
			return 0;
		int index = EMPTY;
		int start = TAG_BITS;
		int end = start + INDEX_BITS;
		index = Integer.valueOf(address.getBitString().substring(start, end), 2);
		return index;
	}

	protected int getOffset(MemoryAddress address) {
		if (OFFSET_BITS == 0)
			return 0;
		int offset = EMPTY;
		int start = TAG_BITS + INDEX_BITS;
		int end = start + OFFSET_BITS;
		offset = Integer.valueOf(address.getBitString().substring(start, end), 2);
		return offset;
	}

	private int log(int x, int base) {
		return (int) Math.ceil((Math.log(x) / Math.log(base)));
	}

	// @Override
	// public String toString() {
	// return String.format("[cacheSize=%d, blockSize=%d, numberOfBlocks=%d]", CACHE_SIZE, BLOCK_SIZE, NUMBER_OF_BLOCKS);
	// }
}
