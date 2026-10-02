package com.sanjay.first;

import org.springframework.stereotype.Component;

@Component
public class Alien {
	final Laptop lap;

	Alien(Laptop lap) {
		this.lap = lap;
	}
	public void code() {
		lap.compile();
	}
}
