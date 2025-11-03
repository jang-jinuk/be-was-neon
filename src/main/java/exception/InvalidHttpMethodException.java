package exception;

public class InvalidHttpMethodException extends RuntimeException {

    public InvalidHttpMethodException() {
        super("지원하지 않는 HTTP 메서드입니다.");
    }
}
