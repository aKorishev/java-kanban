package tools;

public class ManagerSaveException extends Error{
    public ManagerSaveException(String message, Exception ex){
        super(message, ex);
    }
}
