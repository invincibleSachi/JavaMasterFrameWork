package com.inspire.tests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.inspire.abstest.AbstractTest;

public class test extends AbstractTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Matcher m = null;
		String trnPattern="\\[[A-Za-z0-9_@#$%^&!-=,]{24,30}\\]";
		String line="2018-03-30 06:53:36,753 INFO  [15XdQdqG2cTtONiFMJ4kMd79] TracingFilter:80 - request time:     1ms for: v1/internal/health";
		m = Pattern.compile(trnPattern).matcher(line);
		if (m.find()) {
			System.out.println(m.group(0));
		}

	}

}
