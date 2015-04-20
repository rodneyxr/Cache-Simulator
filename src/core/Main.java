package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import core.MemoryAddress.MemoryAddressException;

/**
 * A simple cache simulation program for a direct mapped cache. It will take 6 command-line parameters giving the size of the cache, the block size, a trace flag, and a file name
 * giving the name of a file containing memory addresses. The program will simulate the cache and calculate the miss ratio.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Main {

	public static final String FORMAT = "%8s %8s %8s %5s %8s %8s %10s %11s %s\n";
	
	// 16 6 0 fifo on memory-small.txt
	public static void main(String[] args) {
		if (args.length != 6) {
			error("Usage: <cacheSize> <blockSize> <associativity> <fifo/lru> <trace> <filename>");
		}

		// the log2 of the cache size. If n is 14, the cache contains 16K bytes.
		int cacheSize = -1;
		// the log2 of the block size. If m is 6, the block size is 64 bytes.
		int blockSize = -1;
		int associativity = -1;

		boolean isTracing = false;
		ReplacementPolicy replacementPolicy = null;

		File inputFile = null;
		FileReader fileReader = null;

		try {
			cacheSize = Integer.parseInt(args[0]);
			if (cacheSize <= 0)
				error("Cache size cannot be 0 or less.");
		} catch (NumberFormatException e) {
			error("Invalid cacheSize.");
		}

		try {
			blockSize = Integer.parseInt(args[1]);
			if (blockSize > cacheSize)
				error("Block size cannot be greater than the cache size.");
			if (blockSize < 1)
				error("Block size cannot be 0 or less.");
		} catch (NumberFormatException e) {
			error("Invalid blockSize.");
		}

		try {
			associativity = Integer.parseInt(args[2]);
			if (associativity < 0 || associativity > cacheSize - blockSize)
				associativity = cacheSize - blockSize;
		} catch (NumberFormatException e) {
			error("Invalid associativity");
		}

		try {
			String replacement = args[3];
			if (replacement.equalsIgnoreCase("fifo"))
				replacementPolicy = ReplacementPolicy.FIFO;
			else if (replacement.equalsIgnoreCase("lru"))
				replacementPolicy = ReplacementPolicy.LRU;
			else
				throw new Exception("Invalid replacement policy.");
		} catch (Exception e) {
			error(e.getMessage());
		}

		try {
			String traceFlag = args[4];
			if (traceFlag.equalsIgnoreCase("on"))
				isTracing = true;
			else if (traceFlag.equalsIgnoreCase("off"))
				isTracing = false;
			else
				throw new Exception("Invalid trace flag.");
		} catch (Exception e) {
			error(e.getMessage());
		}

		try {
			inputFile = new File(args[5]);
			if (!inputFile.exists())
				throw new Exception("File does not exist.");
			fileReader = new FileReader(inputFile);
		} catch (Exception e) {
			error(e.getMessage());
		}

		Cache cache = new Cache(cacheSize, blockSize, associativity, replacementPolicy);

		BufferedReader buffer = new BufferedReader(fileReader);
		String line;

		

		if (isTracing) {
			System.out.format(FORMAT, "address", "tag", "set", "h/m", "hits", "misses", "accesses", "miss_ratio", "tags");
		}

		try {
			while ((line = nextLine(buffer)) != null) {
				MemoryAddress address = new MemoryAddress(line);

				cache.access(address);

				if (isTracing) {
					cache.printLastAccess();
				}
			}
		} catch (MemoryAddressException e) {
			error(e.getMessage());
		}

		try {
			buffer.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Rodney Rodriguez");
		System.out.format("%s %s %s %s %s %s\n", args[0], args[1], args[2], args[3], args[4], args[5]);
		System.out.format("memory accesses: %d\n", cache.accesses);
		System.out.format("hits: %d\n", cache.hits);
		System.out.format("misses: %d\n", cache.misses);
		System.out.format("miss ratio: %.08f\n", cache.getHitRatio());
	}

	/**
	 * 
	 * @param buffer
	 * @return the next line in a buffer
	 */
	private static String nextLine(BufferedReader buffer) {
		String line = null;
		try {
			line = buffer.readLine();
		} catch (IOException e) {
			error(e.getMessage());
		}
		return line;
	}

	/**
	 * Prints an error message and exits the program
	 * 
	 * @param message
	 */
	private static void error(String message) {
		System.err.println(message);
		System.exit(-1);
	}

}
