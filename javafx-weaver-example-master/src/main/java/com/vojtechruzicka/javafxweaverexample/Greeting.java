package com.vojtechruzicka.javafxweaverexample;

public class Greeting {

	private final long id;
	private final String content;

	@Override
	public String toString() {
		return "Greeting{" +
				"id=" + id +
				", content='" + content + '\'' +
				'}';
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
