package GuiProject2;
import java.time.LocalDate;

public class Perishable extends Item {  
    private LocalDate expirationDate;

    public Perishable(String name, int inventoryNumber, double unitPrice, int minimumStock, int amountInStock, LocalDate expirationDate){
        super(name, inventoryNumber, unitPrice, minimumStock, amountInStock);
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void depleteStock(int amt){
        if (getAmountInStock() > amt){
            setAmountInStock(getAmountInStock()- amt);
            if (getAmountInStock() < getMinimumStock()){
                System.out.println("Alert the stock is lower than the minimum threshold!");
            }
        }
        else{
            System.out.println("There is not enough in stock for the order");
        }

        if (!expirationDate.isBefore(LocalDate.now())){
            System.out.println("Alert the item has expired!");
        }
    
    }

    // credit to copilot for helping me with this section specifially .isBefore here is the prompt used: if i wanted to find out if an expiration date was after a ceratin date what would i type, i wanna use a comparison like before to check
    public double calculatePrice(){
        if (getExpirationDate().isBefore(LocalDate.now())){
            return ((getUnitPrice() * 1.17) * .5);
        }
        else{
            return getUnitPrice() * 1.17;
        }
    }
    
}
