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

   

    @Override
    public void depleteStock(int amount) throws InvalidEntryException, NotEnoughStockException{

        if (amount < 0){
            throw new InvalidEntryException("Invalid. Only enter positive numbers.");
        }

        if (amount > getAmountInStock()){
            throw new NotEnoughStockException("Not enough in stock.");
        }

        LocalDate todayDate = LocalDate.now();
        if(!getExpirationDate().isAfter(todayDate.plusDays(2))){
            System.out.println("This stock is expiring soon!");
        }
        if (getAmountInStock()> 0 ){
            int currentStock = getAmountInStock() - amount;
            setAmountInStock(currentStock);
        }

        if(getAmountInStock() < getMinimumStock()){
            System.out.println("Stock is running low.\n");
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
