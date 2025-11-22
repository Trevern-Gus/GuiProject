package GuiProject2;

public class nonPerishable extends Item{
    private int warranty;

    public nonPerishable(String name, int inventoryNumber, double unitPrice, int minimumStock, int amountInStock, int warranty){
        super(name, inventoryNumber, unitPrice, minimumStock, amountInStock);
        this.warranty = warranty;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    
    @Override
    public void depleteStock(int amount) throws InvalidEntryException, NotEnoughStockException{
        if (amount < 0){
            throw new InvalidEntryException("Invalid. Only enter positive numbers.");
        }

        if (amount > getAmountInStock()){
            throw new NotEnoughStockException("Not enough in stock.");
        }

        int currentStock = getAmountInStock() - amount;
        setAmountInStock(currentStock);
        if (getAmountInStock() < getMinimumStock()){
            System.out.println("Stock level is low");
        }
    }


    public double calculatePrice(){
        return getUnitPrice() * 1.17;
    }
    
}
