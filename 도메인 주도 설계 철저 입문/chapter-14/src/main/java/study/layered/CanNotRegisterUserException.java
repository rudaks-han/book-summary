package study.layered;

public class CanNotRegisterUserException extends RuntimeException{
    public CanNotRegisterUserException(String msg) {
        super(msg);
    }
}
