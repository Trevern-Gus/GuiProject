package GuiProject2;

public abstract class Item{
    private String name;
    private int inventoryNumber;
    private double unitPrice;
    private int minimumStock;
    private int amountInStock;

    public Item(String name, int inventoryNumber, double unitPrice, int minimumStock, int amountInStock){
        this.name = name;
        this.inventoryNumber = inventoryNumber;
        this.unitPrice = unitPrice;
        this.minimumStock = minimumStock;
        this.amountInStock = amountInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public void addStock(int commodity) throws InvalidEntryException{
        if(commodity < 0){
            throw new InvalidEntryException("Invalid. Only positive integers are allowed.");
        }
        this.amountInStock += commodity;
    }

    public void displayItem(){
        System.out.println("name:" + getName() + " inventoryNumber:" + getInventoryNumber() + " unitPrice:" + getUnitPrice() + " Stock:" + getAmountInStock());
    }

    abstract void depleteStock(int amount) throws InvalidEntryException, NotEnoughStockException;

    public abstract double calculatePrice();

    @Override
    public String toString() {
        return name + " (Inv#: " + inventoryNumber + ")";
    }
    
}

