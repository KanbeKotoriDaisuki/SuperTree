import frontend.Frontend;
import frontend.TestFrontend.DummyBackend;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Frontend
      .getInstance()
      .setBackend(new DummyBackend())
      .run(new Scanner(System.in));
  }
}
