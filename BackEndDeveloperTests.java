import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BackEndDeveloperTests {

    BackendInterface toTest;
    @BeforeEach
    public void makeInstance(){
        toTest = null;//replace when implementing

    }

    @Test
    public void testGetStrength(){
        SuperheroInterface heroToTest = null;
        //Tests for correct handling of hero not in tree
        try{
            toTest.getStrength(heroToTest);
        }catch(NoSuchElementException e){
            System.out.print("Correctly Throws NoSuchElement");
        }
        catch(Exception f){
            fail("Inncorrect Exception Thrown");
        }
        SuperheroInterface heroToTest2 = null; //set to hero in tree
        try {
            assertEquals(50, toTest.getStrength(heroToTest2));
        }
        catch(Exception e){
            fail("Exception thrown when trying to get strength based on hero");
            }
    }

    @Test
    public void testGetHero(){
        SuperheroInterface heroToTest = null;
        //Tests for correct handling of hero not in tree
        try{
            toTest.getHero(5000);
        }catch(NoSuchElementException e){
            System.out.print("Correctly Throws NoSuchElement");
        }
        catch(Exception f){
            fail("Inncorrect Exception Thrown");
        }
        SuperheroInterface heroToTest2 = null; //set to hero in tree
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
            assertEquals(number,8);
        }catch(Exception e){
            fail("Exception thrown while getting number of heroes");
        }
    }
}
