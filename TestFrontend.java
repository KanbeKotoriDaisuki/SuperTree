//package frontend;
//
//import common.BackendInterface;
//import common.SuperHeroInterface;

import java.io.StringReader;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for class Frontend
 */
public class TestFrontend {

//  public static class DummyHero implements SuperheroInterface {
//
//    private static int IdCounter = 0;
//    private int id = IdCounter++;
//
//    private String name = "TestHero" + id;
//    private int speed = id;
//    private int strength = id;
//    private int usefulness = id;
//    private int intelligence = id;
//    private String description = "This is dummy hero #" + id;
//
//    public DummyHero() {}
//
//    public DummyHero(String name, int[] statistics, String description) {
//      IdCounter--;
//      this.name = name;
//      this.speed = statistics[0];
//      this.strength = statistics[1];
//      this.usefulness = statistics[2];
//      this.intelligence = statistics[3];
//      this.description = description;
//    }

//    @Override
//    public int compareTo(SuperheroInterface o) {
//      return getSuperheroName().compareTo(o.getSuperheroName());
//    }
//
//    @Override
//    public String getSuperheroName() {
//      return name;
//    }
//
//    @Override
//    public int getPower() {
//      return speed + strength + usefulness + intelligence;
//    }
//
//    @Override
//    public String getDescription() {
//      return description;
//    }
//
//    @Override
//    public int getSpeed() {
//      return speed;
//    }
//
//    @Override
//    public int getStrength() {
//      return strength;
//    }
//
//    @Override
//    public int getUsefulness() {
//      return usefulness;
//    }
//
//    @Override
//    public int getIntelligence() {
//      return intelligence;
//    }
//
//    public static void resetCounter() {
//      IdCounter = 0;
//    }
//  }
//
//  public static class DummyBackend implements BackendInterface {
//
//    private LinkedList<SuperheroInterface> heroes = new LinkedList<>();
//
//    public DummyBackend() {
//      DummyHero.resetCounter();
//      for (int i = 0; i < 25; i++) {
//        heroes.add(new DummyHero());
//      }
//    }
//
//    @Override
//    public SuperheroInterface getHero(String name)
//      throws NoSuchElementException {
//      for (SuperheroInterface hero : heroes) {
//        if (hero.getSuperheroName().compareTo(name) == 0) {
//          return hero;
//        }
//      }
//      throw new NoSuchElementException();
//    }
//
//    @Override
//    public SuperheroInterface getHero(int strength)
//      throws NoSuchElementException {
//      for (SuperheroInterface hero : heroes) {
//        if (hero.getStrength() == strength) {
//          return hero;
//        }
//      }
//      throw new NoSuchElementException();
//    }
//
//    @Override
//    public SuperheroInterface addHero(
//      String name,
//      int[] statistics,
//      String description
//    )
//      throws InvalidParameterException {
//      DummyHero hero = new DummyHero(name, statistics, description);
//      heroes.add(hero);
//      return hero;
//    }
//
//    @Override
//    public SuperheroInterface editHero(
//      String name,
//      int[] statistics,
//      String description
//    )
//      throws InvalidParameterException {
//      for (SuperheroInterface hero : heroes) {
//        if (hero.getSuperheroName().compareTo(name) == 0) {
//          heroes.remove(hero);
//          DummyHero newHero = new DummyHero(name, statistics, description);
//          heroes.add(newHero);
//          return newHero;
//        }
//      }
//      throw new InvalidParameterException();
//    }
//
//    @Override
//    public LinkedList<SuperheroInterface> getPostOrder() {
//      return heroes;
//    }
//
//    @Override
//    public LinkedList<SuperheroInterface> getPreOrder() {
//      return heroes;
//    }
//
//    @Override
//    public LinkedList<SuperheroInterface> getLevelOrder() {
//      return heroes;
//    }
//
//    @Override
//    public LinkedList<SuperheroInterface> getHeroesInRange(int start, int end) {
//      return heroes;
//    }
//
//    @Override
//    public int getNumberOfHeroes() {
//      return heroes.size();
//    }
//  }
//
 private BackendInterface backend;

/**
 * Prepare the backend that is taken by frontend to test its functionality
 */
 @BeforeEach
 public void initialize() {
   backend = new Backend();
   Frontend.refreshInstance();
 }

 /**
  * Test if exit works fine
  */
 @Test
 @Timeout(1)
 public void testExit() {
   Frontend
     .getInstance()
     .setTestParams(10, 10)
     .setBackend(backend)
     .run(new Scanner(new StringReader("x\n")));
 }

 /**
  * Test if base mode correctly exits
  */
 @Test
 @Timeout(1)
 public void testBase() {
   Frontend
     .getInstance()
     .setTestParams(10, 10)
     .setBackend(backend)
     .run(new Scanner(new StringReader("a\nx\nx\n")));
 }

 /**
  * Test if an invalid command result in a recoverable error.
  */
 @Test
 @Timeout(1)
 public void testError() {
   Frontend
     .getInstance()
     .setTestParams(10, 10)
     .setBackend(backend)
     .run(new Scanner(new StringReader("nosuchcommand\na\nx\n")));
 }
}