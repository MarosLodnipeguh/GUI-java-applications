package SMS;

public class InvalidRecipientException extends RuntimeException{

    public InvalidRecipientException () {
        super("Invalid Recipent Number!");
    }
}
