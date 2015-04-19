package core;

public class MemoryAddress {

	private static final String DEC_REGEX = "^[0-9]+$";
	private static final String HEX_REGEX = "^(0[xX])?[0-9a-fA-F]+$";
	private static final String ZERO_BITS = "00000000000000000000000000000000";

	private int address;
	private String bitString;
	private int lastAccessTime = 0;

	public MemoryAddress(String memoryAddress) throws MemoryAddressException {
		memoryAddress = memoryAddress.trim().toLowerCase();
		if (isDecimal(memoryAddress)) {
			address = Integer.parseInt(memoryAddress, 10);
		} else if (isHex(memoryAddress)) {
			if (memoryAddress.startsWith("0x"))
				memoryAddress = memoryAddress.substring(2);
			address = Integer.parseInt(memoryAddress, 16);
		} else {
			throw new MemoryAddressException("Invalid memory address.");
		}

		if (address < 0)
			throw new MemoryAddressException("Memory address cannot be negative.");

		String bitString = Integer.toBinaryString(address);

		if (bitString.length() > 32)
			throw new MemoryAddressException("Memory address cannot be greater than 32 bits.");

		StringBuilder sb = new StringBuilder(ZERO_BITS);
		sb.replace(sb.length() - bitString.length(), sb.length(), bitString);
		this.bitString = sb.toString();
	}

	public String getBitString() {
		return bitString;
	}

	private static boolean isDecimal(String s) {
		return s.matches(DEC_REGEX);
	}

	private static boolean isHex(String s) {
		return s.matches(HEX_REGEX);
	}

	public static boolean isValid(String s) {
		return (isHex(s) || isDecimal(s)) && !s.contains("-");
	}

	@Override
	public String toString() {
		return Integer.toHexString(address);
	}

	public class MemoryAddressException extends Exception {
		private static final long serialVersionUID = 1L;

		public MemoryAddressException(String message) {
			super(message);
		}
	}

}
