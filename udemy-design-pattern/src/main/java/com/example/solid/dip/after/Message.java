package com.example.solid.dip.after;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class Message {

	private String msg;
	
	private LocalDateTime timestamp;
	
	public Message(String msg) {
		this.msg = msg;
		this.timestamp = LocalDateTime.now(ZoneId.of("UTC"));
	}
}
