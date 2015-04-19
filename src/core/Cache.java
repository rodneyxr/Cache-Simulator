package core;

public class Cache extends BaseCache {

	private int lastTag;
	private int lastIndex;
	private int lastOffset;

	// private int lastBlockPosition;
	private int lastBlockAddress;
	private int lastEntryTag;
	private MemoryAddress lastAddress;

	public Cache(int cacheSize, int blockSize, int associativity, ReplacementPolicy replacementPolicy) {
		super(cacheSize, blockSize, associativity, replacementPolicy);
		lastTag = EMPTY;
		lastIndex = EMPTY;
		lastOffset = EMPTY;
		lastEntryTag = EMPTY;
	}

	@Override
	public void access(MemoryAddress address) {
		super.access(address);
		lastTag = getTag(address);
		lastIndex = getIndex(address);
		lastOffset = getOffset(address);
		int blockPosition = getBlockPosition(address);
		lastBlockAddress = blockPosition % numberOfBlocks;

		if (addresses[lastBlockAddress] != null)
			lastEntryTag = getTag(addresses[lastBlockAddress]);
		else
			lastEntryTag = EMPTY;
		lastAddress = address;

		if (lastEntryTag == EMPTY) {
			addresses[lastBlockAddress] = lastAddress;
			miss();
		} else if (lastEntryTag == lastTag) {
			hit();
		} else {
			addresses[lastBlockAddress] = lastAddress;
			miss();
		}
	}

	public String getLastTag() {
		return Integer.toHexString(lastTag);
	}

	public String getLastBlock() {
		return Integer.toHexString(lastBlockAddress);
	}

	public String getLastEntryTag() {
		if (lastEntryTag == EMPTY)
			return "";
		return Integer.toHexString(lastEntryTag);
	}

	public void printLastAccess() {
		System.out.format(Main.FORMAT, lastAddress, // address
				getLastTag(), // tag
				getLastBlock(), // block
				wasLastHit() ? "hit" : "miss", // hit/miss
				hits, // hits
				misses, // misses
				accesses, // accesses
				String.format("%1.8f", getHitRatio()), // miss ratio
				getLastEntryTag()); // TODO: add tags
	}

}
