// Libraries imported
import java.io.*;
import java.util.*;
import java.text.*;
    
    
//keyboard class to register user input easier, e.g having a message that would prompt you before making a selection, an error should your selection be out of bounds
//and the lowest and highest selection you can make
class Keyboard{
    private Scanner in;
    Keyboard(){
        in = new Scanner(System.in);
    }

    
    //function that passes the prompt before you make your input, the error should your input be out of bounds 
    //the lowest and highest index according to your array through the constructor
    public int readInt(String promptMsg, String errorMsg, int low, int high){
        int num = 0;
        String strInput;
        boolean valid = false;

        while(valid == false ){
            System.out.println(promptMsg);
            strInput = in.nextLine();

            try{
                num = Integer.parseInt(strInput);
                if (num >= low && num <= high)
                    valid = true;
                else
                    System.out.println(errorMsg);
            }
            catch(NumberFormatException e){
                System.out.println(errorMsg);
            }
        }
        return num;
    }
}

//class made to call items of our menu through methods
class MenuItem{
    private String name;
    private double price;
    MenuItem(String name, double price){
        this.name = name;
        this.price = price;
    }
    //our public accessors
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
}

public class coffeeShop {
    
    public static void showMenu(ArrayList<MenuItem> items){
        System.out.println("Welcome to Starbucks, please select a coffee!");
        System.out.println("=============================================");
        //display each item from our ArrayList through a for loop (incremented int i to display by each index)
        for(int i= 0; i < items.size(); i++){
            MenuItem item = items.get(i);
            //.getName is a method made through our class MenuItem to return the string name from our arraylist, same goes with .getPrice but returns a double
            System.out.printf("%d. %s \t %.2f\n", i + 1, item.getName(), item.getPrice());
        }
        System.out.printf("%d. Exit\n", items.size() + 1);
        System.out.println("=============================================");
    }

    
    //again using the method of getName and Price and having the constructor pass through the array list we are able to display our coffee selection
    public static void coffeeChoice(int choice, ArrayList<MenuItem> items){
        //after a selection is made through user input, we will display the choice followed by the price
        MenuItem item = items.get(choice - 1);
        System.out.printf("%s selected worth %.2f\n", item.getName(), item.getPrice());

    }

    //although a little more static, i went with an approach to return the string of my selected coffee that is able to call anywhere in our main
    public static String coffeeString(int choice, ArrayList<MenuItem> items){
        //after a selection is made through user input, we will display the choice followed by the price
        MenuItem item = items.get(choice - 1);
        return item.getName();
    }

    // same goes with the function above, just that it returns the price instead
    public static double coffeePrice(int choice, ArrayList<MenuItem> items){
        //after a selection is made through user input, we will display the choice followed by the price
        MenuItem item = items.get(choice - 1);
        return item.getPrice();
    }


    // this is the function made that takes our tendered amount and does the calculation and gives us our change
    public static void paymentCash (int choice, ArrayList<MenuItem> items){
        System.out.println("You have selected cash.");
        
        Scanner amountTendered = new Scanner(System.in);
        System.out.println("Please enter amount to tender: ");
        int cashGiven = amountTendered.nextInt();

        MenuItem item = items.get(choice - 1);
        double price = item.getPrice();

        double change = cashGiven - price;

        if (cashGiven < price){
            System.out.println("Invalid amount!");
        }

        else if (cashGiven > price){
            System.out.println("Your change is: " + (change));
        }

    }

    //function that was going to be used but used in main instead (hard coded to be able to differentiate VISA and Mastercard in our log)
    public static void paymentCard (int choice, ArrayList<MenuItem> items){
        System.out.println("You have selected card.");
        
        Scanner cardTypes = new Scanner(System.in);
        System.out.println("Please choose 1 for VISA or 2 for Mastercard");
        int cardSelection = cardTypes.nextInt();

        if (cardSelection == 1){
            System.out.println("You have selected VISA, thank you for shopping with us.");
        }
        else if (cardSelection == 2){
            System.out.println("You have selected Mastercard, thank you for shopping with us.");
        }
        else {
            System.out.println("Invalid input!");
        }

    }

    //reads from our menu to create the array list, as seen in the constructor it passes through our arraylist
    public static void readFile(String file, ArrayList<MenuItem> items){
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fileReader);
            String readLines;
            while((readLines = bufReader.readLine()) != null){
                String tokens[] = readLines.split(",");
                String name = tokens[0];
                double price = Double.parseDouble(tokens[1]);
                items.add(new MenuItem(name, price));
            }
            bufReader.close();
        }
        catch(IOException e){
            System.out.println(file + "has had an error reading");
        }
    }

    //we use this to make a log of the purchases made through the coffee shop
    public static void writeFile(String file, ArrayList<String> log){
        //passing not only file but true to make sure that instead of overwriting, we append
        try{FileWriter fileWriter = new FileWriter(file, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i = 0; i < log.size(); i++){
            printWriter.println(log.get(i));
        }
        fileWriter.close();
        }
        catch(IOException e){
            System.out.println(file + "had an error writting/logging");
        }
    }

    public static void main (String[] args){
        int selection;
        //our array lists used to display
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        ArrayList<String> log = new ArrayList<String>();
        ArrayList<String> card = new ArrayList<String>();

        card.add("VISA");
        card.add("Mastercard");
        
        //our set-up to display the date in a formatted fashion
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = dateFormat.format(now);

        //reads off of our menu.txt and outputs it into an the arraylist named menuItems
        readFile("menu.txt", menuItems);

        //makes it so that exit will always be one up on whatever is on the list
        int exit = menuItems.size() + 1;
        //calls our keyboard object that we have made earlier
        Keyboard key = new Keyboard();

        //calling our showMenu function to display the arraylist menuItems with formatting in cluded
        showMenu(menuItems);

        //the variable selection uses the keyboard object from earlier and readInt as the method that has been described above
        selection = key.readInt("Enter your choice of coffee:", "Invalid Choice!", 1, exit);
        
        coffeeChoice(selection, menuItems);
        
        //by using Scanner we are able to get the input from the user
        Scanner input = new Scanner(System.in);
        System.out.println("Press 1 if you would like to pay by cash, press 2 if you would like to pay by Card");

        int paymentSelection = input.nextInt();

        if (paymentSelection == 1){
            //calls our function paymentCash
            paymentCash(paymentSelection, menuItems);
            //our format to add to the arraylist which will then add to the file using our writeFile function
            log.add(strDate + " " + coffeeString(selection, menuItems) + " " + "$" + coffeePrice(selection, menuItems) + " CASH");
            writeFile("log.txt", log);
        }

        if (paymentSelection == 2){
        System.out.println("You have selected card.");
        
        Scanner cardTypes = new Scanner(System.in);
        System.out.println("Please choose 1 for VISA or 2 for Mastercard");
        int cardSelection = cardTypes.nextInt();

        //a little more of a hard coded approach with a little bit of repitition in order to achieve what type of card is used in the log.
        if (cardSelection == 1){
            System.out.println("You have selected VISA, thank you for shopping with us.");
            log.add(strDate + " " + coffeeString(selection, menuItems) + " " + "$" + coffeePrice(selection, menuItems) + " Visa");
            writeFile("log.txt", log);
        }
        else if (cardSelection == 2){
            System.out.println("You have selected Mastercard, thank you for shopping with us.");
            log.add(strDate + " " + coffeeString(selection, menuItems) + " " + "$" + coffeePrice(selection, menuItems) + " Mastercard");
            writeFile("log.txt", log);
        }
        else if (cardSelection < 1 || cardSelection > 2 ) {
            System.out.println("Invalid input!");
        }

        if (paymentSelection < 1 || paymentSelection > 2) {
            System.out.println("Invalid choice!");
        }
    }
}
}