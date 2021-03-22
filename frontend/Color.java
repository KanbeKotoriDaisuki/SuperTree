package frontend;

public class Color {

  public static final Color Black = new Color("30", "40");
  public static final Color Red = new Color("31", "41");
  public static final Color Green = new Color("32", "42");
  public static final Color Yellow = new Color("33", "43");
  public static final Color Blue = new Color("34", "44");
  public static final Color Magenta = new Color("35", "45");
  public static final Color Cyan = new Color("36", "46");
  public static final Color White = new Color("37", "47");

  public static Color fromRgb(int r, int g, int b) {
    return new Color(
      "38;5;" + (16 + 36 * r + 6 * g + b),
      "48;5;" + (16 + 36 * r + 6 * g + b)
    );
  }

  public final String fgValue;
  public final String bgValue;

  private Color(String foreground, String background) {
    this.fgValue = foreground;
    this.bgValue = background;
  }
}
