//package frontend;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple domain specific language
 */
public class DSLanguage {

  /**
   * An internal class for false user inputs
   */
  @SuppressWarnings("serial")
  public class InvalidCommandException extends FrontendException {

    /**
     * Constructor that takes a command and a DSLanguage
     * @param command
     * @param language
     */
    public InvalidCommandException(String command, DSLanguage language) {
      super(
        "Receives user command\n\"" +
        command +
        "\",\n" +
        "but a valid command in the " +
        mode +
        " mode should have one of the following formats:\n" +
        grammarList
          .stream()
          .map(pattern -> grammarString(pattern))
          .reduce("", (a, b) -> a + "- " + b + "\n")
      );
    }
  }

  private ArrayList<Pattern> grammarList = new ArrayList<>();
  private ArrayList<Consumer<Matcher>> actionList = new ArrayList<>();
  private ArrayList<String> descriptionList = new ArrayList<>();

  private String mode;

  /**
   * Constructor that binds the new language with a mode
   */
  public DSLanguage(String mode) {
    this.mode = mode;
  }

  /**
   * Add a grammar to the language
   */
  public void addGrammar(
    String grammar,
    Consumer<Matcher> action,
    String description
  ) {
    grammarList.add(Pattern.compile(grammar));
    actionList.add(action);
    descriptionList.add(description);
  }

  /**
   * Execute a command
   * @param command
   */
  public void execute(String command) {
    command = command.trim();

    Matcher matcher;
    for (int i = 0; i < grammarList.size(); i++) {
      if ((matcher = grammarList.get(i).matcher(command)).matches()) {
        actionList.get(i).accept(matcher);
        return;
      }
    }
    throw new InvalidCommandException(command, this);
  }

  /**
   * Visualize the language for users.
   */
  @Override
  public String toString() {
    String result = "";

    for (int i = 0; i < grammarList.size(); i++) {
      result +=
        "> " +
        grammarString(grammarList.get(i)) +
        "\n" +
        descriptionList.get(i) +
        "\n";
    }

    return result;
  }

  /**
   * Helper for visualize grammars of the language
   */
  private static String grammarString(Pattern grammar) {
    String grammaString = grammar.toString();

    Pattern grammarNamePattern = Pattern.compile("^[a-zA-Z]+");
    Pattern grammarParamterPattern = Pattern.compile(
      "\\(\\?\\<(?<name>[^>]+)\\>"
    );

    Matcher commandNameMatcher = grammarNamePattern.matcher(grammaString);
    commandNameMatcher.find();
    String result = commandNameMatcher.group(0);

    Matcher commandParameterMatcher = grammarParamterPattern.matcher(
      grammaString
    );
    while (commandParameterMatcher.find()) {
      result += " [" + commandParameterMatcher.group("name") + "]";
    }

    return result;
  }
}