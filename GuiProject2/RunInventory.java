package GuiProject2;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;


public class RunInventory {
    public static void main(String[] args) {
        ArrayList<Item> itemList = new ArrayList<Item>();
        boolean running = true;
        Scanner scan = new Scanner(System.in);

        System.out.println("Hello welcome to the Inventory:");
        while (running){
            System.out.println("1. Add Comodity");
            System.out.println("2. Deplete Comodity Stock");
            System.out.println("3. Increase Comodity Stock");
            System.out.println("4. Display item details");
            System.out.println("5. Record a Sale");
            System.out.println("6. Exit");
            System.out.println("Enter the number with your choice:");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice){
                case 1:
                    System.out.println("Set the item name:");
                    String name = scan.nextLine();
                    System.out.println("Set the inventory number:");
                    int invnum = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Set the unit price:");
                    double price = scan.nextDouble();
                    scan.nextLine();
                    System.out.println("Set the minimum stock:");
                    int minStock = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Set the amount in stock:");
                    int amtStock = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Is it perishable? (yes/no)");
                    String perishable = scan.nextLine();
                    if (perishable.equalsIgnoreCase("yes")) {
                        System.out.println("Enter the expiration date (YYYY-MM-DD):");
                        String expDate = scan.nextLine();
                        itemList.add(new Perishable(name, invnum, price, minStock, amtStock, LocalDate.parse(expDate)));
                    } else {
                        System.out.println("Enter the warranty period in months:");
                        int warranty = scan.nextInt();
                        itemList.add(new nonPerishable(name, invnum, price, minStock, amtStock, warranty));
                    }
                    System.out.println("Item added successfully!");
                    break;

                
                    //Have to add exceptions 
                case 2:
                    System.out.println("Set the inventory number to deplete:");
                    int invtnum = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Set the amount to deplete:");
                    int amt = scan.nextInt();
                    scan.nextLine();
                    for (Item item : itemList) {
                        if (item.getInventoryNumber() == invtnum) {
                            item.depleteStock(amt);
                            System.out.println("New stock amount: " + item.getAmountInStock());
                        }
                    }
                    break;

                //Have to add exceptions 
                case 3:
                    System.out.println("Enter the inventory number to add:");
                    int inventnum = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter the amount to add:");
                    int amts = scan.nextInt();
                    scan.nextLine();
                    for (Item item : itemList) {
                        if (item.getInventoryNumber() == inventnum) {
                            item.addtostock(amts);
                            System.out.println("New stock amount: " + item.getAmountInStock());
                        }
                    }
                    break;
                case 4:
                    for (Item item : itemList) {
                        item.displayItem();
                    }
                    break;


                //Might have to add exceptions 
                case 5:
                    Sale sale = new Sale();
                    boolean run = true;
                    while (run) {
                        System.out.println("Enter the inventory number of the item you wish to add to the sale:");
                        int invnums = scan.nextInt();
                        scan.nextLine();
                        System.out.println("Enter the quantity you wish to sell:");
                        int qty = scan.nextInt();
                        scan.nextLine();
                        for (Item item : itemList) {
                            if (item.getInventoryNumber() == invnums) {
                                sale.items.add(new SaleItem(item.getName(), item.getInventoryNumber(), item.getUnitPrice(), qty));
                            }
                        }
                        System.out.println("Do you want to add another item to the sale? (yes/no)");
                        String response = scan.nextLine();
                        if (!response.equalsIgnoreCase("yes")) {
                            run = false;
                        }
                    }
                    System.out.println("Total sale price: $" + sale.calculatePrice());
                    break;

                    
                case 6:
                    System.out.println("Exiting the program. Goodbye!");
                    scan.close();
                    running = false;
                    break;
            }
        }
    }
}
