package core;

/**
 * TODO: input is memory access. Store address in cache if its not there already
 * (this is a miss). If it is then that is a hit.
 * 
 * @author Rodney
 *
 */
public class DirectMappedCache extends Cache {
	
	private int lastTag;
	private int lastIndex;
	private int lastOffset;
	
	private int lastBlockPosition;
	private int lastBlockAddress;
	private int lastEntryTag;
	
	public DirectMappedCache(int cacheSize, int blockSize) {
		super(cacheSize, blockSize);
		lastTag = EMPTY;
		lastIndex = EMPTY;
		lastOffset = EMPTY;
		lastEntryTag = EMPTY;
	}

	@Override
	public void access(MemoryAddress address) {
		super.access(address);
		int bitIndex = 0;
		lastTag = Integer.valueOf(address.getBitString().substring(bitIndex, bitIndex += tagBits), 2);
		try {
		lastIndex = Integer.valueOf(address.getBitString().substring(bitIndex, bitIndex += indexBits), 2);
		} catch (Exception e) {
			lastIndex = 0;
		}
		lastOffset = Integer.valueOf(address.getBitString().substring(bitIndex, bitIndex += offsetBits), 2);
		
		lastBlockPosition = Integer.valueOf(address.getBitString().substring(0, tagBits + indexBits), 2);
		lastBlockAddress = lastBlockPosition % numberOfBlocks;
		
		lastEntryTag = tags[lastBlockAddress];
		
		if (lastEntryTag == EMPTY) {
			tags[lastBlockAddress] = lastTag;
			miss();
		} else if (lastEntryTag == lastTag) {
			hit();
		} else {
			tags[lastBlockAddress] = lastTag;
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

}
