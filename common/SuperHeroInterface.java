package common;

public interface SuperHeroInterface extends Comparable<SuperHeroInterface> {
  public String getSuperheroName();

  public int getPower();

  public String getDescription();

  public int getSpeed();

  public int getStrength();

  public int getUsefulness();

  public int getIntelligence();
}
