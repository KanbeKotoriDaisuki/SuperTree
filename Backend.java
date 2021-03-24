// --== CS400 File Header Information ==--
// Name: Ethan Foley
// Email: efoley7@wisc.edu
// Team: IF Blue
// Role: Backend
// TA: Sid
// Lecturer: Florian
// Notes to Grader: <optional extra notes>
import java.io.File;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class handles the back end of the project. This allows for the user input
 * taken in by the front end to adapt and take data from two red black trees.
 */
public class Backend implements BackendInterface {
    SuperheroDataReaderInterface data;//data reader to take in data
    RedBlackTree<namedHero> nameTree;//Red black tree for finding names in the data set
    RedBlackTree<SuperheroInterface> powerTree;//red black tree for finding powers in the data set
    List<SuperheroInterface> heroList;//list of heros returned from the data reader

    /**
     * This is the constructor for backend objects. This instantiates the data reader and the trees
     * for this project. It also fills the trees with the data taken in from the data reader
     *
     * @param args Command line argument to create the file reader
     */
    public Backend(String[] args) {
        try {
            data = new SuperheroDataReader();
            File heroFile = new File("dataset.csv");
            heroList = data.readDataSet(new FileReader(heroFile));
            nameTree = new RedBlackTree<namedHero>();
            powerTree = new RedBlackTree<SuperheroInterface>();
            //Iterate through the list returned from the data reader and
            //add them to the trees
            for (int i = 0; i < heroList.size(); ++i) {
                SuperheroInterface toInsert = heroList.get(i);
                powerTree.insert(toInsert);
                // System.out.println(i);
                // System.out.println(heroList.get(i));
                nameTree.insert(new namedHero(toInsert));
            }
        }
        //Handles bad file input by the user
        catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error Reading File Input");
        }

    }

    /**
     * Constructor for backend objects to be tested without a file
     */
    public Backend() {
        nameTree = new RedBlackTree<>();
        powerTree = new RedBlackTree<>();
    }

    @Override
    /**
     * This method gets a hero based on the heroes name
     * @Param hero Name of the hero to get
     * @Return Returns the hero
     * @throws NoSuchElementException if the hero isn't found
     */
    public SuperheroInterface getHero(String hero) throws NoSuchElementException {
        RedBlackTree.Node<namedHero> node = nameTree.root;//start at the root
        //search through the tree until a null node is found
        while (node != null) {
            //if the node is found
            if (node.data.compareNames(hero) == 0) {
                return node.data.hero;
            }
            //go to the left subtree
            else if (node.data.compareNames(hero) > 0) {
                node = node.leftChild;
            }
            //go to the right subtree
            else {
                node = node.rightChild;
            }
        }
        //if not found
        throw new NoSuchElementException();
    }


    @Override
    /**
     * Get a hero based off of its power
     * @param strength Power to search based off of
     * @return hero if found
     * @throws NoSuchElementException if the hero is not found
     */
    public SuperheroInterface getHero(int strength) throws NoSuchElementException {
        RedBlackTree.Node<SuperheroInterface> node = powerTree.root;//start at root
        String[] hero = new String[]{"0", "0", "90", "0", "0", "" + strength, "0"};//hero to compare to
        Hero toCompare = new Hero(hero);
        //search until a null node is found
        while (node != null) {
            toCompare.heroInfo[0] = node.data.getSuperheroName(); //make sure the names are the same for comparison
            //if the heroes are the same return the hero
            if (node.data.compareTo(toCompare) == 0) {
                return node.data;
            }
            //go to left subtree
            else if (node.data.compareTo(toCompare) > 0) {
                node = node.leftChild;
            }
            //go to right subtree
            else {
                node = node.rightChild;
            }
        }
        //throw if not found
        throw new NoSuchElementException();
    }


    @Override
    /**
     * Get a post order traversal of the powerTree
     * @return Linked list with the traversal
     */
    public LinkedList<SuperheroInterface> getPostOrder() {
        LinkedList<SuperheroInterface> postOrder = new LinkedList<>();
        postHelper(postOrder, powerTree.root);
        return postOrder;
    }

    /**
     * Recursive helper for post order
     *
     * @param postOrder Linked list to add to
     * @param node      node for the traversal
     */
    private void postHelper(LinkedList<SuperheroInterface> postOrder,
                            RedBlackTree.Node<SuperheroInterface> node) {

        postHelper(postOrder, node.leftChild); //vist left subtree
        postHelper(postOrder, node.rightChild); //visit right subtree
        postOrder.add(node.data); //visit root
    }

    @Override
    /**
     * This method returns a pre order traversal of the power tree
     * @return Linked list based on the traversal
     */
    public LinkedList<SuperheroInterface> getPreOrder() {
        LinkedList<SuperheroInterface> preOrder = new LinkedList<>();
        postHelper(preOrder, powerTree.root);
        return preOrder;
    }

    /**
     * This is a helper method to recursively traverse the power tree
     *
     * @param preOrder Linked list to add to
     * @param node     node to search based off of
     */
    private void preHelper(LinkedList<SuperheroInterface> preOrder, RedBlackTree.Node<SuperheroInterface> node) {
        preOrder.add(node.data); //visit the root
        preHelper(preOrder, node.leftChild); //visit the left subtree
        preHelper(preOrder, node.rightChild); //visit the right subtree
    }

    @Override
    /**
     * This method returns a level order traversal of the power tree
     * @return Linked list with the level order traversal in
     */
    public LinkedList<SuperheroInterface> getLevelOrder() {
        //make a queue for the level order traversal using a linked list
        LinkedList<RedBlackTree.Node<SuperheroInterface>> queue = new LinkedList<>();
        LinkedList<SuperheroInterface> toReturn = new LinkedList<>();
        if (powerTree.root == null) return null; //if there is nothing in the tree
        queue.add(powerTree.root); //add the root to the queue
        while (!queue.isEmpty()) {
            RedBlackTree.Node<SuperheroInterface> node = queue.remove(0);
            toReturn.add(node.data);
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }

        }
        return toReturn;
    }

    @Override
    /**
     * Get heroes in a certain range of powers from the power tree.
     * @param start Power level to start at
     * @param stop Power level to stop at
     * @return In order traversal starting and stoping at the indicated range
     */
    public LinkedList<SuperheroInterface> getHeroesInRange(int start, int stop) {
        LinkedList<SuperheroInterface> inOrder = new LinkedList<>();//list to return
        Iterator<SuperheroInterface> search = powerTree.iterator();//use the iterator for the RBtree
        //iterate through the tree and add only heroes in the specified power range
        while (search.hasNext()) {
            SuperheroInterface next = search.next();
            if (next.getPower() >= start && next.getPower() <= stop) {
                inOrder.add(next);
            }

        }
        return inOrder;
    }


    @Override
    /**
     * Returns the number of heroes in the data set
     * @return Number of heroes in the data set
     */
    public int getNumberOfHeroes() {
        return powerTree.size();
    }

    @Override
    /**
     * This method adds a hero to the tree based on user input
     * @param name Name of the new hero
     * @param statistics Statistics associated with the hero
     * @param description of the hero
     * @return The hero added to the tree
     * @throws IllegalArgumentException if the user passed in an argument incorrectly
     */
    public SuperheroInterface addHero(String name, int[] statistics, String description) throws IllegalArgumentException {
        try {
    	String[] info = new String[7];
        //fill the info array based off of the three params
        info[0] = name;
        int power = 0;
        for (int i = 0; i < statistics.length; ++i) {
            info[i + 1] = "" + statistics[i];
            power += statistics[i];
        }
        info[5] = "" + power;
        info[6] = description;

        //add the new hero to both trees
        SuperheroInterface toAdd = new Hero(info);
        namedHero namedToAdd = new namedHero(toAdd);
        
        powerTree.insert(toAdd);
        nameTree.insert(namedToAdd);
        return toAdd;
        }catch(Exception e) {
        	e.printStackTrace();
        }
return null;
    }

    @Override
    /**
     * This method edits a hero already in the trees based on user input
     * @param name Name of the new hero
     * @param statistics Statistics associated with the hero
     * @param description of the hero
     * @return The hero added to the tree
     * @throws InvalidParameterException
     */
    public SuperheroInterface editHero(String name, int[] statistics, String description)
            throws InvalidParameterException {
        
            Hero toChange = (Hero) this.getHero(name);
            Hero powerToChange = null;
            int powerSearch = (int)(Math.ceil(Float.parseFloat(toChange.heroInfo[5])));
            LinkedList<SuperheroInterface> powers = this.getHeroesInRange(powerSearch, powerSearch);
            for(int i = 0; i < powers.size(); ++i) {
            	if(powers.get(i).getSuperheroName().equals(toChange.getSuperheroName())) {
            		powerToChange = (Hero)powers.get(i);
            }
            }
            int power = 0;
            for (int i = 1; i < statistics.length; ++i) {
                toChange.heroInfo[i] = "" + statistics[i-1];
                powerToChange.heroInfo[i] = "" + statistics[i-1];
                power += statistics[i-1];
            }
            powerToChange.heroInfo[5] = "" + power;
            toChange.heroInfo[5] = "" + power;
            toChange.heroInfo[6] = description;
            powerToChange.heroInfo[6] = description;
        System.out.println(powerToChange.toString());
        return powerToChange;
    }

}