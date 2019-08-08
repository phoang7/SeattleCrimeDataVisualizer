// Phillip Hoang
// 1/6/18
// This class creates a data table of crimes in each neighborhood in a city

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
   
public class CrimeTable {
   private Scanner read;
   private ArrayList<String> textFile;

   private String[][] rowCells;
   private String[] columnNames; 
   private String[][] rowCellsDuplicate;
   private JTable table;
   private JScrollPane tableScrollPane;
   private JScrollPane textBoxScrollPane;
   private JPanel panel;
   private GridBagConstraints c;
   private JMenuBar menuBar;
   private JMenu menu;
   private JFrame[] frames;
   private JMenuItem[] items;
   private JRadioButton[] radioButtons;
   private JToggleButton toggleButton;
   private ButtonGroup buttonGroup;
   private JTextArea textBox;
   
   private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
   private GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
   private Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
      
   // Constructs a CrimeTable object to display a data table of crimes
   // in each neighborhood in a city
   public CrimeTable(String[][] rowCells, String[] columnNames, String[][] rowCellsDuplicate) throws FileNotFoundException {
      this.read = new Scanner(new File("README.txt"));
      this.textFile = new ArrayList<String>();
      
      this.frames = new JFrame[]{new JFrame("Crime Data"), new JFrame("Tools")};
      this.items = new JMenuItem[]{new JMenuItem("Tools"), new JMenuItem("Exit")};
      
      while (this.read.hasNextLine()) {
         this.textFile.add(this.read.nextLine());
      }
      
      this.rowCells = rowCells;
      this.columnNames = columnNames;
      this.rowCellsDuplicate = rowCellsDuplicate;
      
      this.items[0].addActionListener(openTools);
      
      this.menu = new JMenu("Options");
      this.menu.add(this.items[0]);
      this.items[1].addActionListener(exitApp);
      this.menu.add(this.items[1]);
      this.menuBar = new JMenuBar();
      this.menuBar.add(this.menu);
      this.frames[0].setJMenuBar(this.menuBar);
      
      this.table = new JTable(this.rowCells, this.columnNames);
      this.table.setDefaultEditor(Object.class, null); // disables user editing cells
      this.tableScrollPane = new JScrollPane(this.table);
      this.frames[0].add(this.tableScrollPane);
      this.frames[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.frames[0].setExtendedState(this.frames[0].getExtendedState() | JFrame.MAXIMIZED_BOTH);
      this.frames[0].setVisible(true);
      
      this.panel = new JPanel();
      this.panel.setLayout(new GridBagLayout());
      this.c = new GridBagConstraints();
      this.c.fill = GridBagConstraints.BOTH;
      this.toggleButton = new JToggleButton("Show/Unshow Percentages per Crime " +
         "In Each Neighborhood", false);
      this.toggleButton.addActionListener(actionListener);
      this.c.gridwidth = 4;
      this.c.gridx = 0;
      this.c.gridy = 0;
      this.panel.add(this.toggleButton, this.c);
      this.c.gridy = 2;
      this.c.ipady = 100;
      this.textBox = new JTextArea(30, 30);
      this.textBoxScrollPane = new JScrollPane(this.textBox);
      this.panel.add(this.textBoxScrollPane, this.c);
      this.c.gridwidth = 1;
      this.c.gridy = 1;
      this.c.ipady = 0;
      this.buttonGroup = new ButtonGroup();
      this.radioButtons  = new JRadioButton[]{new JRadioButton("Key"), new JRadioButton("Data"),
         new JRadioButton("Clear"), new JRadioButton("View README")};
      for (int i = 0; i < this.radioButtons.length; i++) {
         this.buttonGroup.add(this.radioButtons[i]);
         this.panel.add(this.radioButtons[i], this.c);
         this.c.gridx++;
      }
      this.radioButtons[2].setSelected(true);
      this.radioButtons[0].addActionListener(key);
      this.radioButtons[1].addActionListener(data);
      this.radioButtons[2].addActionListener(clear);
      this.radioButtons[radioButtons.length - 1].addActionListener(viewReadMe);
      this.frames[1].add(this.panel);
      this.frames[1].pack();
      this.frames[1].setResizable(false);
      int x = (int) rect.getMaxX() - this.frames[1].getWidth();
      this.frames[1].setLocation(x, 0);
      this.frames[1].setLocationRelativeTo(null);
      
   }
   
   ActionListener viewReadMe = new ActionListener() {
      
      public void actionPerformed(ActionEvent actionEvent) {
         textBox.setText("");
         for (int i = 0; i < textFile.size(); i++) {
            textBox.append(textFile.get(i) + "\n");
         }
      }
   };
   
   ActionListener data = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
         textBox.setText("");
         textBox.append("Data of Crimes for Each Neighborhood\n\n");
         for (int i = 0; i < rowCellsDuplicate.length - 1; i++) {
            textBox.append(table.getValueAt(i, 0) + "\n");
            for (int j = 1; j < columnNames.length - 1; j++) {
               textBox.append("   " + columnNames[j] + ": ");
               textBox.append(table.getValueAt(i, j) + " / " + 
                     table.getValueAt(rowCellsDuplicate.length - 1, j));
               int total = Integer.parseInt((String) 
                     table.getValueAt(rowCellsDuplicate.length - 1, j));
               if (total != 0) {
                  double value = Integer.parseInt((String) table.getValueAt(i, j));
                  double percent = value / total * 100;
                  percent = Math.round(percent * 100.0) / 100.0;
                  textBox.append(" (" + percent + "%)\n");
               }
               else {
                  textBox.append("\n");
               }
            }
            textBox.append("\n");
         }
      }
      
      
   };
   
   ActionListener clear = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
         textBox.setText("");
      }
   };
   
   ActionListener key = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
         textBox.setText("");
         textBox.append("Key for column and row names\n\n");
         textBox.append("Column names:\n\n");
         for (int i = 0; i < columnNames.length; i++) {
            textBox.append(columnNames[i] + "\n");
         }
         textBox.append("\n");
         textBox.append("Row names:\n\n");
         for (int i = 0; i < rowCells.length; i++) {
            textBox.append(rowCells[i][0] + "\n");
         }
      }
   }; 
     
   ActionListener exitApp = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
         System.exit(0);
      }
   };
   
   ActionListener openTools = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         frames[1].setVisible(true);
      }
   };
   
   ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
         AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
         boolean selected = abstractButton.getModel().isSelected();
         if (selected) {
            for (int i = 0; i < rowCells.length; i++) {
               table.setValueAt("N/A", i, columnNames.length - 1);
            }
            for (int i = 1; i < columnNames.length - 1; i++) {
               int lastRowCellValue = Integer.parseInt((String) 
                     table.getValueAt(rowCells.length - 1, i));
               for (int j = 0; j < rowCells.length; j++) {
                  if (lastRowCellValue == 0) {
                     table.setValueAt("N/A", j, i);
                  }
                  else {
                     double value = Integer.parseInt((String) table.getValueAt(j, i));
                     double total = Integer.parseInt((String) table.getValueAt(rowCells.length - 1,
                           i));
                     double percent = value / total * 100;
                     percent = Math.round(percent * 100.0) / 100.0;
                     table.setValueAt(percent + "%", j, i);
                  }
               }
            }
         }
         else {
            for (int i = 1; i < columnNames.length; i++) {
               for (int j = 0; j < rowCells.length; j++) {
                  table.setValueAt(rowCellsDuplicate[j][i], j, i);
               }
            }
         }
      }
   };
   
}