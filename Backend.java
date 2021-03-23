
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.zip.DataFormatException;

public class Backend implements BackendInterface {
    SuperheroDataReaderInterface data;
    RedBlackTree<namedHero> nameTree;
    RedBlackTree<SuperheroInterface> powerTree;
    List<SuperheroInterface> heroList;

    public Backend(String[] args) {
       try {
    	    data = new SuperheroDataReader() ;
    	   File heroFile = new File("dataset.csv");
           heroList = data.readDataSet(new FileReader(heroFile));
           nameTree = new RedBlackTree<namedHero>();
           powerTree = new RedBlackTree<SuperheroInterface>();
           for(int i = 0; i< heroList.size(); ++i){
           	//Hero temp = new Hero(heroList.get(i));
               SuperheroInterface toInsert =  heroList.get(i);
               powerTree.insert(toInsert);
               System.out.println(i);
               nameTree.insert(new namedHero(toInsert));
               

           }
           System.out.println(nameTree.toString());
       }
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.print("Error Reading File Input");
    	}

    }
	@Override
    public SuperheroInterface getHero(String hero) throws NoSuchElementException {
        RedBlackTree.Node<namedHero> node = nameTree.root;
        while(node != null){
            if(node.data.compareNames(hero) == 0){
                return node.data.hero;
            }
            else if(node.data.compareNames(hero) > 0){
                node = node.leftChild;
            }
            else{
                node = node.rightChild;
            }
        }
        throw new NoSuchElementException();
    }


    @Override
    public SuperheroInterface getHero(int strength) throws NoSuchElementException {
        RedBlackTree.Node<SuperheroInterface> node = powerTree.root;
        String[] hero = new String[] {"0", "0", "90", "0", "0", "0"};
        Hero toCompare = new Hero(hero);//place the strength here in the final app
        while(node != null){
        	toCompare.heroInfo[0] = node.data.getSuperheroName(); 
            if(node.data.compareTo(toCompare) == 0){
                return node.data;
            }
            else if(node.data.compareTo(toCompare) > 0){
                node = node.leftChild;
            }
            else{
                node = node.rightChild;
            }
        }
        throw new NoSuchElementException();
    }


    @Override
    public LinkedList<SuperheroInterface> getPostOrder() {
        LinkedList<SuperheroInterface> postOrder = new LinkedList<>();
        postHelper(postOrder, powerTree.root);
        return postOrder;
    }
     public void postHelper(LinkedList<SuperheroInterface> postOrder,
                            RedBlackTree.Node<SuperheroInterface> node){

        postHelper(postOrder, node.leftChild);
        postHelper(postOrder, node.rightChild);
        postOrder.add(node.data);
     }

    @Override
    public LinkedList<SuperheroInterface> getPreOrder() {
        LinkedList<SuperheroInterface> preOrder = new LinkedList<>();
        postHelper(preOrder, powerTree.root);
        return preOrder;
    }

    public void preHelper(LinkedList<SuperheroInterface> preOrder, RedBlackTree.Node<SuperheroInterface> node){
        preOrder.add(node.data);
        preHelper(preOrder,node.leftChild);
        preHelper(preOrder,node.rightChild);
    }

    @Override
    public LinkedList<SuperheroInterface> getLevelOrder() {
        LinkedList<RedBlackTree.Node<SuperheroInterface>> queue = new LinkedList<>();
        LinkedList<SuperheroInterface> toReturn = new LinkedList<>();
        if(powerTree.root == null) return null;
        queue.add(powerTree.root);
        while(!queue.isEmpty()){
            RedBlackTree.Node<SuperheroInterface> node = queue.remove(0);
            toReturn.add(node.data);
            if(node.leftChild != null){
                queue.add(node.leftChild);
            }
            if(node.rightChild != null){
                queue.add(node.rightChild);
            }

        }
        return toReturn;
    }

    @Override
    public LinkedList<SuperheroInterface> getHeroesInRange(int start, int stop) {
        LinkedList<SuperheroInterface>inOrder = new LinkedList<>();
        Iterator<SuperheroInterface> search = powerTree.iterator();
        while(search.hasNext()){
            SuperheroInterface next = search.next();
            if(next.getStrength() >= start && next.getStrength() <= stop) {
                inOrder.add(next);
            }

        }
        return inOrder;
    }


    @Override
    public int getNumberOfHeroes() {
        return powerTree.size();
    }

    @Override
    public SuperheroInterface addHero(String name, int[] statistics, String description) throws IllegalArgumentException {
        return null;
    }
	
	@Override
	public SuperheroInterface editHero(String name, int[] statistics, String description)
			throws InvalidParameterException {
		// TODO Auto-generated method stub
		return null;
	}

}