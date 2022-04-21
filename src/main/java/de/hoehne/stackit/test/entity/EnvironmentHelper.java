package de.hoehne.stackit.test.entity;

import java.util.List;
import java.util.stream.Collectors;

public class EnvironmentHelper {

	private EnvironmentHelper() {
		// we don't need instances
	}

	public static List<String> getEnv() {
		return System.getenv().entrySet().stream().map(e -> "%s = %s".formatted(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}
}
