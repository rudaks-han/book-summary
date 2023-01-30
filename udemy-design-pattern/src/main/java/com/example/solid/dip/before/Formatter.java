package com.example.solid.dip.before;

//Common interface for classes formatting Message object
public interface Formatter {
	
	String format(Message message) throws FormatException;
	
}
