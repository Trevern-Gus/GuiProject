package GuiProject2;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;


public class RunInventory {
    private static final ArrayList<Sale> sales = new ArrayList<>();
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
            System.out.print("Enter the number with your choice: ");

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

        
                case 2:
                      System.out.println("\n--- Add to stock ---");
                      System.out.print("Inventory number: ");
                            int invNum = scan.nextInt();

                            Item productItem = null;
                            for (Item i : itemList) {
                                if (i.getInventoryNumber() == invNum) {
                                    productItem = i;
                                    break;
                                }
                            }

                            if (productItem == null) {
                                System.out.println("Item not found.");
                                break;
                            }

                            System.out.print("Amount of commodity: ");
                            int commoAmount = scan.nextInt();

                            try {
                                productItem.addStock(commoAmount);
                                System.out.println("Stock level updated.");
                            } catch (InvalidEntryException e) {
                                System.out.println(e.getMessage());
                            }
                    break;

                
                case 3:
                        System.out.println("\n--- Deplete Stock---");
                        System.out.print("Inventory number: ");
                        int inv = scan.nextInt();
                        System.out.print("Deplete Amount: ");
                        int amt = scan.nextInt();

                        Item itemFound = null;
                        for (Item itm : itemList) {
                            if (itm.getInventoryNumber() == inv) {
                                itemFound = itm;
                                break;
                            }
                        }

                        if (itemFound == null) {
                            System.out.println("Item not found.");
                            break;
                        }

                        try {
                            itemFound.depleteStock(amt);
                            System.out.println("Stock updated for item: " + itemFound.getName());
                        } catch (InvalidEntryException e) {
                            System.out.println(e.getMessage());
                        } catch (NotEnoughStockException e) {
                            System.out.println(e.getMessage());
                        }
            
                    break;
                case 4:
                    for (Item itemm : itemList) {
                        itemm.displayItem();
                    }
                    break;


                
                case 5:
                     System.out.println("\n---Input Sales---");
                        List<SaleItem> saleItems = new ArrayList<>();

                        while (true) {
                            System.out.print("Enter inventory number to sell (enter -1 to end session): ");
                            int inventNum = scan.nextInt();
                            if (inventNum == -1) break;

                            Item item = null;
                            for (Item i : itemList) {
                                if (i.getInventoryNumber() == inventNum) {
                                    item = i;
                                    break; 
                                }
                            }

                            if (item == null) {
                                System.out.println("Item not found.");
                                continue;
                            }

                            System.out.print("Quantity: ");
                            int quantity = scan.nextInt();

                            try {
                                item.depleteStock(quantity);

                                double unitPrice = item.calculatePrice();
                                SaleItem saleItem = new SaleItem(
                                    item.getName(),
                                    item.getInventoryNumber(),
                                    unitPrice,
                                    quantity
                                );
                                saleItems.add(saleItem);

                            } catch (InvalidEntryException e) {
                                System.out.println(e.getMessage());
                            } catch (NotEnoughStockException e) {
                                System.out.println(e.getMessage());
                            }
                        }

                        if (!saleItems.isEmpty()) {
                            Sale sale = new Sale(saleItems);
                            sales.add(sale);
                        
                            System.out.println("\nInvoice: #" + sale.getInvoicenum());
                            System.out.println("Date: " + sale.getDate());
                            for (SaleItem saleItem : sale.getSaleItems()) {
                                System.out.println("Item: " + saleItem.getInvname() + "\tQTY: " + saleItem.getQuantity());
                            }
                            System.out.printf("Total Price: $%.2f%n", sale.calculatePrice());
                        }
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
