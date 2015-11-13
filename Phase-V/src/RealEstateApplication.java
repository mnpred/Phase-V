/*
    Phase V by:
                Nasser Al-Zughaibi; Gno: 00765920; nalzugha@masonlive.gmu.edu
                Michael Poulos; G no: 00905202; mpoulos@masonlive.gmu.edu
                Sava Vukomanovic; G no: 00821360; svukoman@masonliv@gmu.edu
    
*/
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class RealEstateApplication 
{
    public static void main(String[] args) throws IOException
    {
        //Initialize the maximum amount of houses as well as the array of objects to store all information
        int MAX_HOUSES = 25;
        House[] data = new House[MAX_HOUSES];
        
        //Store the path of the file so edits re-write to the same file.
        String path = chooseFile(data);
        
        //Initialilze the menu.
        int option = 0;
        do
        {
            option = showMenu();
            switch (option)
            {
                 case 0:
                    editHouseInfo(data, path);
                    break;
                 case 1: 
                    viewHousingInfo(data);
                    break;
                 case 2:
                    JOptionPane.showMessageDialog(null, "Goodbye");
                    break;
            }
        }while(option != 2);
        
        
    }
    /*Method purpose: To find the file that is to be read into the program.
      Method parameters: House[] data object
      Return: String path, path of the file for re-writing purposes.
    */
    public static String chooseFile(House[] data) throws FileNotFoundException
    {
        //Initialize all the variables
        int xCord = -1;
        int yCord = -1;
        String name = " ";
        double money = 0.0;
        int status = -1;
        int type = -1;
        Scanner in = null;
        int i = 0;
        String comma;
        
        //JFileChooser opens a dialog box to choose a specefic file regardelss if its in the same location as the .java file.
        JFileChooser aFile = new JFileChooser();
        int result = aFile.showOpenDialog(null);
        String path = aFile.getSelectedFile().getPath();
        if (result == JFileChooser.APPROVE_OPTION)
        {
            try{
                //Receives the file from the given path from JFileChooser
                in = new Scanner(new FileInputStream(new File(path)));
                while(in.hasNextLine()){
                    //There's a comma after each variable.
                    xCord = Integer.parseInt(in.next());
                    comma = in.next();

                    yCord = Integer.parseInt(in.next());
                    comma = in.next();

                    name = in.next();
                    comma = in.next();

                    money = Double.parseDouble(in.next());
                    comma = in.next();

                    status = Integer.parseInt(in.next());
                    comma = in.next();

                    type = Integer.parseInt(in.next());            
                    
                    //At the end we pass everything to the House class constructor
                    data[i] = new House(xCord, yCord, name, money, status, type);
                    i++;  
                }
                in.close();         
            }catch(IllegalArgumentException f){
             JOptionPane.showMessageDialog(null, f.getMessage());
            }   
            catch(NoSuchElementException e){}

        }
        return path;
    }
    /*Method purpose: To present the user with a simple menu.
      Method parameters: None.
      Return: int choice, which is the menu choice.
    */
    public static int showMenu ()
    {
       Object[] options = {"Edit housing information", "View housing informaiton", "Exit"};
       int choice = JOptionPane.showOptionDialog(null, "What would you like to do?", "Main Menu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
       
       return choice;
    }
    /*Method purpose: To receive coordinates and ask what to edit.
      Method parameters: House[] data object, and String path.
      Return: None.
    */
    public static void editHouseInfo(House[] data, String path)
    {
        int xCoordinate = 0;
        int yCoordinate = 0;
        String grid = "";
        boolean confirmation = false;
        do
        {
            //Begin the coordinate methods.
            xCoordinate = xCoordinates(data);
            yCoordinate = yCoordinates(data);

            //Asks the user if the choice is correct. If not then the user will be asked for the coordinates again.
            confirmation = JOptionPane.showConfirmDialog(null, "You have chosen coordinates X: " + xCoordinate + " & Y: " + yCoordinate + "\nIs that correct?") == JOptionPane.YES_OPTION;
        }while(confirmation == false);
        
        //Search through the objects for the house
        int x = 0; boolean valid = false;
        do
        {
            if (xCoordinate == data[x].getXCoordinate() && yCoordinate == data[x].getYCoordinate())
            {
                valid = true;
            }
            else x++;
        }while(valid == false);
        //Start the edit menu process.
        int option = 0;
        do
        {
            //After receiving the choice for the menu it'll go to the choices method and also save any changes back to the file again.
            option = editMenu();
            switch (option)
            {
                 case 0:
                    editPropertyType(data, x);
                    saveToFile(data, path);
                    break;
                 case 1: 
                    editPrice(data, x);
                    saveToFile(data, path);
                    break;
                 case 2:
                    editAgentName(data, x);
                    saveToFile(data, path);
                    break;
                 case 3: 
                    editStatus(data, x);
                    saveToFile(data, path);
                    break;
                 case 4:
                    JOptionPane.showMessageDialog(null, "Returning to Main menu");
                    break;
            }
        }while(option != 4);
    }
    /*Method purpose: To present the user with a simple menu to edit housing information.
      Method parameters: None.
      Return: int choice, which is the menu choice.
    */
    public static int editMenu()
    {
        Object[] options = {"Property type", "Price", "Agent name", "For sale/sold", "Done editing"};
       int choice = JOptionPane.showOptionDialog(null, "What would you like to edit?", "Edit Menu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
       
       return choice;
    }
    /*
      Method purpose: Presents the user with a simple menu for property type
      Method parameters: House data object, and int x for the object which coordinates it corresponds to.
      Return: None.
    */
    public static void editPropertyType(House[] data, int x)
    {
        Object[] property = {"Single family home", "Townhouse", "Condo", "Apartment"};
        int propertyType = 0;
        try
        {
            propertyType = JOptionPane.showOptionDialog(null, "Which type of property is this home?", "Property edit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, property, property[0]);
        }catch(IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        data[x].setType(propertyType);
    }
    /*
      Method purpose: Prompt the user for the price of the house.
      Method parameters: House data object, and int x for the object which coordinates it corresponds to.
      Return: None.
    */
    public static void editPrice(House[] data, int x)
    {
        double price = 0;
        boolean valid = false;
        
        do
        {
            //Prompts the user for the price, retries if an error occurs
            try
            {
                price = Double.parseDouble(JOptionPane.showInputDialog("Please enter the property price"));
                data[x].setHousePrice(price);
                valid = true;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Price must be entered in a numaric format");
                valid = false;
            }catch(IllegalArgumentException f){
                JOptionPane.showMessageDialog(null, f.getMessage());   
                valid = false;
            }

        }while(!valid);       
    }
    /*
      Method purpose: Prompt the user for the name of the agent for editing.
      Method parameters: House data object, and int x for the object which coordinates it corresponds to.
      Return: None.
    */
    public static void editAgentName(House[] data, int x)
    {
        //Prompt for the name, only reprompts if an error occurs.
        String name = "";
        boolean valid = false;
        do
        {
            try
            {
                name = JOptionPane.showInputDialog(null, "Please enter the agents name");
                data[x].setAgentName(name);
                valid = true;
            }catch(IllegalArgumentException f){
                JOptionPane.showMessageDialog(null, f.getMessage());
                valid = false;
            }
        }while(!valid);
    }
    /*
      Method purpose: Prompt the user for the status of the house.
      Method parameters: House data object, and int x for the object which coordinates it corresponds to.
      Return: None.
    */
    public static void editStatus(House[] data, int x)
    {
       //present the user with a buttom prompt. Then simply pass the choice into the house object.
       Object[] status = {"For sale", "Sold", "N/A"};
       int propertyStatus = 0;
       try
        {
            propertyStatus = JOptionPane.showOptionDialog(null, "What is the status of this home??", "Property edit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, status, status[0]);
        }catch(IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
       data[x].setHouseStatus(propertyStatus);
    }
    /*
      Method purpose: Gives the user all information of the house based on the inputted coordinates
      Method parameters: House data array of objects.
      Return: None.
    */
    public static void viewHousingInfo(House[] data)
    {
        int xCoordinate = 0;
        int yCoordinate = 0;
        String grid = "";
        boolean confirmation = false;
        do
        {
            //Begin the coordinate methods.
            xCoordinate = xCoordinates(data);
            yCoordinate = yCoordinates(data);

            //Asks the user if the choice is correct. If not then the user will be asked for the coordinates again.
            confirmation = JOptionPane.showConfirmDialog(null, "You have chosen coordinates X: " + xCoordinate + " & Y: " + yCoordinate + "\nIs that correct?") == JOptionPane.YES_OPTION;
        }while(confirmation == false);
        
        //Search for the coordinates and print the toString of the matching house.
        for(int i=0; i<data.length; i++){
         if(data[i].getXCoordinate() == xCoordinate && data[i].getYCoordinate() == yCoordinate){
            JOptionPane.showMessageDialog(null, data[i].toString()); 
         }
        }
         
    }
    /*
      Method purpose: To get the X coordinates from the user.
      Method parameters: House data array of objects.
      Return: The X coordinates.
    */
    public static int xCoordinates(House[] data)
    {
        int xCoordinate = 0;
        String grid = "";
        boolean valid = true;
        //Get the X coordinates
            do
            {
                //printGrid gives back a 5x5 grid of the houses.
                grid = printGrid(data);
                try
                {
                    xCoordinate = Integer.parseInt(JOptionPane.showInputDialog("X denotes house on the market, O for Sold, "
                            + "and * for not handled by this reaslestate company.\n"
                            +grid + "\n\nPlease enter the X coordinate. Must be between 1 & 5"));
                if (xCoordinate < 0 || xCoordinate > 5)
                {
                    JOptionPane.showMessageDialog(null, "ERROR!  Please enter an integer between 1 and 5");
                    valid = false;
                }
                }catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Coordinates MUST be entered in numaric format");
                    valid = false;
                }
            }while (!valid);
        return xCoordinate;
    }
    /*
      Method purpose: To get the Y coordinates from the user.
      Method parameters: House data array of objects.
      Return: The Y coordinates.
    */
    public static int yCoordinates(House[] data)
    {
        int yCoordinate = 0;
        String grid = "";
        boolean valid = true;
        //Get the Y coordinates
        do
        {
            //printGrid gives back a 5x5 grid of the houses.
            grid = printGrid(data);
            try
            {
            yCoordinate = Integer.parseInt(JOptionPane.showInputDialog("X denotes house on the market, O for Sold, "
                            + "and * for not handled by this reaslestate company.\n"
                            +grid + "\n\nPlease enter the X coordinate. Must be between 1 & 5"));
            if (yCoordinate < 0 || yCoordinate > 5)
            {
                JOptionPane.showMessageDialog(null, "ERROR!  Please enter an integer between 1 and 5");
                valid = false;
            }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Coordinates MUST be entered in numaric format");
                valid = false;
            }
        }while (!valid);
        return yCoordinate;
    }
    
    /*
      Method purpose: Prints the X, O, and * in a grid format for the houses.
      Method parameters: House data array of objects.
      Return: String grid, used to show as the user enters in the previous methods.
    */
    public static String printGrid(House[] data)
    {
        String grid = "";
        for (int j = 0; j < data.length; j++)
        {            
         switch(j){
            case(5):
            case(10):
            case(15):
            case(20): grid+= "\n";            
         }
         grid+=data[j].ToSymbol();        
        }
        return grid;
    }
    /*
      Method purpose: To print to the same file chosen in the beginning of the program.
      Method parameters: House data array of objects, and String path which is the path to the original file.
      Return: None.
    */
    public static void saveToFile(House[] data, String path){
        //instantiates a new object of the PrintWriter class
        PrintWriter op = null;
        try{
           //This prompts the user to enter a txt file name of the users choosing. The txt file will be placed in the .java file's directory
              op = new PrintWriter(new FileOutputStream(new File(path)));
        }catch(FileNotFoundException e){
           JOptionPane.showMessageDialog(null, "The file was unable to be created/updated, please check the permissions on the current directory and try again","Real Estate Application", JOptionPane.ERROR_MESSAGE);
        }
        for(int i=0; i<data.length; i++){
           op.println(data[i].getXCoordinate() + " , " + data[i].getYCoordinate() + " , " + data[i].getAgentName() + " , " + String.format("%.0f", data[i].getPrice()) + " , " + data[i].getStatus() + " , " + data[i].getType());
        }
        op.close();
        JOptionPane.showMessageDialog(null, "Information has successfully been saved to file to: \n" + path);
    }
}
