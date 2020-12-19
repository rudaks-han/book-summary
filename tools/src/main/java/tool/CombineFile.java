package tool;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CombineFile {
    public static void main(String[] args) throws IOException {
        String bookName = "Clean Architecture";

        String userDir = System.getProperty("user.dir");
        String outputFileName = bookName + ".md";
        String path = userDir + "/" + bookName;
        String result = "";

        File[] files = new File(path).listFiles();
        for (File file: files) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.endsWith(".md")) {
                System.out.println(absolutePath);

                String data = FileUtils.readFileToString(new File(absolutePath), "UTF-8");
                result += data;
            }
        }

        FileUtils.writeStringToFile(new File(path + "/" + outputFileName), result, "UTF-8");
        System.out.println("file created : " + (path + "/" + outputFileName));
    }
}
