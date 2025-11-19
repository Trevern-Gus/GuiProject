package GuiProject2;

public class SaleItem {
    private double unitPrice;
    private int quantity;
    private double totalprice;
    private String invname;
    private int invnum;

    public SaleItem(String invname, int invnum, double unitPrice, int quantity){
        this.invname = invname;
        this.invnum = invnum;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalprice() {
        return getUnitPrice() * getQuantity();
    }

    public String getInvname() {
        return invname;
    }

    public void setInvname(String invname) {
        this.invname = invname;
    }

    public int getInvnum() {
        return invnum;
    }

    public void setInvnum(int invnum) {
        this.invnum = invnum;
    }
}
