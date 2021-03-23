public class namedHero implements Comparable<namedHero>{
    public String name;
    SuperheroInterface hero;
    public namedHero(SuperheroInterface hero){
    	
        this.name = hero.getSuperheroName();
        this.hero = hero;
    }


    public int compareNames(String otherName) {
        return this.name.compareTo(otherName);
    }

 @Override
    public int compareTo(namedHero o) {

	 return this.name.compareTo(o.name);
    
 }
    
 public String toString() {
	 return hero.toString();
 }
 
}