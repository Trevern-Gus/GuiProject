package GuiProject2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Interface extends JFrame {

    private JTabbedPane mainPane;
    private JPanel inventory, sales;
    private JToolBar toolbar;
    private JButton addItembtn, deleteItembtn, addStockbtn, depleteStockbtn, Savebtn;
    private JScrollPane scrollPane;
    private JList<String> itemList;

    public Interface(){
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        initComponents();
        initEvents();
    }

    public void initComponents(){
        mainPane = new JTabbedPane();
        inventory = new JPanel();
        sales = new JPanel();
        toolbar = new JToolBar();
        addItembtn = new JButton("Add Item");
        deleteItembtn = new JButton("Delete Item");
        addStockbtn = new JButton("Add Stock");
        depleteStockbtn = new JButton("Deplete Stock");
        Savebtn = new JButton("Save");
        scrollPane = new JScrollPane();
        itemList = new JList<String>();

        itemList.setVisibleRowCount(1);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.add(itemList);
        scrollPane.setViewportView(itemList);

        

        toolbar.add(addItembtn);
        toolbar.add(deleteItembtn);
        toolbar.add(addStockbtn);
        toolbar.add(depleteStockbtn);
        toolbar.add(Savebtn);
        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        


        inventory.add(toolbar);
        inventory.add(scrollPane);

        mainPane.add(inventory);
        mainPane.add(sales);

        mainPane.addTab("Inventory", inventory);
        mainPane.addTab("Sales", sales);

        add(mainPane);
        

    }

    public void initEvents(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent p){
             System.exit(1);
            }
        });
    }
    
    public static void main(String[] arg) {
        Interface gui = new Interface();
        gui.setVisible(true);
    }
}
