package de.hoehne.stackit.test.entity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentHelper {

	private static final List<String> environments = new ArrayList<>();

	static {
		System.getenv().entrySet().stream().map(e -> "%s = %s".formatted(e.getKey(), e.getValue()))
				.forEach(e -> environments.add(e));
	}

	private EnvironmentHelper() {
		// we don't need instances
	}

	public static List<String> getEnv() {
		return Collections.unmodifiableList(environments);
	}

	public static String getPodName() {
		return System.getenv("HOSTNAME");
	}

	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error("Not able to get the Hostname", e);
			return "unknown";
		}
	}

	public static String getHostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("Not able to get the Host IP", e);
			return "unknown";
		}

	}

}
