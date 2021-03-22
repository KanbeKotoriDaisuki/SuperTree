package frontend;

import common.BackendInterface;
import common.SuperHeroInterface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frontend {

  private static final String TITLE = "Marvel Heroes";
  private static final String[] LOGO = {
    "                                        ",
    "                  FULL                  ",
    "                                        ",
    "  ███  ███  ███  █████ ██   █████████   ",
    "  ████████ █████ ██  █████ █████   ██   ",
    "  ████████ ██ ██ ██  ██ ██ ██ ████ ██   ",
    "  ██ ██ ██ █████ █████  █████ ██   ██   ",
    "  ██ ██ █████ █████  ██  ███  ████ ████ ",
    "                                        ",
    "                 HEROES                 ",
    "                                        ",
  };
  private static final Color PRIMARY = Color.fromRgb(5, 0, 0);
  private static final Color SECONDARY = Color.fromRgb(3, 0, 0);
  private static final Color TERTIARY = Color.fromRgb(3, 2, 4);
  private static final Color CONTRAST = Color.White;

  private static Frontend singleton = new Frontend();

  private BackendInterface backend;

  private enum Mode {
    BASE,
    GET,
    EDIT,
    DISPLAY,
    ERROR,
  }

  private Integer testWidth;
  private Integer testHeight;

  private Mode mode = Mode.BASE;
  private ArrayList<String> arguments = new ArrayList<>();

  private Frontend() {}

  public static Frontend getInstance() {
    return singleton;
  }

  public static void refreshInstance() {
    singleton = new Frontend();
  }

  public Frontend setBackend(BackendInterface backend) {
    this.backend = backend;
    return this;
  }

  public Frontend setTestParams(int width, int height) {
    testWidth = width;
    testHeight = height;
    return this;
  }

  public Frontend run(Scanner scanner) {
    if (backend == null) {
      return this;
    }

    if (testWidth != null && testHeight != null) {
      Viewport.initialize(testWidth, testHeight, scanner);
    } else {
      Viewport.initialize(null, null, scanner);
    }
    Viewport vp = Viewport.getInstance();

    while (mode != null) {
      try {
        prepareViewport(vp);
        switch (mode) {
          case BASE:
            baseMode(vp);
            break;
          case GET:
            getMode(vp);
            break;
          case EDIT:
            editMode(vp);
            break;
          case DISPLAY:
            displayMode(vp);
            break;
          case ERROR:
            errorMode(vp);
            break;
        }
      } catch (Exception exception) {
        if (exception instanceof FrontendException) {
          arguments.add(0, "external");
          arguments.add(1, exception.getMessage());
        } else {
          arguments.add(0, "internal");
          arguments.add(1, exception.toString());
        }
        arguments.add(2, mode.toString());
        mode = Mode.ERROR;
      }
    }

    cleanViewport(vp);
    vp.close();

    return this;
  }

  protected void baseMode(Viewport vp) {
    DSLanguage language = new DSLanguage(mode.toString());

    language.addGrammar(
      "a(?:\\s+(?<option>a|b|c)\\s+(?<value>\"[^\"]*\"|\\S+))?",
      matcher -> {
        mode = Mode.GET;
        arguments.clear();
        if (matcher.group("option") != null) {
          arguments.add(matcher.group("option"));
          arguments.add(matcher.group("value"));
        }
      },
      "By default, enter the selection page.\n" +
      "Option a: show a hero by the given name.\n" +
      "Option b: show a hero by the given strength.\n" +
      "Option c: show heroes in the given range of power level [a,b]."
    );
    language.addGrammar(
      "b",
      matcher -> {
        mode = Mode.EDIT;
        arguments.clear();
      },
      "Enter the selection page of the EDIT mode."
    );
    language.addGrammar(
      "c(?:\\s+(?<option>a|b|c))?",
      matcher -> {
        mode = Mode.DISPLAY;
        arguments.clear();
        if (matcher.group("option") != null) {
          arguments.add(matcher.group("option"));
        }
      },
      "By default, enter the selection page.\n" +
      "Option a: show the search tree in pre order.\n" +
      "Option b: show the search tree in post order.\n" +
      "Option c: show the search tree in level order."
    );
    language.addGrammar(
      "x",
      matcher -> {
        mode = null;
      },
      "Exit the program."
    );

    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(PRIMARY, CONTRAST);
        vp.drawFilledRect(1, 2, 0, -1);
        vp.setColors(CONTRAST, PRIMARY);
        vp.setBold(true);
        for (int i = 0; i < LOGO.length; i++) {
          vp.drawSingleLineText(
            (vp.getWidth() - LOGO[i].length() - 40) / 2 + 1,
            (vp.getHeight() - LOGO.length) / 2 + i,
            LOGO[i]
          );
        }
      }
    );

    drawManual(vp, language);

    language.execute(vp.pause(null));
  }

  protected void getMode(Viewport vp) {
    DSLanguage language = new DSLanguage(mode.toString());

    language.addGrammar(
      "a\\s+(?<name>\"[^\"]*\"|\\S+)",
      matcher -> {
        mode = Mode.GET;
        arguments.clear();
        arguments.add("a");
        arguments.add(matcher.group("name"));
      },
      "Show a hero by the given name."
    );
    language.addGrammar(
      "b\\s+(?<strength>\\S+)",
      matcher -> {
        mode = Mode.GET;
        arguments.clear();
        arguments.add("b");
        arguments.add(matcher.group("strength"));
      },
      "Show a hero by the given strengh."
    );
    language.addGrammar(
      "c\\s+(?<range>\\S+)",
      matcher -> {
        mode = Mode.GET;
        arguments.clear();
        arguments.add("c");
        arguments.add(matcher.group("range"));
      },
      "Show heroes in the given range of power level [a,b]."
    );
    language.addGrammar(
      "d",
      matcher -> {
        mode = Mode.GET;
        arguments.clear();
      },
      "Show the total number of heroes."
    );
    language.addGrammar(
      "x",
      matcher -> {
        mode = Mode.BASE;
        arguments.clear();
      },
      "Return to BASE mode."
    );

    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(PRIMARY, CONTRAST);
        vp.drawFilledRect(1, 2, 0, -1);

        if (arguments.size() != 0) {
          switch (arguments.get(0)) {
            case "a":
            case "b":
              SuperHeroInterface hero;
              try {
                if (arguments.get(0).compareTo("a") == 0) {
                  String name = removeQuotes(arguments.get(1));
                  hero = backend.getHero(name);
                } else {
                  hero = backend.getHero(Integer.valueOf(arguments.get(1)));
                }
              } catch (NoSuchElementException e) {
                arguments.clear();
                throw new FrontendException(
                  "No Hero is found with the given condition"
                );
              }

              drawHero(
                vp,
                hero.getSuperheroName(),
                statisticsArray(
                  hero.getSpeed(),
                  hero.getStrength(),
                  hero.getUsefulness(),
                  hero.getIntelligence()
                ),
                hero.getDescription()
              );

              break;
            case "c":
              int start, end;
              Pattern rangePattern = Pattern.compile(
                "\\[(?<low>[0-9]+),(?<high>[0-9]+)\\]"
              );
              Matcher matcher = rangePattern.matcher(arguments.get(1));

              final String RANGEFORMATMSG =
                "Wrong format for range.\n" +
                "The corrent format is [start, end]";
              if (matcher.find()) {
                try {
                  start = Integer.parseInt(matcher.group("low"));
                  end = Integer.parseInt(matcher.group("high"));
                } catch (NumberFormatException exception) {
                  arguments.clear();
                  throw new FrontendException(RANGEFORMATMSG);
                }
              } else {
                arguments.clear();
                throw new FrontendException(RANGEFORMATMSG);
              }
              vp.drawMultiLineText(
                2,
                3,
                40,
                backend
                  .getHeroesInRange(start, end)
                  .stream()
                  .map(x -> x.getSuperheroName())
                  .reduce("", (a, b) -> a + " " + b)
              );

              break;
            case "d":
              vp.drawSingleLineText(
                2,
                2,
                "Number of Heroes: " + backend.getNumberOfHeroes() + "."
              );
              break;
          }
        }
      }
    );

    drawManual(vp, language);

    language.execute(vp.pause(null));
  }

  protected void editMode(Viewport vp) {
    DSLanguage language = new DSLanguage(mode.toString());

    language.addGrammar(
      "a\\s+(?<name>\"[^\"]*\"|\\S+)",
      matcher -> {
        mode = Mode.EDIT;
        arguments.clear();
        arguments.add("a");
        arguments.add(matcher.group("name"));
      },
      "Add a hero of given name."
    );
    language.addGrammar(
      "b\\s+(?<name>\"[^\"]*\"|\\S+)",
      matcher -> {
        mode = Mode.EDIT;
        arguments.clear();
        arguments.add("b");
        arguments.add(matcher.group("name"));
      },
      "Edit a hero of given name."
    );
    if (arguments.size() > 1) {
      language.addGrammar(
        "c\\s+(?<statistic>sp|st|us|it)\\s+(?<value>[0-9]+)",
        matcher -> {
          mode = Mode.EDIT;
          arguments.add(matcher.group("statistic"));
          arguments.add(matcher.group("value"));
        },
        "Set a statistic (sp/st/us/it) of the current hero."
      );
      language.addGrammar(
        "d\\s+(?<description>.+)",
        matcher -> {
          mode = Mode.EDIT;
          arguments.add("ds");
          arguments.add(matcher.group("description"));
        },
        "Set the description of the current hero."
      );
      language.addGrammar(
        "e",
        matcher -> {
          mode = Mode.EDIT;
          arguments.add(0, "e" + arguments.remove(0));
          arguments.add(arguments.get(1));
        },
        "Confirm changes to the current hero."
      );
    }
    language.addGrammar(
      "x",
      matcher -> {
        mode = Mode.BASE;
        arguments.clear();
      },
      "Return to BASE mode and discard any change."
    );

    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(PRIMARY, CONTRAST);
        vp.drawFilledRect(1, 2, 0, -1);
      }
    );

    if (arguments.size() > 1) {
      String name = removeQuotes(arguments.get(1));
      int[] statistics = new int[] { 0, 0, 0, 0 };
      String description = "";

      SuperHeroInterface hero;
      if (
        arguments.get(0).compareTo("a") == 0 ||
        arguments.get(0).compareTo("ea") == 0
      ) {
        try {
          hero = backend.getHero(name);
          arguments.clear();
          throw new FrontendException(
            "There is already a hero with the name " + name
          );
        } catch (NoSuchElementException exception) {}
      } else if (
        arguments.get(0).compareTo("b") == 0 ||
        arguments.get(0).compareTo("eb") == 0
      ) {
        try {
          hero = backend.getHero(name);
          statistics =
            statisticsArray(
              hero.getSpeed(),
              hero.getStrength(),
              hero.getUsefulness(),
              hero.getIntelligence()
            );
          description = hero.getDescription();
        } catch (NoSuchElementException exception) {
          arguments.clear();
          throw new FrontendException("There is no hero with the name " + name);
        }
      }

      for (int i = 2; i < arguments.size(); i += 2) {
        switch (arguments.get(i)) {
          case "sp":
            statistics[0] = Integer.valueOf(arguments.get(i + 1));
            break;
          case "st":
            statistics[1] = Integer.valueOf(arguments.get(i + 1));
            break;
          case "us":
            statistics[2] = Integer.valueOf(arguments.get(i + 1));
            break;
          case "it":
            statistics[3] = Integer.valueOf(arguments.get(i + 1));
            break;
          case "ds":
            description = arguments.get(i + 1);
            break;
        }
      }
      if (arguments.get(0).compareTo("ea") == 0) {
        backend.addHero(name, statistics, description);
        name += " added";
      } else if (arguments.get(0).compareTo("eb") == 0) {
        backend.editHero(name, statistics, description);
        name += " edited";
      }
      drawHero(vp, name, statistics, description);
    }
    drawManual(vp, language);

    language.execute(vp.pause(null));
  }

  protected void displayMode(Viewport vp) {
    DSLanguage language = new DSLanguage(mode.toString());

    language.addGrammar(
      "a",
      matcher -> {
        mode = Mode.DISPLAY;
        arguments.clear();
        arguments.add("a");
      },
      "Display in pre-order."
    );
    language.addGrammar(
      "b",
      matcher -> {
        mode = Mode.DISPLAY;
        arguments.clear();
        arguments.add("b");
      },
      "Display in post-order."
    );
    language.addGrammar(
      "c",
      matcher -> {
        mode = Mode.DISPLAY;
        arguments.clear();
        arguments.add("c");
      },
      "Display in level-order."
    );
    language.addGrammar(
      "x",
      matcher -> {
        mode = Mode.BASE;
        arguments.clear();
      },
      "Return to BASE mode."
    );

    if (arguments.size() > 0) {
      LinkedList<SuperHeroInterface> heroes;

      switch (arguments.get(0)) {
        case "a":
          heroes = backend.getPreOrder();
          break;
        case "b":
          heroes = backend.getPostOrder();
          break;
        case "c":
          heroes = backend.getLevelOrder();
          break;
        // this branch is only for compiler check, and should never be reached.
        default:
          heroes = new LinkedList<>();
      }

      drawThenRestoreStyles(
        vp,
        () -> {
          vp.setColors(PRIMARY, CONTRAST);
          vp.drawFilledRect(1, 2, 0, -1);

          vp.drawMultiLineText(
            2,
            3,
            40,
            heroes
              .stream()
              .map(x -> x.getSuperheroName())
              .reduce("", (a, b) -> a + " " + b)
          );
        }
      );
    }

    drawManual(vp, language);
    language.execute(vp.pause(null));
  }

  protected void errorMode(Viewport vp) {
    boolean isInternalError = arguments.get(0).compareTo("internal") == 0;
    String errorMsg;
    if (isInternalError) {
      errorMsg = "Application Error\n\n" + arguments.get(1);
    } else {
      errorMsg = arguments.get(1);
    }

    DSLanguage language = new DSLanguage(mode.toString());

    if (!isInternalError) {
      language.addGrammar(
        "a",
        matcher -> {
          mode = Mode.valueOf(arguments.get(2));
          arguments.remove(2);
          arguments.remove(1);
          arguments.remove(0);
        },
        "Return to the previous page.\n"
      );
    }
    language.addGrammar(
      "x",
      matcher -> {
        mode = null;
      },
      "Exit program.\n"
    );

    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(CONTRAST, Color.Red);
        vp.drawFilledRect(1, 2, 0, -1);
        vp.drawMultiLineText(2, 3, vp.getWidth() - 42, errorMsg);
      }
    );

    drawManual(vp, language);

    language.execute(vp.pause(null));
  }

  private void prepareViewport(Viewport vp) {
    vp.reset();
    drawTitleBar(vp);
    drawCommandLine(vp);
  }

  private void cleanViewport(Viewport vp) {
    vp.reset();
  }

  private void drawTitleBar(Viewport vp) {
    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(CONTRAST, PRIMARY);
        vp.drawFilledRect(1, 1, 0, 1);
        vp.drawSingleLineText(1, 1, TITLE);
        vp.drawSingleLineText(vp.getWidth() / 2, 1, mode.toString());
      }
    );
  }

  private void drawManual(Viewport vp, DSLanguage language) {
    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(CONTRAST, SECONDARY);
        vp.drawFilledRect(-40, 2, 0, -1);
        vp.setItalic(true);
        vp.drawSingleLineText(-23, 3, "MANUAL");
        vp.setItalic(false);
        vp.drawMultiLineText(-39, 5, 38, language.toString());
      }
    );
  }

  private void drawHero(
    Viewport vp,
    String name,
    int[] statistics,
    String description
  ) {
    drawThenRestoreStyles(
      vp,
      () -> {
        vp.setColors(PRIMARY, CONTRAST);

        vp.drawSingleLineText(2, 3, name);

        vp.drawSingleLineText(2, 5, "POWER " + calculatePower(statistics));
        vp.drawSingleLineText(2, 7, "SPEED " + statistics[0]);
        vp.drawSingleLineText(2, 9, "STRENGTH " + statistics[1]);
        vp.drawSingleLineText(2, 11, "USEFULNESS " + statistics[2]);
        vp.drawSingleLineText(2, 13, "INTELLIGENCE " + statistics[3]);

        vp.drawMultiLineText(2, 15, 40, description);

        vp.setColors(null, SECONDARY);

        vp.drawFilledRect(20, 5, 44, 5);
        vp.drawFilledRect(20, 7, 44, 7);
        vp.drawFilledRect(20, 9, 44, 9);
        vp.drawFilledRect(20, 11, 44, 11);
        vp.drawFilledRect(20, 13, 44, 13);

        vp.setColors(null, PRIMARY);

        vp.drawFilledRect(20, 5, 19 + calculatePower(statistics) / 4, 5);
        vp.drawFilledRect(20, 7, 19 + statistics[0], 7);
        vp.drawFilledRect(20, 9, 19 + statistics[1], 9);
        vp.drawFilledRect(20, 11, 19 + statistics[2], 11);
        vp.drawFilledRect(20, 13, 19 + statistics[3], 13);
      }
    );
  }

  private void drawCommandLine(Viewport vp) {
    vp.setColors(CONTRAST, TERTIARY);
    vp.drawFilledRect(1, 0, 0, 0);
    vp.drawSingleLineText(1, 0, " > ");
    vp.setCursorPosition(4, 0);
  }

  private void drawThenRestoreStyles(Viewport vp, Runnable r) {
    Color fgColor = vp.getFgColor(), bgColor = vp.getBgColor();
    boolean isBold = vp.isBold(), isItalic = vp.italic();
    r.run();
    vp.setColors(fgColor, bgColor);
    vp.setBold(isBold);
    vp.setItalic(isItalic);
  }

  private String removeQuotes(String input) {
    if (
      input.length() > 1 &&
      input.charAt(0) == '"' &&
      input.charAt(input.length() - 1) == '"'
    ) {
      return input.substring(1, input.length() - 1);
    } else return input;
  }

  private int[] statisticsArray(
    int speed,
    int strength,
    int usefulness,
    int intelligence
  ) {
    return new int[] { speed, strength, usefulness, intelligence };
  }

  private int calculatePower(int[] statistics) {
    return statistics[0] + statistics[1] + statistics[2] + statistics[3];
  }
}
