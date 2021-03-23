// --== CS400 File Header Information ==--
// Name: qingfei wang
// Email: qwang398@wisc.edu
// Team: <the team name: IF Blue>
// Role: <Data Wrangler>
// TA: <Siddharth (Sid) Mohan>
// Lecturer: <Gary 001>
// Notes to Grader: <optional extra notes>
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.*;
import java.util.zip.DataFormatException;


public class CreateFiles {
  
  private static final String csv1 = "marvel_characters_info.csv";
  private static final String csv2 = "charcters_stats.csv";
  private static final String csv3 = "superheroes_power_matrix.csv";
  private static final String head = "Name,Intelligence,Strength"
      + ",Speed,Usefulness,Power Rating,Description";
  
  
  public static List<String> createCSV ()
      throws FileNotFoundException, IOException, DataFormatException {
    
    List<String> result = new ArrayList<>();
    
    //arrayList that contains marvel hero names
    FileReader fileReader1 = new FileReader(csv1);
    Scanner marvel_info = new Scanner(fileReader1);
    List<String> name = new ArrayList<>();
    while (marvel_info.hasNext()) {
      // read and parse raw
      String raw = marvel_info.nextLine();
      String[] values = raw.split(",");
      if (values[7].startsWith("Marvel")) {
        name.add(values[1]);
      }
    }
    name = name.stream().distinct().collect(Collectors.toList());
    
    // super power arrayList
    // create new scanner
    FileReader fileReader3 = new FileReader(csv3);
    Scanner power_matrix = new Scanner(fileReader3);
    List<String> ability = new ArrayList<>();
    //get headers of the csv
    String[] headers = power_matrix.nextLine().split(",");
    
    while (power_matrix.hasNext()) {
      // read and parse raw
      String raw = power_matrix.nextLine();
      String[] values = raw.split(",");     
      // List contains abilities of each hero
      List<String> each_ability = new ArrayList<>();
      String description;
      for (int i = 1; i<headers.length;i++) {
        if (values[i].contains("TRUE")) {
          each_ability.add(headers[i].toLowerCase());
        }
      }
      description = each_ability.stream().collect(Collectors.joining(", "));
      if (name.contains(values[0])) {     
         ability.add(values[0]+ "/ has abilities such as "+ description+", and so on.");      
      }
    }
    
    //create final csv with description,Intelligence,Speed,Strength,Usefulness,power Rating
    FileReader fileReader2 = new FileReader(csv2);
    Scanner stats = new Scanner(fileReader2);
    List<String> heroList = new ArrayList<>();
    //get headers of the csv
    String[] headers_hero = stats.nextLine().split(",");
    
    while (stats.hasNext()) {
      // read and parse raw
      String raw = stats.nextLine();
      String[] values = raw.split(",");       
      // calculate power rating
      int intelligence = Integer.parseInt(values[2]);
      int strength = Integer.parseInt(values[3]);
      int speed = Integer.parseInt(values[4]);
      int usefulness = Integer.parseInt(values[6]);
      float power_rating =  (float) ((intelligence+strength+speed+usefulness)/ (4.0));      
      // hero object
      if (name.contains(values[0])) {
           String hero = String.join(",",values[0],Integer.toString(intelligence)
                ,Integer.toString(strength),Integer.toString(speed)
                ,Integer.toString(usefulness),Float.toString(power_rating));
           heroList.add(hero);
      }   
    }
    
    
    //final join
    List<String> heroes = new ArrayList<>();
    
    for (String descrip : ability) {
      for (String h :heroList) {
        String[] hero = h.split(",");
        String[] des = descrip.split("/");
        if (des[0].equals(hero[0])) {
          String d = '"' + des[0]+des[1] + '"';
          heroes.add(String.join(",",h, d));
        }
      }
    }
    
    return heroes;
  } 

  public static void main(String[] args) throws FileNotFoundException, IOException, DataFormatException {
    List<String> heroList = createCSV();

    // create csv file
    FileWriter csvWriter = new FileWriter("dataset.csv");
    csvWriter.append(head);
    csvWriter.append("\n");
    
    for (String rowData : heroList) {
        csvWriter.append(rowData);
        csvWriter.append("\n");
    }

    csvWriter.flush();
    csvWriter.close();
    
    //create txt file
//    File f = new File ("dataset.txt");
//    if (!f.exists()) {
//        f.createNewFile();
//    }
//    FileWriter fw = new FileWriter(f.getAbsoluteFile());
//    PrintWriter writer = new PrintWriter(fw);
//
//    for(String s : heroList) {
//      writer.println(s);
//    }
//    writer.close();

  }

}