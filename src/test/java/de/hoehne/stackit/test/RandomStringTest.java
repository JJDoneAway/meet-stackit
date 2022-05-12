package de.hoehne.stackit.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringTest {

	@Test
	public void testRandomString() throws Exception {
		System.out.println(RandomStringUtils.randomAlphabetic(5, 15));
	}
}
