package core;

public class MemoryAddress {

	private static final String DEC_REGEX = "^[0-9]+$";
	private static final String HEX_REGEX = "^(0[xX])?[0-9a-fA-F]+$";

	int address;

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
