package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cache extends BaseCache {

	private int lastTag;

	private int lastBlockAddress;
	private MemoryAddress lastAddress;
	private MemoryAddress[] lastAddressList;

	public Cache(int cacheSize, int blockSize, int associativity,
			ReplacementPolicy replacementPolicy) {
		super(cacheSize, blockSize, associativity, replacementPolicy);
		lastTag = EMPTY;
	}

	@Override
	public void access(MemoryAddress address) {
		super.access(address);
		lastAddress = address;
		lastTag = getTag(address);
		address.setTag(lastTag);
		int blockPosition = getBlockPosition(address);
		lastBlockAddress = blockPosition % NUMBER_OF_SETS;
		MemoryAddress[] tmpList = addresses[lastBlockAddress];

		// save snapshot because we will need to print this
		// and we will be modifying this array after this line
		lastAddressList = tmpList.clone();

		int lastEntryTag = isHit(tmpList, address);

		// set is empty
		if (tmpList[0] == null) {
			address.setLastAccessTime(accesses);
			tmpList[0] = address;
			miss();

			// set is not empty but there was a miss
		} else if (lastEntryTag == EMPTY) {
			address.setLastAccessTime(accesses);
			replace(tmpList, address);
			miss();

			// set was not empty and there was a hit
		} else {
			if (RP == ReplacementPolicy.LRU)
				tmpList[lastEntryTag].setLastAccessTime(accesses);
			hit();
		}
	}

	/**
	 * 
	 * @param tmpList
	 * @param address
	 * @return -1 if miss, index of block in tmpList otherwise
	 */
	private int isHit(MemoryAddress[] tmpList, MemoryAddress address) {
		int index = EMPTY;
		for (int i = 0; i < ASSOCIATIVITY; i++) {
			if (tmpList[i] == null)
				break;
			if (getTag(tmpList[i]) == getTag(address)) {
				index = i;
				break;
			}
		}
		return index;
	}

	private void replace(MemoryAddress[] tmpList, MemoryAddress address) {
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
				if (tmpList[i].getLastAccessTime() > tmpList[index]
						.getLastAccessTime()) {
					index = i;
				}
			}
		} else if (RP == ReplacementPolicy.FIFO) {
			// find oldest block and set replace index
			for (int i = 0; i < ASSOCIATIVITY; i++) {
				if (tmpList[i].getLastAccessTime() > tmpList[index]
						.getLastAccessTime()) {
					index = i;
				}
			}
		}
		tmpList[index] = address;
	}

	public String getLastTag() {
		return Integer.toHexString(lastTag);
	}

	public String getLastBlock() {
		return Integer.toHexString(lastBlockAddress);
	}

	public String getLastEntryTags() {
		StringBuilder sb = new StringBuilder();
		List<MemoryAddress> sortedList = new ArrayList<MemoryAddress>(
				ASSOCIATIVITY);
		for (MemoryAddress address : lastAddressList)
			if (address != null)
				sortedList.add(address);
		Collections.sort(sortedList);
		boolean fencePost = false;
		for (MemoryAddress address : sortedList) {
			if (address == null)
				break;

			if (fencePost) {
				sb.append(",");
			} else {
				fencePost = true;
			}

			sb.append(String.format("%s(%s)",
					Integer.toHexString(getTag(address)),
					address.getLastAccessTime()));
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
