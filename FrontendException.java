//package frontend;

/**
 * Exceptions that derives from user behavior
 */
@SuppressWarnings("serial")
public class FrontendException extends RuntimeException {

  public FrontendException() {
    super();
  }

  public FrontendException(String msg) {
    super(msg);
  }
}
