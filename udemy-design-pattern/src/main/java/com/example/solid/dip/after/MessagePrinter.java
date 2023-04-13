package com.example.solid.dip.after;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MessagePrinter {

    // write message to a file
    public void writeMessage(Message msg, Formatter formatter, PrintWriter writer) throws IOException {
        writer.println(formatter.format(msg));
        writer.flush();
    }
}
