package de.hoehne.stackit.test.entity;

import java.time.Duration;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Name {

	private String greet;
	private String name;
	private Duration duration;
	
	public String getPodName() {
		return EnvironmentHelper.getPodName();
	}
	
	public String getHostName() {
		return EnvironmentHelper.getHostName();
	}
	
	public String getHostIP() {
		return EnvironmentHelper.getHostIP();
	}
	
	public List<String> getEnvironment() {
		return EnvironmentHelper.getEnv();

	}
}
