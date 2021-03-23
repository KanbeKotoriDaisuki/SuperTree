
// --== CS400 File Header Information ==--
//Name: qingfei wang
//Email: qwang398@wisc.edu
//Team: <the team name: IF Blue>
//Role: <Data Wrangler>
//TA: <Siddharth (Sid) Mohan>
//Lecturer: <Gary 001>
//Notes to Grader: <optional extra notes>
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.DataFormatException;
import java.io.FileNotFoundException;
import java.util.Collections;


public class SuperheroDataReader implements SuperheroDataReaderInterface {

public List<SuperheroInterface> readDataSet(Reader inputFileReader)
   throws FileNotFoundException, IOException, DataFormatException {
 
 
 List<SuperheroInterface> result = new ArrayList<>();
 // read input file
 Scanner superHeroReader = new Scanner(inputFileReader);
 // track the line that runs wrong
 int lineCount = 2;
 // close file if the file is empty
 if (!superHeroReader.hasNext()) {
   superHeroReader.close();
   throw new DataFormatException("Empty file");
 }
 //skip the header
 superHeroReader.nextLine();

 while (superHeroReader.hasNext()) {
   // read and parse lines in the file
   String raw = superHeroReader.nextLine();
   // separate lines into hero patterns
   String[] values = raw.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
   // hero objects only have 7 properties
   if (values.length !=7) {
     superHeroReader.close();
     throw new DataFormatException("CSV syntax error at line " + lineCount);
   }
   // add new hero object into the list
   result.add(new Hero(values));
   lineCount++;
 }
   
   
 superHeroReader.close();
 
 // sort the hero with their power rating in ascending order
 Collections.sort(result,(a, b) -> {
   return a.compareTo(b);});

 // return 25 heroes each time
 ArrayList<SuperheroInterface> final_result = new ArrayList<>();
for (int i =0; i< 170;i++) {
   final_result.add(result.get(i));
 }
 return final_result;

}

}
