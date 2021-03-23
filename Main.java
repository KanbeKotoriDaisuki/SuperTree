//import Frontend;
//import frontend.TestFrontend.DummyBackend;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
//	  export WIDTH=$(tput cols);
//	  export HEIGHT=$(tput lines);
    Frontend
      .getInstance()
      .setBackend(new Backend(args))
      .run(new Scanner(System.in));
  }
}