//package common;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public interface BackendInterface {


  /**
   * Get a hero by strength.
   * @param strength
   * @return the hero found
   * @throws NoSuchElementException
   */
  public SuperheroInterface getHero(int strength) throws NoSuchElementException;

  /**
   * Add a new hero.
   * @param name the name of the new hero
   * @param statistics the four statistics, in order, of the new hero
   * @param description the description of the new hero
   * @return a deep copy of the newly created hero
   * @throws InvalidParameterException when the new hero conflicts with an existing hero
   */
  public SuperheroInterface addHero(
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
  public SuperheroInterface editHero(
    String name,
    int[] statistics,
    String description
  ) throws InvalidParameterException;

  public LinkedList<SuperheroInterface> getPostOrder();

  public LinkedList<SuperheroInterface> getPreOrder();

  public LinkedList<SuperheroInterface> getLevelOrder();

  public LinkedList<SuperheroInterface> getHeroesInRange(int start, int end);

  public int getNumberOfHeroes();

SuperheroInterface getHero(String hero) throws NoSuchElementException;
}
