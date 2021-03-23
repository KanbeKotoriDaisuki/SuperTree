import java.util.List;

public interface SuperheroInterface extends Comparable<SuperheroInterface>{
  
  public String getSuperheroName();
  public int getPower();
  public String getDescription();
  public int getSpeed();
  public int getStrength();
  public int getUsefulness();
  public int getIntelligence();
  
  //from super interface Comparable
  public int compareTo (SuperheroInterface otherSuperhero);
}
