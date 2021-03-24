//import Frontend;
//import frontend.TestFrontend.DummyBackend;
import java.util.Scanner;

/**
 * The main class
 */
public class Main {

/**
 * The entry point of the application
 * @param args
 */
  public static void main(String[] args) {
//	  export WIDTH=$(tput cols);
//	  export HEIGHT=$(tput lines);
    Frontend
      .getInstance()
      .setBackend(new Backend(args))
      .run(new Scanner(System.in));
  }
}