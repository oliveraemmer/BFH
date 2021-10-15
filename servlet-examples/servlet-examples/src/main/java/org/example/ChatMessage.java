package org.example;

import java.time.LocalDateTime;

public class ChatMessage {

	private final LocalDateTime date;
	private final String text;

	public ChatMessage(String text) {
		this.date = LocalDateTime.now().withNano(0);
		this.text = text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
}
