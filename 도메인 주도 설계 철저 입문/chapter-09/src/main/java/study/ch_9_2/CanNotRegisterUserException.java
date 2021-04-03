package study.ch_9_2;

public class CanNotRegisterUserException extends RuntimeException{
    public CanNotRegisterUserException(String message) {
        super(message);
    }
}
