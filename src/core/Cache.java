package core;

public class Cache extends BaseCache {

	private int lastTag;

	private int lastBlockAddress;
	private int lastEntryTag;
	private MemoryAddress lastAddress;
	private MemoryAddress[] lastAddressList;

	public Cache(int cacheSize, int blockSize, int associativity, ReplacementPolicy replacementPolicy) {
		super(cacheSize, blockSize, associativity, replacementPolicy);
		lastTag = EMPTY;
		lastEntryTag = EMPTY;
	}

	@Override
	public void access(MemoryAddress address) {
		super.access(address);
		lastAddress = address;
		lastTag = getTag(address);
		int blockPosition = getBlockPosition(address);
		lastBlockAddress = blockPosition % NUMBER_OF_SETS;
		MemoryAddress[] tmpList = addresses[lastBlockAddress];

		// save snapshot because we will need to print this
		// and we will be modifying this array after this line
		lastAddressList = tmpList.clone();

		if (tmpList[0] != null)
			lastEntryTag = getTag(tmpList[0]);
		else
			lastEntryTag = EMPTY;

		if (lastEntryTag == EMPTY) {
			address.setLastAccessTime(accesses);
			tmpList[0] = address;
			miss();
		} else if (lastEntryTag == lastTag) {
			// TODO: search for address if not in there then miss?
			if (RP == ReplacementPolicy.LRU) {
				address.setLastAccessTime(accesses);
				tmpList[0] = address;
				lastAddressList[0] = address;
			} else if (RP == ReplacementPolicy.FIFO) {
				// lastAddressList[0]
			}
			hit();
		} else {
			address.setLastAccessTime(accesses);
			boolean isFull = tmpList[ASSOCIATIVITY - 1] != null;
			int index = 0;

			if (!isFull) {
				// find first empty block and set replace index
				for (int i = 0; i < ASSOCIATIVITY; i++) {
					if (tmpList[i] == null) {
						index = i;
						break;
					}
				}
				// find oldest access time if full
				// else put into first empty block
			} else if (RP == ReplacementPolicy.LRU) {
				// replace last used
				for (int i = 0; i < ASSOCIATIVITY; i++) {
					if (tmpList[i].getLastAccessTime() > tmpList[index].getLastAccessTime()) {
						index = i;
					}
				}
			} else if (RP == ReplacementPolicy.FIFO) {
				// find oldest block and set replace index
				for (int i = 0; i < ASSOCIATIVITY; i++) {
					if (tmpList[i].getLastAccessTime() > tmpList[index].getLastAccessTime()) {
						index = i;
					}
				}
			}
			tmpList[index] = address;
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

	public String getLastEntryTags() {
		StringBuilder sb = new StringBuilder();
		boolean fencePost = false;

		for (MemoryAddress address : lastAddressList) {
			if (address == null)
				break;

			if (fencePost) {
				sb.append(",");
			} else {
				fencePost = true;
			}

			sb.append(String.format("%s(%s)", Integer.toHexString(getTag(address)), address.getLastAccessTime()));
		}
		return sb.toString();
	}

	public void printLastAccess() {
		System.out.format(Main.FORMAT, lastAddress, // address
				getLastTag(), // tag
				getLastBlock(), // set
				wasLastHit() ? "hit" : "miss", // hit/miss
				hits, // hits
				misses, // misses
				accesses, // accesses
				String.format("%1.8f", getHitRatio()), // miss ratio
				getLastEntryTags());
	}

}
