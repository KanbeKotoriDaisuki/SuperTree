import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BackEndDeveloperTests {

    Backend toTest;
    Hero heroToTest;
    Hero heroToTest2;
    @Before
    public void makeInstance(){
        toTest = new Backend(); ;//replace when implementing
        String[] x = new String[] {"Ammo","1","2","4","50","1","The GOAT"};
        heroToTest = new Hero(x);
        String[] y = new String[] {"Walrus","50","0","0","5","50","The other GOAT"};
        heroToTest2 = new Hero(y);
        int[] stats1 = {Integer.parseInt(x[1]),Integer.parseInt(x[2]),Integer.parseInt(x[3]) ,
        		Integer.parseInt(x[4]), Integer.parseInt(x[5])};
        toTest.addHero(x[0], stats1, x[6]);
        int[] stats2 = {Integer.parseInt(y[1]),Integer.parseInt(y[2]),Integer.parseInt(y[3]) ,
        		Integer.parseInt(y[4]), Integer.parseInt(y[5])};
        toTest.addHero(y[0], stats2, y[6]);

    }

    @Test
    public void testGetStrength(){
        
        //Tests for correct handling of hero not in tree
        try{
            toTest.getHero(heroToTest.getSuperheroName());
        }catch(NoSuchElementException e){
            System.out.print("Correctly Throws NoSuchElement");
        }
        catch(Exception f){
        	f.printStackTrace();
            fail("Inncorrect Exception Thrown");
        }
        ; //set to hero in tree
        try {
            assertEquals(50, toTest.getHero(heroToTest2.getSuperheroName()));
        }
        catch(Exception e){
            fail("Exception thrown when trying to get strength based on hero");
            }
    }

    @Test
    public void testGetHero(){
 
        //Tests for correct handling of hero not in tree
        try{
            toTest.getHero(5000);
        }catch(NoSuchElementException e){
            System.out.print("Correctly Throws NoSuchElement");
        }
        catch(Exception f){
            fail("Inncorrect Exception Thrown");
        }

        try {
            assertEquals(50, toTest.getHero(50));
        }
        catch(Exception e){
            fail("Exception thrown when trying to get strength based on hero");
        }
    }


    @Test
    public void testLevelOrder(){
        try{
            LinkedList<SuperheroInterface> traversal= toTest.getLevelOrder();
            LinkedList<SuperheroInterface> compare= null;//set to linked list of superhero objects in tree
            for(int i = 0; i < traversal.size(); ++i){
                assertEquals(traversal.get(i),compare.get(i));
            }
        }catch(Exception e){
            fail("Exception thrown during level order traversal");
        }

    }

    @Test
    public void testGetHeroesInRange(){
        try{
            LinkedList<SuperheroInterface> heroes= toTest.getHeroesInRange(5,50);
            LinkedList<SuperheroInterface> range= null;//set to linked list of superhero objects in tree
            for(int i = 0; i < heroes.size(); ++i){
                assertEquals(heroes.get(i),range.get(i));
            }
        }catch(Exception e){
            fail("Exception thrown while getting heroes in range");
        }
    }

    @Test
    public void testGetNumHeroes(){
        try{
            int number = toTest.getNumberOfHeroes();
            assertEquals(number,2);
        }catch(Exception e){
            fail("Exception thrown while getting number of heroes");
        }
    }
}