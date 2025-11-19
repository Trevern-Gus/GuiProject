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

    public void depleteStock(int amt) {
        if (getAmountInStock() > amt){
            setAmountInStock(getAmountInStock()- amt);
            if (getAmountInStock() < getMinimumStock()){
                System.out.println("Alert the stock is lower than the minimum threshold!");
            }
        }
        else{
            System.out.println("There is not enough in stock for the order");
        }
    }

    public double calculatePrice(){
        return getUnitPrice() * 1.17;
    }
    
}
