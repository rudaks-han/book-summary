package com.example.solid.dip.after;

//Common interface for classes formatting Message object
public interface Formatter {
	
	String format(Message message) throws FormatException;
	
}
