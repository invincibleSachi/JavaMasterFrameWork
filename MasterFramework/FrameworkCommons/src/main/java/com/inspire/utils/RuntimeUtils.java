package com.inspire.utils;

import java.io.IOException;

/**
 * @author sachi
 *
 */
public class RuntimeUtils {

	public static int exec(String command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			if (!p.isAlive()) {
				return p.exitValue();
			} else {
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int exec(String[] command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			if (!p.isAlive()) {
				return p.exitValue();
			} else {
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
