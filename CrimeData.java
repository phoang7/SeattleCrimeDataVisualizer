// A program to analyze data pulled from data.seattle.gov and provide information
//   about crimes in various neighborhoods of Seattle.
//
// This program demonstrates how meaningful, real problems can be solved on real
//   data using the programming skills learned in CSE142. There are no concepts
//   used in this program that was not taught in CSE142, though there are a few
//   unfamiliar library methods called (namely, split() and binarySearch()).

// Phillip Hoang
// 1/6/18
// This program will prompt the user if they want to process a file called Crime_Data.csv
// to display data in the console and a data table of crimes in each neighborhood of a city.
// Note: You must have a file called Crime_Data.csv stored in this program's folder
// for this program to be able to run

import java.util.*;
import java.io.*;
import javax.swing.*;

public class CrimeData {
   public static final String[] CRIMES = {"AGGRAVATED ASSAULT", "AGGRAVATED ASSAULT-DV", "ARSON", "BURGLARY-COMMERCIAL", "BURGLARY-COMMERCIAL-SECURE PARKING", "BURGLARY-RESIDENTIAL", "BURGLARY-RESIDENTIAL-SECURE PARKING", "CAR PROWL", "DISORDERLY CONDUCT", "DUI", "FAMILY OFFENSE-NONVIOLENT", "GAMBLE", "HOMICIDE", "LIQUOR LAW VIOLATION", "LOITERING", "MOTOR VEHICLE THEFT", "NARCOTIC", "PORNOGRAPHY", "PROSTITUTION", "RAPE", "ROBBERY-COMMERCIAL", "ROBBERY-RESIDENTIAL", "ROBBERY-STREET", "SEX OFFENSE-OTHER", "THEFT-ALL OTHER", "THEFT-BICYCLE", "THEFT-BUILDING", "THEFT-SHOPLIFT", "TRESPASS", "WEAPON"};
   public static final String[] NEIGHBORHOODS = {"ALASKA JUNCTION", "ALKI", "BALLARD NORTH", "BALLARD SOUTH", "BELLTOWN", "BITTERLAKE", "BRIGHTON/DUNLAP", "CAPITOL HILL", "CENTRAL AREA/SQUIRE PARK", "CHINATOWN/INTERNATIONAL DISTRICT", "CLAREMONT/RAINIER VISTA", "COLUMBIA CITY", "COMMERCIAL DUWAMISH", "COMMERCIAL HARBOR ISLAND", "DOWNTOWN COMMERCIAL", "EASTLAKE - EAST", "EASTLAKE - WEST", "FAUNTLEROY SW", "FIRST HILL", "FREMONT", "GENESEE", "GEORGETOWN", "GREENWOOD", "HIGH POINT", "HIGHLAND PARK", "HILLMAN CITY", "JUDKINS PARK/NORTH BEACON HILL", "LAKECITY", "LAKEWOOD/SEWARD PARK", "MADISON PARK", "MADRONA/LESCHI", "MAGNOLIA", "MID BEACON HILL", "MILLER PARK", "MONTLAKE/PORTAGE BAY", "MORGAN", "MOUNT BAKER", "NEW HOLLY", "NORTH ADMIRAL", "NORTH BEACON HILL", "NORTH DELRIDGE", "NORTHGATE", "PHINNEY RIDGE", "PIGEON POINT", "PIONEER SQUARE", "QUEEN ANNE", "RAINIER BEACH", "RAINIER VIEW", "ROOSEVELT/RAVENNA", "ROXHILL/WESTWOOD/ARBOR HEIGHTS", "SANDPOINT", "SLU/CASCADE", "SODO", "SOUTH BEACON HILL", "SOUTH DELRIDGE", "SOUTH PARK", "UNIVERSITY", "UNKNOWN", "WALLINGFORD"};
   
   public static void main(String[] args) throws FileNotFoundException {
      Scanner data = new Scanner(new File("Crime_Data.csv"));
      Scanner console = new Scanner(System.in);
      
      // Uncomment this code to read the list of crimes and/or neighborhoods directly from the
      //   file instead of using the preprocessed lists above.
      //       System.out.println(getValues(data, "c"));
      //       data.close();
      //       data = new Scanner(new File("Crime_Data.csv"));
      //       System.out.println(getValues(data, "n"));
      
      //int neighborhood;
      //do {
      //System.out.println("Choose a neighborhood: ");
      //for (int i = 0; i < NEIGHBORHOODS.length; i++) {
      //System.out.println("    " + (i + 1) + ": " + NEIGHBORHOODS[i]);
      //}
      //neighborhood = console.nextInt();
      //} while (neighborhood < 0 || neighborhood >= NEIGHBORHOODS.length || neighborhood == 0);
      //System.out.println(NEIGHBORHOODS[neighborhood-1]);
      //int[] counts = processData(data, neighborhood);
      //printResults(counts);
      int[][] neighborhoods = new int[NEIGHBORHOODS.length][CRIMES.length]; // 59 by 30                                   
      JFrame mainFrame = new JFrame();
      String message = "Process a file called Crime_Data.csv to process the data to create a data" +
            " table for Seattle Neighborhoods?" +  
            " \n(Note: You must have the file in this program's folder)";
      Object[] choices = {"Process File", "Quit"};
      int choice = JOptionPane.showOptionDialog(mainFrame, message, "Process Data?", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            choices,  //the titles of buttons
            choices[0]); //default button title
      if (choice == 0) {
         System.out.println("one sec!");
         processDataForAll(neighborhoods, data, mainFrame);
         System.out.println("done!");
         
         int[] totalCrimes = getTotalForEachCrimeInSeattle(neighborhoods); //length of 30
         String[][] rowCells = createRowCells(neighborhoods, totalCrimes); //60 by 32 
         String[] columnNames = createColumnNames(); //length of 32
         String[][] rowCellsDuplicate = createRowCells(neighborhoods, totalCrimes); //60 by 32

         CrimeTable table = new CrimeTable(rowCells, columnNames, rowCellsDuplicate);
         
         // You can uncomment this but it'll display the key and data to console
         //System.out.println();
         //System.out.println("Key for column and row names");
         //System.out.println();
         //printKey(columnNames, rowCells);
         //System.out.println("Data from each neighborhood:");
         //System.out.println();
         //printResultsInEachNeighborhood(neighborhoods, totalCrimes);
      }
      else {
         mainFrame.dispose();
      }
      
      // Uncomment if you want to see the total crimes for each type in Seattle
      //printResults(totalCrimes);
      
   }
   
   // Prints a key for the columns and rows in the data table
   // column = types of crimes
   // neighborhoods = neighborhood names in the city
   public static void printKey(String[] column, String[][] neighborhoods) {
      System.out.println("Column (Type of Crime) Key:");
      System.out.println();
      for (int i = 1; i < column.length - 1; i++) {
         System.out.println(column[i]);
      }
      System.out.println();
      System.out.println("Row (Neighborhoods) Key:");
      System.out.println();
      for (int i = 0; i < neighborhoods.length - 1; i++) {
         System.out.println(neighborhoods[i][0]);
      }
      System.out.println();
   }
   
   // Creates column names for the data table
   public static String[] createColumnNames() {
      String[] columnNames = Arrays.copyOf(CRIMES, CRIMES.length + 2); //32
      for (int i = columnNames.length - 2; i > 0; i--) {
         columnNames[i] = columnNames[i - 1];
      }
      for (int i = 1; i < columnNames.length - 1; i++) {
         columnNames[i] = i + ". " + columnNames[i];
      }
      columnNames[columnNames.length - 1] = "Total";
      columnNames[0] = "Neighborhood";
      return columnNames;
   }
   
   // Creates the row cells for the data table
   // neighborhoods = number of crimes for each type of crime for each neighborhood 
   // totalCrimes = total number of crimes for each type of crime from a city
   public static String[][] createRowCells(int[][] neighborhoods, int[] totalCrimes) {
      String[][] rowTableData = new String[NEIGHBORHOODS.length + 1]
      [CRIMES.length + 2]; // 60 by 32
      for (int i = 0; i < rowTableData.length; i++) {
         int total = 0;
         if (i != rowTableData.length - 1) {
            rowTableData[i][0] = (i + 1) + ". " + NEIGHBORHOODS[i];
            for (int j = 0; j < CRIMES.length; j++) {
               total += neighborhoods[i][j];
               rowTableData[i][j + 1] = "" + neighborhoods[i][j];
            }
         }
         else {
            rowTableData[i][0] = "All neighborhoods";
         }
         rowTableData[i][rowTableData[0].length  - 1] = "" + total;
      }
      int total = 0;
      for (int i = 0; i < totalCrimes.length; i++) {
         total += totalCrimes[i];
         rowTableData[rowTableData.length - 1][i + 1] = "" + totalCrimes[i];
      }
      rowTableData[rowTableData.length - 1][rowTableData[0].length - 1] = "" + total;
      return rowTableData;
   }
   
   // Prints the data for each neighborhood
   // neighborhoods = number of crimes for each type of crime for each neighborhood 
   // totalCrimes = total number of crimes for each type of crime from a city
   public static void printResultsInEachNeighborhood(int[][] neighborhoods, int[] totalCrimes) {
      for (int i = 0; i < neighborhoods.length; i++) {
         System.out.println((i + 1) + ": " + NEIGHBORHOODS[i]);
         for (int j = 0; j < CRIMES.length; j++) {
            System.out.print(CRIMES[j] + ": " + neighborhoods[i][j]  + " / " + totalCrimes[j]);
            double crimePercentage = (double) neighborhoods[i][j] / totalCrimes[j] * 100;
            if (!Double.isNaN(crimePercentage)) {
               System.out.println(" (" + crimePercentage + "%)");
            }
            else {
               System.out.println();
            }
         }  
         System.out.println();                         
      }
   }
   
   // Continues to prompt the user if they want to process the data until they choose an option
   // neighborhoods = number of crimes for each type of crime for each neighborhood
   // data = reads the file called Crime_Data.csv
   // frame =  a window
   public static void processDataForAll(int[][] neighborhoods, Scanner data, JFrame frame) throws FileNotFoundException {
      JPanel panel = new JPanel();
      JLabel text =  new JLabel("please wait...");
      panel.add(text);
      JProgressBar progressBar = new JProgressBar(0,100);
      progressBar.setValue(0);
      progressBar.setStringPainted(true);
      panel.add(progressBar);
      
      frame.add(panel);
      frame.setTitle("Processing data...");
      frame.setSize(250, 75);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setResizable(false);
      frame.setVisible(true);
      
      System.out.print("please wait");
      for (int i = 0; i < neighborhoods.length; i++) {
         System.out.print(".");
         int percent = (int) Math.round((double) i / neighborhoods.length * 100);
         progressBar.setValue(percent);
         neighborhoods[i] = processData(data, i);
         data.close();
         data = new Scanner(new File("Crime_Data.csv"));
      }
      System.out.println();
      text.setText("Done!");
      progressBar.setValue(100);
      
      frame.dispose();
   }
   
   // Gets the total number of crimes for each type of crime in a city
   // neighborhoods = number of crimes for each type of crime for each neighborhood
   public static int[] getTotalForEachCrimeInSeattle(int[][] neighborhoods) {
      int[] totalCrimes = new int[CRIMES.length];
      for (int i = 0; i < CRIMES.length; i++) {
         for (int j = 0; j < NEIGHBORHOODS.length; j++) {
            totalCrimes[i] += neighborhoods[j][i];
         }
      }
      return totalCrimes;
   }
   
   // Reads the data in the input scanner and computes how many of each
   //   type of crime occured in the specified neighborhood. Returns
   //   an array where each element is the count of the type of crime
   //   with the corresponding index in the list of crimes above.
   //
   // Scanner data - the input file to read from
   // int neighborhood - the number of the neighborhood chosen by the user
   public static int[] processData(Scanner data, int neighborhood) {
      int[] results = new int[CRIMES.length];
      
      data.nextLine();     // throw out line that contains only field names
      while (data.hasNextLine()) {
         String line = data.nextLine();
         String[] fields = line.split(",");
         
         String crime = fields[5];
         String currNeighborhood = fields[10];
         
         int neighborhoodIndex = Arrays.binarySearch(NEIGHBORHOODS, currNeighborhood);
         if (neighborhoodIndex == (neighborhood - 1)) {     // subract one because the menu starts at 1
            int index = Arrays.binarySearch(CRIMES, crime);
            if (index > 0) {
               results[index]++;
            }
         }
      }
      
      return results;
   }
   
   // Prints out the number of crimes of each type in the given array.
   //
   // int[] counts - the count of each type of crime.
   public static void printResults(int[] counts) {
      for (int i = 0; i < CRIMES.length; i++) {
         System.out.println(CRIMES[i] + ": " + counts[i]);
      }
   }
   
   // Reads the input file and produces a list of all crime or neighborhood
   //   names found in the file, which is then returned. This list can be used
   //   instead of the preprocessed list in case new crimes or neighborhoods
   //   were added to the file.
   //
   // Scanner data - the input file to read from
   // String category - either "c" to get a list of crimes or "n" to get
   //   a list of neighborhoods
   public static ArrayList<String> getValues(Scanner data, String category) {
      ArrayList<String> results = new ArrayList<String>();
      data.nextLine();
      while (data.hasNextLine()) {
         String line = data.nextLine();
         String[] fields = line.split(",");
         
         String val;
         if (category.equalsIgnoreCase("c")) {
            val = fields[5];
         } else {
            val = fields[10];
         }
         if (!results.contains(val)) {
            results.add(val);
         }
      }
      Collections.sort(results);
      return results;
   }
}