package generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GeneratePassword {
	private static final int NUMBER_NUM = 10;
	private static final int ALPHABET_NUM = 26;
	public static String getPassword(int digit, boolean number, boolean lower, boolean upper,
			boolean allchartype, boolean onlyonce) {
		char[] c = new char[NUMBER_NUM + ALPHABET_NUM * 2];
		List<Character> password;
		char[] array;
		int charnum = 0;
		int chartypenum = 0;
		if (number) {
			chartypenum++;
			charnum += NUMBER_NUM;
			for (int i = charnum - NUMBER_NUM; i < charnum; i++)
				c[i] = ((char)('0' + i - charnum + NUMBER_NUM));
		}
		if (lower) {
			chartypenum++;
			charnum += ALPHABET_NUM;
			for (int i = charnum - ALPHABET_NUM; i < charnum; i++)
				c[i] = ((char)('a' + i - charnum + ALPHABET_NUM));
		}
		if (upper) {
			chartypenum++;
			charnum += ALPHABET_NUM;
			for (int i = charnum - ALPHABET_NUM; i < charnum; i++)
				c[i] = ((char)('A' + i - charnum + ALPHABET_NUM));
		}
		if (chartypenum <= 0) return null;
		if (onlyonce && digit > charnum)
			digit = charnum;
		if (allchartype && digit < chartypenum)
			digit = chartypenum;
		password = new ArrayList<Character>(digit);
		if (allchartype) {
			if (number)
				password.add((char)('0' + NUMBER_NUM * Math.random()));
			if (lower)
				password.add((char)('a' + ALPHABET_NUM * Math.random()));
			if (upper)
				password.add((char)('A' + ALPHABET_NUM * Math.random()));
		}
		while (password.size() < digit) {
			char pass = 0;
			boolean decision = false;
			while (!decision) {
				pass = c[((int)(charnum * Math.random()))];
				decision = true;
				if (onlyonce && password.contains(pass))
					decision = false;
			}
			password.add(pass);
		}
		if (allchartype)
			Collections.shuffle(password);
		array = new char[digit];
		Iterator<Character> iterator = password.iterator();
		for (int i = 0; i < digit; i++)
			array[i] = iterator.next();
		return new String(array);
	}
}
