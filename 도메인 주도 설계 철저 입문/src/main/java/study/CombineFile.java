package study;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CombineFile {
    public static void main(String[] args) throws IOException {
        String outputFileName = "도메인 주도 설계 철저 입문 (java).md";
        String path = "/Users/macbookpro/_WORK/_GIT/book-summary/도메인 주도 설계 철저 입문/docs";
        //String path = "/Users/macbookpro/_WORK/_GIT/ddd-study/docs";

        String result = "";
        for (int i=0; i<=15; i++) {
            if (i == 8) {
                continue;
            }

            String fileName = "chapter-" + (i<10 ? "0" + i : i) + ".md";

            String data = FileUtils.readFileToString(new File(path + "/" + fileName), "UTF-8");;

            result += data;
        }

        FileUtils.writeStringToFile(new File(path + "/" + outputFileName), result, "UTF-8");

        System.out.println("file created : " + (path + "/" + outputFileName));
    }
}
