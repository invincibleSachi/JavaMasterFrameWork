package com.inspire.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.fluttercode.datafactory.impl.DataFactory;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi TestDataGenerator generates random test data at
 *         runtime
 *
 */

public class TestDataGenerator {
	public static Logger log = MasterLogger.getInstance();
	static ArrayList<Long> RandNumbers = new ArrayList<Long>();
	DataFactory df = new DataFactory();

	public static String createText() {
		String uuid = UUID.randomUUID().toString();
		return uuid.toString();
	}

	public String getValidFirstName() {

		return df.getFirstName();
	}

	public String getValidLastName() {

		return df.getLastName();
	}

	public String getAboutme(int length) {

		return df.getRandomWord(length);
	}

	public String getValidAddress1() {

		return df.getAddress();
	}

	public String getValidAddress2() {

		return df.getAddressLine2();
	}

	public String getValidCity() {

		return df.getCity();
	}

	public static String getRandomWord(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	public static long getRandomEpochTime() {
		// from past 24 hours
		Random random = new Random();
		long rand = random.nextInt(86400);
		long epochtime = (System.currentTimeMillis() / 1000) * 1000;
		epochtime = epochtime - rand * 1000;
		return epochtime;

	}

	public String getRandomChars(int length) {
		return df.getRandomChars(length);
	}

	public String getValidGender(String[] gender) {
		int min = 1;
		int max = gender.length;

		Random random = new Random();
		int randomNum = random.nextInt((max - min) + 1) + min;
		String desiredGender = gender[randomNum - 1];
		return desiredGender;
	}

	public String getValidLanguagePref(String[] languagePref) {
		int min = 1;
		int max = languagePref.length;

		Random random = new Random();
		int randomNum = random.nextInt((max - min) + 1) + min;
		String desiredlang = languagePref[randomNum - 1];
		return desiredlang;
	}

	public String getEmptyString() {
		return "";
	}

	public String getCompanyName() {

		return df.getBusinessName();
	}

	public String getValidDOB(Date StartDate, Date EndDate, String DateFormat) {
		Date dte = df.getDateBetween(StartDate, EndDate);
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		String strDate = sdf.format(dte);
		return strDate;

	}

	public String getValidIndiaMobileNo() {
		int size = 10;
		double min = Math.pow(10, size - 1) * 7;
		double max = Math.pow(10, (size)) - 1;

		Random random = new Random();
		long randomValue = (long) (min + (long) (random.nextDouble() * (max - min)));

		return Long.toString(randomValue);

	}

	public String getValidIndiaMobileNoWithCountryCode() {
		int size = 10;
		double min = Math.pow(10, size - 1) * 7;
		double max = Math.pow(10, (size)) - 1;

		Random random = new Random();
		long randomValue = (long) (min + (long) (random.nextDouble() * (max - min)));

		String DesiredMobileNo = "+91" + Long.toString(randomValue);

		return DesiredMobileNo;

	}

	public String getValidInternationalMobileNoWithCountryCode(String CountryCode, int size) {

		double min = Math.pow(10, size - 1) * 7;
		double max = Math.pow(10, (size)) - 1;

		Random random = new Random();
		long randomValue = (long) (min + (long) (random.nextDouble() * (max - min)));
		String DesiredMobileNo = CountryCode + Long.toString(randomValue);

		return DesiredMobileNo;
	}

	public static Long getAnyRandomNumeric(int size) {
		double min = Math.pow(10, size - 1);
		double max = Math.pow(10, (size)) - 1;

		Random random = new Random();
		long randomValue = (long) (min + (long) (random.nextDouble() * (max - min)));
		while(RandNumbers.contains(randomValue)) {
			randomValue = (long) (min + (long) (random.nextDouble() * (max - min)));
		}
		RandNumbers.add(randomValue);

		return randomValue;
	}
	
	public static String getAnyRandomNumericStr(int size) {
		return Long.toString(getAnyRandomNumeric(size));
	}

	public String getValidCountryName() {
		Random random = new Random();
		int min = 1;
		int max = 250;
		int randomNum = random.nextInt((max - min) + 1) + min;
		String[] countries = Locale.getISOCountries();
		String countryCd = countries[randomNum];
		Locale loc = new Locale("", countryCd);
		String CountryName = loc.getDisplayCountry();
		return CountryName;
	}

	public String getValidInternationalLanguage() {
		Random random = new Random();
		int min = 1;
		int max = 160;
		int randomNum = random.nextInt((max - min) + 1) + min;
		String[] languages = Locale.getISOLanguages();
		String languageCd = languages[randomNum];
		Locale loc = new Locale(languageCd);
		String language = loc.getDisplayLanguage();
		return language;
	}

	public String getRandomEmail() {

		return df.getEmailAddress();
	}

	public static String getRandomEmailID() {
		Random random = new Random();
		DataFactory df = new DataFactory();
		String[] Providers = { "gmail", "yahoo", "outlook", "msn", "inbox", "indiatimes", "zoho", "Aol", "Yandex.mail",
				"ymail", "rediff", "snapdeal", "freecharge", "hotmail", "me", "bellsouth", "charter", "earthlink",
				"talktalk", "sky", "orange", "freeserve", "hanmail", "orange", "live", "rambler", "skynet",
				"tvcablenet", "telenet", "fibertel", "ntlworld", "tiscali", "web", "arnet", "naver", "safemail", "wow",
				"games", "gmx", "hushmail", "inbox", "cox", "laposte" };
		String[] Domains = { "com", "in", "net", "co.in", "co.ke", "ac", "mil", "gov.in", "gov.us", "au", "ac.in",
				"co.uk", "be", "ru", "ar", "net.mx", "com.ar", "de", "fr", "uk" };

		int prvidx = random.nextInt(Providers.length);
		int domainidx = random.nextInt(Domains.length);
		String provider, Domain, FirstName, numbr, desiredEmail;

		provider = Providers[prvidx];
		Domain = Domains[domainidx];
		FirstName = "ndtest" + df.getFirstName() + df.getLastName();
		numbr = getAnyRandomNumericStr(10);

		int IncludeMidName = random.nextInt(2);
		if (IncludeMidName > 0) {
			String rndchars = df.getRandomWord(3);
			desiredEmail = FirstName + "." + rndchars + "." + numbr + "@" + provider + "." + Domain;
		} else {
			desiredEmail = FirstName + "." + numbr + "@" + provider + "." + Domain;
		}
		return desiredEmail.toLowerCase();
	}

	public String getRandomIndianState() {
		String[] ArrState = { "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chandigarh", "Chhatisgarh", "Dadar Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujrat", "Haryana",
				"Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnatka", "Kerala", "Lakshadweep",
				"MadhyaPradesh", "Manipur", "Meghlaya", "Manipur", "Mizoram", "Nagaland", "Odisha", "Puducherry",
				"Punjab", "Rajasthan", "Sikkim", "Telangana", "Tamilnadu", "Tripura", "UttarPradesh", "Uttarakhand",
				"West Bengal", "Others" };
		Random random = new Random();
		int index = random.nextInt(36);
		return ArrState[index];
	}

	public int getPostalCode(int size) {
		int min = (int) Math.pow(10, size - 1);
		int max = (int) (Math.pow(10, (size)) - 1);
		return df.getNumberBetween(min, max);
	}

	public String getDisplayName(int length) {
		return df.getRandomWord(length);
	}

	public String getPhotoURL() {
		String ID_Prefix = "302022773";
		int min = (int) Math.pow(10, 7);
		int max = (int) (Math.pow(10, 8) - 1);
		int numeric_Part1 = df.getNumberBetween(min, max);
		String FbID = ID_Prefix + String.valueOf(numeric_Part1);
		String P_Url_Fixed = " http://www.facebook.com/photo.php?fbid=";
		return P_Url_Fixed + FbID;

	}

	public String getSocialSrc(String[] SocialSrcList) {
		return SocialSrcList[df.getNumberBetween(0, SocialSrcList.length - 1)];
	}

	public String getSocialID() {
		return Integer.toString(df.getNumberBetween(100000000, 999999999));
	}

	public String getToken() {
		return df.getRandomChars(126);
	}

	public String getSocialSecret() {
		return df.getRandomChars(125);
	}

	public String getTokenExpiry(int month) {
		String DD, MM, YYYY;
		Calendar now = Calendar.getInstance();
		MM = Integer.toString(now.get(Calendar.MONTH) + month);
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		DD = Integer.toString(now.get(Calendar.DATE));
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		YYYY = Integer.toString(now.get(Calendar.YEAR));
		String dte = MM + "-" + DD + "-" + YYYY;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return (dte + " " + sdf.format(now.getTime()));
	}

	public static String getDob(double age) {
		String dob = "";
		Calendar now = Calendar.getInstance();
		int years = (int) age;
		int month = (int) (age - (int) age) * 100;
		int days = (int) (((age - (int) age) * 10000) % 100);

		now.add(Calendar.YEAR, -years);
		if (now.get(Calendar.MONTH) < 13) {
			now.add(Calendar.MONTH, -month);
		}

		if (now.get(Calendar.DATE) < 31) {
			now.add(Calendar.DATE, -days);
		}

		if (now.get(Calendar.MONTH) < 10) {
			dob = "0" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);
		} else {
			dob = now.get(Calendar.MONTH) + "/" + now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);
		}
		return dob;

	}

	public static String getprevDate(int olddate) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -olddate);
		String month = Integer.toString(now.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = Integer.toString(now.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		return now.get(Calendar.YEAR) + "-" + month + "-" + day;
	}

	public static Date convertString2Date(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date dte = null;
		try {
			dte = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			log.info("error while parsing date String pass correct date format" + e.getMessage());
		}
		return dte;

	}

	public static String getfutureDate(int adddate) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, +adddate);
		String month = Integer.toString(now.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = Integer.toString(now.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		return now.get(Calendar.YEAR) + "-" + month + "-" + day;
	}

	public static Long getepochtime(int adddate) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		now.add(Calendar.DATE, adddate);
		return now.getTimeInMillis();
	}

	public static Long getepochtimeAddMillis(long epoch, int milliSec) {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(epoch);
		now.add(Calendar.MILLISECOND, milliSec);
		return now.getTimeInMillis();
	}

	public static Long getepochtimeAddminutes(int addminutes) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		now.add(Calendar.MINUTE, addminutes);
		return now.getTimeInMillis();
	}

	public static Long getepochtimemidnight(int adddate) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		now.add(Calendar.DATE, +adddate);
		String month = Integer.toString(now.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = Integer.toString(now.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTimeInMillis();
	}

	public static String getdateFromEpoch(String epochStr, String timeZone) {
		long epoch = Long.parseLong(epochStr);
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		Date date = new Date(epoch);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formatted = format.format(date);
		return formatted;
	}

	public static String getdateFromEpoch(long epochStr, String timeZone) {
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		Date date = new Date(epochStr);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formatted = format.format(date);
		return formatted;
	}
	
	public static String getDateAddhours(int hours,String timeZone) {
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		long epoch=getepochtimeAddminutes(60*hours);
		Date date = new Date(epoch);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formatted = format.format(date);
		return formatted;
	}

	public static String getdateFromEpoch(long epochStr, String timeZone, String dateFormat) {
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		Date date = new Date(epochStr);
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formatted = format.format(date);
		return formatted;
	}

	public static String getdateFromEpoch1(String epochStr, String timeZone) {
		long epoch = Long.parseLong(epochStr);
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		Date date = new Date(epoch);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss.SSS");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formatted = format.format(date);
		return formatted;
	}

	public static Long getFirstDayOfWeek() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static Long getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static String formatDate(Date dte, String format) {
		DateFormat dformat = null;
		if (format == null) {
			dformat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		} else {
			dformat = new SimpleDateFormat(format);
		}

		String formatted = dformat.format(dte);
		return formatted;
	}

	public static Date addhours2Date(int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		Date date = calendar.getTime();
		return date;
	}

	public static Date addminuts2Date(int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, minutes);
		Date date = calendar.getTime();
		return date;
	}

	public static String getformatedDateinTimeZone(Date date, String format, String timeZone) {
		DateFormat df = new SimpleDateFormat(format);
		if (timeZone.equals("")) {
			df.setTimeZone(TimeZone.getDefault());
		} else {

			df.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return df.format(date);
	}

	public static String getformatedDateinTimeZone(String date, String format, String timeZone) {
		DateFormat df = new SimpleDateFormat(format);
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		return df.format(date);
	}

	public static String getDateTimeStampinTimeZone(String format, String timeZone) {
		Date dte = new Date();
		DateFormat df = new SimpleDateFormat(format);
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}

		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		return df.format(dte);
	}

	public static long getepochFromDate(String date, String format, String timeZone) {
		if(format==null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		DateFormat df = new SimpleDateFormat(format);
		log.info("converting date " + date + " to epoch");
		if (timeZone == null || timeZone.equals("")) {
			timeZone = "Etc/UTC";
		}
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		long e = 0;
		try {
			Date dateformated = df.parse(date);
			e = dateformated.getTime();
		} catch (ParseException ex) {
			log.info("date formatting failed passed date is invalid format expected " + format + " found " + date
					+ " exception msg " + ex.toString());
		}

		return e;
	}

}
