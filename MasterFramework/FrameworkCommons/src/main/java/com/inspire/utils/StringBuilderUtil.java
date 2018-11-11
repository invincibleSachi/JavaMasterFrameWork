package com.inspire.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class StringBuilderUtil {
	StringBuilder sb = new StringBuilder();
	public static Logger log = MasterLogger.getInstance();

	public String replace(String original, String replaceable) {
		sb.append(original);
		int[] index = getAllIndexes(original, "#");
		sb.replace(index[0], index[1] + 1, replaceable);
		return sb.toString();
	}

	public int[] getAllIndexes(String s, String searchkey) {
		int[] index = new int[2];
		Pattern p = Pattern.compile(searchkey);
		Matcher m = p.matcher(s);
		int i = 0;
		while (m.find()) {
			index[i++] = m.start();
		}
		return index;
	}

	public static String getAbsoluteQuery(String propfileName, String propertyName,
			Map<String, String> valuestoReplace) {
		String sqlQuery = PropertyFileUtils.getProperty(propfileName, propertyName);
		if (valuestoReplace != null) {
			for (Map.Entry<String, String> entry : valuestoReplace.entrySet()) {
				String keyToReplace = "#" + entry.getKey() + "#";
				sqlQuery = sqlQuery.replaceAll(keyToReplace, entry.getValue());
			}
		}
		return sqlQuery;
	}

	public static String getreplacedString(String original, String toReplace) {
		StringBuilderUtil sbUtils = new StringBuilderUtil();
		return sbUtils.replace(original, toReplace);
	}

	public static String replaceAllString(String original, String pattern, String toReplace) {
		return original.replaceAll(Pattern.quote(pattern), toReplace);
	}

	public static String removeDoubleQuotes(String s) {
		return s.replace("\"", "");
	}
}
