package common;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public interface BackendInterface {
  /**
   * Get a hero by name
   * @param name
   * @return the hero found
   * @throws NoSuchElementException
   */
  public SuperHeroInterface getHero(String name) throws NoSuchElementException;

  /**
   * Get a hero by strength.
   * @param strength
   * @return the hero found
   * @throws NoSuchElementException
   */
  public SuperHeroInterface getHero(int strength) throws NoSuchElementException;

  /**
   * Add a new hero.
   * @param name the name of the new hero
   * @param statistics the four statistics, in order, of the new hero
   * @param description the description of the new hero
   * @return a deep copy of the newly created hero
   * @throws InvalidParameterException when the new hero conflicts with an existing hero
   */
  public SuperHeroInterface addHero(
    String name,
    int[] statistics,
    String description
  ) throws InvalidParameterException;

  /**
   * Edit an existing hero.
   * @param name the new name of the hero
   * @param statistics the new four statistics, in order, of the hero
   * @param description the new description of the hero
   * @return a deep copy of the hero
   * @throws InvalidParameterException when the hero conflicts with an existing hero
   */
  public SuperHeroInterface editHero(
    String name,
    int[] statistics,
    String description
  ) throws InvalidParameterException;

  public LinkedList<SuperHeroInterface> getPostOrder();

  public LinkedList<SuperHeroInterface> getPreOrder();

  public LinkedList<SuperHeroInterface> getLevelOrder();

  public LinkedList<SuperHeroInterface> getHeroesInRange(int start, int end);

  public int getNumberOfHeroes();
}
