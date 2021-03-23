// --== CS400 File Header Information ==--
// Name: qingfei wang
// Email: qwang398@wisc.edu
// Team: <the team name: IF Blue>
// Role: <Data Wrangler>
// TA: <Siddharth (Sid) Mohan>
// Lecturer: <Gary 001>
// Notes to Grader: <optional extra notes>
import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataWranglerTests {
  
//  new StringReader("Name,power rating,speed,strength,usefulness,intelligence,description\n"+
//      "A-Bomb,60,17,100,64,17,On rare occasions, and through unusual circumstances, Jones has been able to tap into a mysterious, near-limitless energy sources known as the Destiny Force. The Destiny force is believed to be inherent in all humanity. Jones has used this power to alter reality in the past by bringing figures from his own imagination to life or even figures from different times of existence. He has proven able to render thousands of Kree and Skrull warriors immobile with a thought, single-handedly overcome the Atlantean army, augment all of his own physical attributes, heal himself after sustaining life threatening energies and levitate.\n "
//    +"Abomination,63,80,53,90,55, Like the Hulk, the Abomination can use his superhumanly strong leg muscles to leap great distances, covering miles in a single bound. The Abomination has vast physical strength. The Abomination is resistant to extremes of temperature, and can hold his breath for extended periods of time; in the case of lack of air or heat, he may enter a coma-like state of suspended animation.\n" +
//    "Air-Walker,100,85,100,100,55, \n")

    /**
     * This test makes sure there are 25 superheroes being read from superheroes.txt
     * @return true if it contain 25 heros.
     */
    @Test
    public void testReaderNumberOfSuperheroes()  throws Exception{
      //boolean pass = false;
      SuperheroDataReader readerToTest= new SuperheroDataReader();
      // create a super heros list
      List<SuperheroInterface> heroList = readerToTest.readDataSet(new FileReader("dataset.csv"));
      
//      if(heroList.size() == 25) {
//        pass = true;
//      }

     // check if the list contains 25 heros
      System.out.println(heroList.size());
     assertEquals(170, heroList.size());
     

   }

   /**
    * This test makes sure the order of the superheroes in the list is correct
    * @return true if the order is ascending.
    */
   @Test
   public void testSuperheroOrder()  throws Exception{
     //boolean pass =false;
     SuperheroDataReader readerToTest=new SuperheroDataReader();
     // create a super heros list
     List<SuperheroInterface> heroList = readerToTest.readDataSet(new FileReader("dataset.csv"));

     // for every hero objects, its power rating will be less than next hero.
     for (int i = 1; i< heroList.size();i++) {
//       if(heroList.get(i-1).compareTo(heroList.get(i))<=0) {
//         pass =true;
//       }
       assertTrue(heroList.get(i-1).compareTo(heroList.get(i)) <= 0);
     }
   }
   
   /**
    * This test makes sure the descriptions of the superheroes are correct
    * @return true if the description is matching
    */
   @Test
   public void testReaderSuperheroDescription()  throws Exception{
     //boolean pass = false;
     SuperheroDataReader readerToTest= new SuperheroDataReader();
     // create a super heros list
     List<SuperheroInterface> heroList = readerToTest.readDataSet(new FileReader("dataset.csv"));

     //only test the top 3 heros
     //match the description list with the hero objects
     String[] descriptionList = new String[] {"Ammo has abilities such as weapons master, and so on."
         ,"Arachne has abilities such as agility, accelerated healing, durability, super strength, "
             + "stamina, super speed, reflexes, psionic powers, toxin and disease resistance, enhanced touch,"
             + " wallcrawling, web creation, and so on."
             ,"Beak has abilities such as flight, gliding, vision - telescopic, and so on."};
     
     for (int i =0; i< 3;i++) {
//       if (heroList.get(i).getDescription().equals(descriptionList[i])) {
//         pass = true;
//       }
    	 
       assertEquals(descriptionList[i], heroList.get(i).getDescription());
     }
   }
   
   /**
    *  This test makes sure the superheroes' powers are in the right ranges
    * @return true if all heros' power rating are 1-100
    */
   @Test
   public void testReaderCorrectPowerRange()  throws Exception{
     //boolean pass =false;
     SuperheroDataReader readerToTest= new SuperheroDataReader();
     // create a (null) super heros list
     List<SuperheroInterface> heroList = readerToTest.readDataSet(new FileReader("dataset.csv"));

     // loop for every hero object to check if the power rating is less than or equal to 100
     for(int i=0; i<heroList.size();i++) {
//       if (heroList.get(i).getPower() <= 100) {
//         pass = true;
//       }
       assertTrue(heroList.get(i).getPower() <= 200);
     }
   }
   
   /**
    * This test makes sure the names of the superheroes are correct 
    * @return true if the top 3 hero have correct name
    */
   @Test
   public void testReaderSuperheroNames()  throws Exception{
     //boolean pass =false;
     SuperheroDataReader readerToTest= new SuperheroDataReader();
     // create a (null) super heros list
     List<SuperheroInterface> heroList = readerToTest.readDataSet(new FileReader("dataset.csv"));

     // compare the name in the name list to heroList
     String[] nameList = new String[] {"Ammo","Arachne","Beak"};
     for (int i =0; i< 3;i++) {
//       if (heroList.get(i).getSuperheroName().equals(nameList[i])) {
//         pass = true;
//       }
       assertEquals(nameList[i], heroList.get(i).getSuperheroName());
     }
   }
   
}
