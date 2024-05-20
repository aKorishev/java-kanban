package tools;

public class ManagerLoadException extends Error {
    public ManagerLoadException(String message, Exception ex) {
        super(message, ex);
    }
}
