package GuiProject2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Sale{
    private LocalDate date;
    private int invoicenum;
    public ArrayList<SaleItem> items;

    public Sale(){
        this.items = new ArrayList<SaleItem>();
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
    
    public double calculatePrice(){
        double total = 0.00;
        for (int i=0; i < items.size(); i++){
            total += items.get(i).getTotalprice();

        }

        return total;
    }

}