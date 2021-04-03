package study.ch_10_4;

public class CanNotRegisterUserException extends RuntimeException{
    public CanNotRegisterUserException(String message) {
        super(message);
    }
}
