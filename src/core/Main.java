package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import core.MemoryAddress.MemoryAddressException;

/**
 * A simple cache simulation program for a direct mapped cache. It will take 4 command-line parameters giving the size of the cache, the block size, a trace flag, and a file name giving the name of a
 * file containing memory addresses. The program will simulate the cache and calculate the hit ratio.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args.length != 4) {
			error("Usage: <cacheSize> <blockSize> <trace> <filename>");
		}

		int cacheSize = -1; // the log2 of the cache size. If n is 14, the cache contains 16K bytes.
		int blockSize = -1; // the log2 of the block size. If m is 6, the block size is 64 bytes.

		/*
		 * If tracing is on, the tracing output should appear in a table with columns right justified. Assume at most 32 bits for addresses, tags, and block numbers. Assume that counts of hits,
		 * misses, and accesses require at most 7 decimal digits. Do not include commas with the decimal numbers. Each line of the table must be shorter than 80 characters and when printed, must
		 * appear all on one line.
		 */
		boolean isTracing = false;
		File inputFile = null;
		FileReader fileReader = null;

		// TODO: validate the command line parameters
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
			fileReader = new FileReader(inputFile);
		} catch (Exception e) {
			error(e.getMessage());
		}

		DirectMappedCache dmc = new DirectMappedCache(cacheSize, blockSize);

		BufferedReader buffer = new BufferedReader(fileReader);
		String line;

		try {
			while ((line = nextLine(buffer)) != null) {
				System.out.println(line);
				MemoryAddress address = new MemoryAddress(line);
				System.out.println("\t" + address);
			}
		} catch (MemoryAddressException e) {
			error(e.getMessage());
		}

		System.out.println("Rodney Rodriguez");
		System.out.format("%s %s %s %s\n", args[0], args[1], args[2], args[3]);
		System.out.format("memory accesses: %d\n", 0);
		System.out.format("hits: %d\n", 0);
		System.out.format("misses: %d\n", 0);
		System.out.format("miss ratio: %.08f\n", 0f);

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
