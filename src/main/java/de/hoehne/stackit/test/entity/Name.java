package de.hoehne.stackit.test.entity;

import java.time.Duration;

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
}
