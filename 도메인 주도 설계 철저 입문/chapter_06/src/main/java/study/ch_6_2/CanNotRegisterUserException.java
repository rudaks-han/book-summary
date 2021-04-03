package study.ch_6_2;

public class CanNotRegisterUserException extends RuntimeException{
    public CanNotRegisterUserException(String message) {
        super(message);
    }
}
