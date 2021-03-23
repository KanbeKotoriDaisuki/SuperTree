// --== CS400 File Header Information ==--
// Name: qingfei wang
// Email: qwang398@wisc.edu
// Team: <the team name: IF Blue>
// Role: <Data Wrangler>
// TA: <Siddharth (Sid) Mohan>
// Lecturer: <Gary 001>
// Notes to Grader: <optional extra notes>
import java.lang.*;
import java.util.Arrays;
import java.util.List;
public class Hero implements SuperheroInterface {
  String[] heroInfo;
  
  public Hero(String[] heroInfo) {
    this.heroInfo = heroInfo;
  }

  @Override
  public String getSuperheroName() {
    // TODO Auto-generated method stub
    return heroInfo[0];
  }

  @Override
  public int getPower() {
    // TODO Auto-generated method stub
    float power = Float.parseFloat(heroInfo[5]);;
    return (int)(Math.ceil(power));
  }

  @Override
  public String getDescription() {
    String description = heroInfo[6].replace("\"", "");
    // TODO Auto-generated method stub
    return description;
  }

  @Override
  public int getSpeed() {
    // TODO Auto-generated method stub
    int i=Integer.parseInt(heroInfo[3]);  
    return i;
  }

  @Override
  public int getStrength() {
    // TODO Auto-generated method stub
    int i=Integer.parseInt(heroInfo[2]);  
    return i; 
  }

  @Override
  public int getUsefulness() {
    // TODO Auto-generated method stub
    int i=Integer.parseInt(heroInfo[4]);  
    return i;
  }

  @Override
  public int getIntelligence() {
    // TODO Auto-generated method stub
    int i=Integer.parseInt(heroInfo[1]);  
    return i;
  }

  @Override
  public int compareTo(SuperheroInterface otherSuperhero) {
    // TODO Auto-generated method stub
    if (this.getPower() < otherSuperhero.getPower()) {
      return -1;
    }else if (this.getPower() > otherSuperhero.getPower()) {
      return 1;
    }else {
      return this.getSuperheroName().compareTo(otherSuperhero.getSuperheroName());
      
    }
  }
  
  @Override
  public String toString() {
    return new StringBuilder()
      .append("Name: "+heroInfo[0]+ "\n")
      .append("Power Rating: " + heroInfo[5] + "\t")
      .append("Strength: " + heroInfo[2] + "\t")
      .append("Speed: " + heroInfo[3] + "\t")
      .append("Usefulness: " + heroInfo[4] + "\t")
      .append("Intelligence: " + heroInfo[1] + "\n")
      .append("Description: " + heroInfo[6].replace("\"", "") + "\n")
      .toString();
  }
  
}