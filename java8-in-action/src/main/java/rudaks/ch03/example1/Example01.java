package rudaks.ch03.example1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Example01 {

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }

    public static void main(String[] args) throws IOException {
        String oneLine = processFile((BufferedReader br) -> br.readLine());
    }
}
