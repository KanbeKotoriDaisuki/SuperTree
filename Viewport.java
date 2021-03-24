//package frontend;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Viewport for the frontend
 */
public class Viewport implements AutoCloseable {

  private static Viewport singleton = new Viewport();

  private Scanner stdin;

  private Color fgColor = Color.White;
  private Color bgColor = Color.Black;
  private boolean bold = false;
  private boolean italic = false;

  private int width = 80;
  private int height = 24;

  /**
   * Viewport should not be initialized from external.
   */
  private Viewport() {
    System.out.println("\u001b[?1049h\u001b[H");
  }

  /**
   * Coordinate helper
   * @param x
   * @return
   */
  private int realX(int x) {
    return (x <= 0) ? width + x : x;
  }

  /**
   * Coordinate helper
   */
  private int realY(int y) {
    return (y <= 0) ? height + y : y;
  }

  /**
   * Get the single instance of the viewport
   */
  public static Viewport getInstance() {
    if (singleton == null) {
      throw new RuntimeException(
        "Viewport should not be called without initialization"
      );
    }
    return singleton;
  }

  /**
   * Initialize the viewport
   * @param width if null, try env & user input
   * @param height if null, try env & user input
   * @param scanner source of user input for this viewport
   */
  public static void initialize(
    Integer width,
    Integer height,
    Scanner scanner
  ) {
    singleton = new Viewport();
    singleton.stdin = scanner;
    if (width == null || height == null) {
      try {
        singleton.width = Integer.parseInt(System.getenv("WIDTH"));
        singleton.height = Integer.parseInt(System.getenv("HEIGHT"));
      } catch (Exception exception) {
        System.out.print("Configure the width of application: ");
        singleton.width = singleton.stdin.nextInt();
        System.out.print("Configure the height of application: ");
        singleton.height = singleton.stdin.nextInt();
        singleton.stdin.nextLine();
      }
    } else {
      singleton.width = width;
      singleton.height = height;
    }

    singleton.reset();
  }

  /**
   * Clear everything from the screen, use alternative scroll buffer
   */
  public void reset() {
    // reset text styles
    System.out.print("\u001b[0m");
    // clear screen and scroll buffer
    System.out.print("\u001b[2J");
    // set cursor to home
    System.out.print("\u001b[H");
  }

  /**
   * Pause for an user input
   * @param msg
   * @return
   */
  public String pause(String msg) {
    if (msg != null) {
      System.out.print(msg);
    }
    String result = stdin.nextLine();
    return result;
  }

  /**
   * Close the viewport, return to main scroll buffer
   */
  @Override
  public void close() {
    System.out.println("\u001b[?1049l");
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Color getFgColor() {
    return fgColor;
  }

  public Color getBgColor() {
    return bgColor;
  }

  public boolean isBold() {
    return bold;
  }

  public boolean italic() {
    return italic;
  }

  public void setColors(Color foreground, Color background) {
    if (foreground != null) {
      fgColor = foreground;
    }
    if (background != null) {
      bgColor = background;
    }
    System.out.print("\u001b[" + fgColor.fgValue + ";" + bgColor.bgValue + "m");
  }

  public void setBold(boolean flag) {
    if (flag != bold) {
      bold = flag;
      if (flag) {
        System.out.print("\u001b[1m");
      } else {
        System.out.print("\u001b[22m");
      }
    }
  }

  public void setItalic(boolean flag) {
    if (flag != italic) {
      italic = flag;
      if (flag) {
        System.out.print("\u001b[3m");
      } else {
        System.out.print("\u001b[23m");
      }
    }
  }

  /**
   * Set the cursor to position x:y.
   * @param x
   * @param y
   * @return
   */
  public boolean setCursorPosition(int x, int y) {
    x = realX(x);
    y = realY(y);
    if (x <= width && y <= height) {
      System.out.print("\u001b[" + y + ";" + x + "H");
      return true;
    } else {
      return false;
    }
  }

  public void saveCursorPosition() {
    System.out.print("\u001b[s");
  }

  public void restoreCursorPosition() {
    System.out.print("\u001b[u");
  }

  /**
   * Draw a filled rectangle with by a pair of points.
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  public void drawFilledRect(int x1, int y1, int x2, int y2) {
    x1 = realX(x1);
    y1 = realY(y1);
    x2 = realX(x2);
    y2 = realY(y2);
    saveCursorPosition();
    for (int i = y1; i <= y2 && i <= height; i++) {
      setCursorPosition(x1, i);
      for (int j = x1; j <= x2 && j <= width; j++) {
        System.out.print(" ");
      }
      if (i == 7) {
        System.out.print("");
      }
    }
    restoreCursorPosition();
  }

  /**
   * Draw a single line of text
   * @param x
   * @param y
   * @param msg
   */
  public void drawSingleLineText(int x, int y, String msg) {
    x = realX(x);
    y = realY(y);
    saveCursorPosition();
    setCursorPosition(x, y);
    for (int i = y; i < y + msg.length() && i <= width; i++) {
      System.out.print(msg.charAt(i - y));
    }
    restoreCursorPosition();
  }

  /**
   * Draw many lines of text with a specific width
   * @param x
   * @param y
   * @param width
   * @param msg
   * @return the height of the texts
   */
  public int drawMultiLineText(int x, int y, int width, String msg) {
    x = realX(x);
    y = realY(y);

    if (width <= 0) return 0;

    // process msg
    ArrayList<String> tokens = new ArrayList<>();
    for (String line : msg.split("\\n|\\r\\n")) {
      for (String token : line.split("\\s+")) {
        tokens.add(token);
      }
      tokens.add("\n");
    }
    tokens.remove(tokens.size() - 1);

    int lineCount = 0, lineWidth = 0, tokenIndex = 0;

    while (tokenIndex < tokens.size()) {
      if (tokens.get(tokenIndex).compareTo("\n") == 0) {
        // new line
        lineCount++;
        lineWidth = 0;
        tokenIndex++;
      } else if (tokens.get(tokenIndex).length() <= width) {
        // single line token
        if (
          lineWidth != 0 &&
          lineWidth + 1 + tokens.get(tokenIndex).length() > width
        ) {
          lineCount++;
          lineWidth = 0;
        }
        drawSingleLineText(
          (lineWidth == 0) ? x : x + lineWidth + 1,
          y + lineCount,
          tokens.get(tokenIndex)
        );
        lineWidth +=
          ((lineWidth == 0) ? 0 : 1) + tokens.get(tokenIndex).length();
        tokenIndex++;
      } else {
        // multi line token
        for (String line : tokens
          .get(tokenIndex)
          .split("(?<=\\G.{" + width + ",}\\s)")) {
          drawSingleLineText(x, y + lineCount++, line);
          lineWidth = line.length();
        }
        tokenIndex++;
      }
    }

    return lineCount;
  }
}
