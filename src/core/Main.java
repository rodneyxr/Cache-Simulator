package core;

import java.io.File;

/**
 * A simple cache simulation program for a direct mapped cache. It will take 4
 * command-line parameters giving the size of the cache, the block size, a trace
 * flag, and a file name giving the name of a file containing memory addresses.
 * The program will simulate the cache and calculate the hit ratio.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args.length != 4) {
			error("Usage: <cacheSize> <blockSize> <trace> <filename>");
		}

		int cacheSize = -1;
		int blockSize = -1;
		boolean isTracing = false;
		File inputFile = null;

		try {
			cacheSize = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			error("Invalid cacheSize.");
		}

		try {
			blockSize = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			error("Invalid blockSize.");
		}

		try {
			isTracing = Boolean.parseBoolean(args[2]);
		} catch (Exception e) {
			error(e.getMessage());
		}

		try {
			inputFile = new File(args[3]);
			if (!inputFile.exists())
				throw new Exception("File does not exist.");
		} catch (Exception e) {
			error(e.getMessage());
		}

		System.out.println("Rodney Rodriguez");
		System.out.format("%d %d %b %s\n", cacheSize, blockSize, isTracing, inputFile);
		System.out.format("memory accesses: %d\n", 0);
		System.out.format("hits: %d\n", 0);
		System.out.format("misses: %d\n", 0);
		System.out.format("miss ratio: %.08f\n", 0f);
	}

	private static void error(String message) {
		System.err.println(message);
		System.exit(-1);
	}

}
