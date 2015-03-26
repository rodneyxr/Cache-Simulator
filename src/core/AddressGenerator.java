package core;

import java.util.Random;

public class AddressGenerator {

	public static void main(String[] args) {
		Random rand = new Random();
		int total = 5000;
		int last = 20000;
		for (int i = 0; i < total; i++) {
			int address = 0;
			float r;
			if ((r = rand.nextFloat() * 100) < 10)
				last -= 1024;
			else if (r > 90)
				last += 2048;
			address = Math.max((rand.nextInt(16) - 4) * 2 + last, 0);
			last = address;

			if (rand.nextBoolean())
				System.out.println(Integer.toHexString(address));
			else
				System.out.println(address);
		}
	}

}
