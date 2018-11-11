package com.inspire.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;


/**
 * @author sachi
 *
 */
public class RandomPasswordGenerator {
	public String getRandomPassword(int lowerCaseCounts, int UperCaseCounts, int NumericCounts, int SpecialCharCount) {

		char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z' };
		char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		char[] specials = { '!', '@', '#', '?', '%', '&', '-', '(', ')', '{', '}', '.', '~' };

		int rnd;
		Random random = new Random();
		ArrayList<Character> Password = new ArrayList<Character>();
		String newPassword = "";
		if (lowerCaseCounts > 0) {
			for (int i = 0; i < lowerCaseCounts; i++) {
				rnd = random.nextInt(26);
				Password.add(lower[rnd]);
			}
		}

		if (UperCaseCounts > 0) {
			for (int i = 0; i < UperCaseCounts; i++) {
				rnd = random.nextInt(26);
				Password.add(upper[rnd]);
			}
		}

		if (NumericCounts > 0) {
			for (int i = 0; i < NumericCounts; i++) {
				rnd = random.nextInt(10);
				Password.add(digits[rnd]);
			}

		}

		if (SpecialCharCount > 0) {
			for (int i = 0; i < SpecialCharCount - 1; i++) {
				rnd = random.nextInt(14);
				Password.add(specials[rnd]);
			}
		}

		Collections.shuffle(Password);

		Iterator it = Password.iterator();
		while (it.hasNext()) {
			newPassword = newPassword + it.next();
		}
		System.out.println(newPassword);

		return newPassword;

	}

}
