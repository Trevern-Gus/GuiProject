package GuiProject2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sale{
    private LocalDate date;
    private int invoicenum;
    private ArrayList<SaleItem> items;

    public Sale(List<SaleItem> saleItems){
        this.items = new ArrayList(saleItems);
        this.date = LocalDate.now();
        ArrayList<Integer> uniques = new ArrayList<Integer>();
        Random rand = new Random();
        int num = rand.nextInt(1000000);
        while (uniques.contains(num)){
            num = rand.nextInt(1000000);
        }
        this.invoicenum = num;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getInvoicenum() {
        return invoicenum;
    }
    public List<SaleItem> getSaleItems() {
         return items; 
    }

    public double calculatePrice(){

      double total = 0.0;
        for (SaleItem i : items) {
            total += i.getTotalprice();
        }
        return total;
    }

}