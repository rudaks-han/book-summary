package com.example.solid.dip.before;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MessagePrinter {

    // write message to a file
    public void writeMessage(Message msg, String filename) throws IOException {
        Formatter formatter = new JSONFormatter();
        try(PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(formatter.format(msg));
            writer.flush();
        }
    }
}
