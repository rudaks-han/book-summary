package rudaks;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class Test {

    public int count;

    public static void main(String[] args) {

        String specialChar = " @";
        //String text = "할머니가 방에+ 들어가신다";
        String text = "kmhan@spectra.co.kr";

        String [] result = StringUtils.split(text, specialChar);
        StringTokenizer tokenizer = new StringTokenizer(text, specialChar);
        while(tokenizer.hasMoreTokens()) {
            System.out.println("token => " + tokenizer.nextToken());
        }
        //Arrays.asList(result).forEach(System.out::println);

    }
}

