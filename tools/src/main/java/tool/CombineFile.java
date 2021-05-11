package tool;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombineFile {
    public static void main(String[] args) throws IOException {
        String bookName = "clean code";
        //String bookName = "Get Your Hands Dirty on Clean Architecture";

        String userDir = System.getProperty("user.dir");
        String outputFileName = bookName + ".md";
        String path = userDir + "/" + bookName;
        String result = "";

        List<File> files = Arrays.asList(new File(path).listFiles());
        Collections.sort(files);
        
        for (File file: files) {
            String absolutePath = file.getAbsolutePath();
            String fileName = file.getName();
            if (absolutePath.endsWith(".md") && !fileName.equals(bookName+".md")) {
                System.out.println(absolutePath);

                String data = FileUtils.readFileToString(new File(absolutePath), "UTF-8");
                result += data;
            }
        }

        FileUtils.writeStringToFile(new File(path + "/" + outputFileName), result, "UTF-8");
        System.out.println("file created : " + (path + "/" + outputFileName));
    }
}
