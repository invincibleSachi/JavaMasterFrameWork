package com.inspire.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inspire.abstest.AbstractTest;


public class Testsasdfas extends AbstractTest {

	@Test
	public void sampleTest() {
		Assert.assertEquals(5+4, 9,"actual is not as expected");
	}
	public static void main(String[] args) {
	}

}
