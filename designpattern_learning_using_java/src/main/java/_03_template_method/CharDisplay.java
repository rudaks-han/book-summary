package _03_template_method;

public class CharDisplay extends AbstractDisplay {

    private char ch; // 표시해야 하는 문자

    public CharDisplay(char ch) {
        this.ch = ch;
    }

    @Override
    public void open() {
        System.out.print("<<"); // 시작 문자열 "<<"를 표시
    }

    @Override
    public void print() {
        // 필드에 저장해 둔 문자을 1회 표시
        System.out.print(ch);
    }

    @Override
    public void close() {
        // 종료 문자열 ">>"를 표시
        System.out.println(">>");
    }
}
