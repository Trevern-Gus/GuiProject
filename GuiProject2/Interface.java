package GuiProject2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Interface extends JFrame {

    private ArrayList<Item> itemList;

    private DefaultListModel<Item> listModel;
    private JList<Item> itemJList;

    private JTextField txtInventoryNumber, txtName, txtAmountInStock, txtMinStock, txtUnitPrice, txtExpiry, txtWarranty;
    private JButton btnAddItem, btnDeleteItem, btnAddStock, btnDepleteStock, btnSave;

    public Interface(ArrayList<Item> items) {
        this.itemList = items;
        setTitle("Inventory System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Sale", new JPanel()); // Placeholder for Sale Tab
        add(tabbedPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Toolbar
        JToolBar toolbar = new JToolBar();
        btnAddItem = new JButton("Add Item");
        btnDeleteItem = new JButton("Delete Item");
        btnAddStock = new JButton("Add Stock");
        btnDepleteStock = new JButton("Deplete Stock");
        btnSave = new JButton("Save");

        toolbar.add(btnAddItem);
        toolbar.add(btnDeleteItem);
        toolbar.add(btnAddStock);
        toolbar.add(btnDepleteStock);
        toolbar.add(btnSave);

        panel.add(toolbar, BorderLayout.NORTH);

        // Item List
        listModel = new DefaultListModel<>();
        itemJList = new JList<>(listModel);
        itemJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemJList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Data Panel
        JPanel dataPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        dataPanel.add(new JLabel("Inventory Number:"));
        txtInventoryNumber = new JTextField();
        txtInventoryNumber.setEditable(false);
        dataPanel.add(txtInventoryNumber);

        dataPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        dataPanel.add(txtName);

        dataPanel.add(new JLabel("Amount In Stock:"));
        txtAmountInStock = new JTextField();
        txtAmountInStock.setEditable(false);
        dataPanel.add(txtAmountInStock);

        dataPanel.add(new JLabel("Minimum Stock:"));
        txtMinStock = new JTextField();
        dataPanel.add(txtMinStock);

        dataPanel.add(new JLabel("Unit Price:"));
        txtUnitPrice = new JTextField();
        dataPanel.add(txtUnitPrice);

        dataPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        txtExpiry = new JTextField();
        dataPanel.add(txtExpiry);

        dataPanel.add(new JLabel("Warranty (months):"));
        txtWarranty = new JTextField();
        dataPanel.add(txtWarranty);

        panel.add(dataPanel, BorderLayout.SOUTH);

        // Populate list
        refreshItemList();

        // Event Listeners
        itemJList.addListSelectionListener(e -> populateFields());

        btnSave.addActionListener(e -> saveItem());
        btnAddItem.addActionListener(e -> addNewItem());
        btnDeleteItem.addActionListener(e -> deleteItem());
        btnAddStock.addActionListener(e -> changeStock(true));
        btnDepleteStock.addActionListener(e -> changeStock(false));

        return panel;
    }

    private void refreshItemList() {
        listModel.clear();
        for (Item item : itemList) {
            listModel.addElement(item);
        }
    }

    private void populateFields() {
        Item selected = itemJList.getSelectedValue();
        if (selected != null) {
            txtInventoryNumber.setText(String.valueOf(selected.getInventoryNumber()));
            txtName.setText(selected.getName());
            txtAmountInStock.setText(String.valueOf(selected.getAmountInStock()));
            txtMinStock.setText(String.valueOf(selected.getMinimumStock()));
            txtUnitPrice.setText(String.valueOf(selected.getUnitPrice()));

            if (selected instanceof Perishable) {
                txtExpiry.setText(((Perishable) selected).getExpirationDate().toString());
                txtWarranty.setText("");
                txtWarranty.setEditable(false);
                txtExpiry.setEditable(true);
            } else if (selected instanceof nonPerishable) {
                txtWarranty.setText(String.valueOf(((nonPerishable) selected).getWarranty()));
                txtExpiry.setText("");
                txtExpiry.setEditable(false);
                txtWarranty.setEditable(true);
            }
        }
    }

    private void saveItem() {
        Item selected = itemJList.getSelectedValue();
        if (selected != null) {
            selected.setName(txtName.getText());
            selected.setMinimumStock(Integer.parseInt(txtMinStock.getText()));
            selected.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));

            if (selected instanceof Perishable) {
                ((Perishable) selected).setExpirationDate(LocalDate.parse(txtExpiry.getText()));
            } else if (selected instanceof nonPerishable) {
                ((nonPerishable) selected).setWarranty(Integer.parseInt(txtWarranty.getText()));
            }
            refreshItemList();
        }
    }

    private void addNewItem() {
        txtInventoryNumber.setText("");
        txtName.setText("");
        txtAmountInStock.setText("");
        txtMinStock.setText("");
        txtUnitPrice.setText("");
        txtExpiry.setText("");
        txtWarranty.setText("");
        itemJList.clearSelection();
    }

    private void deleteItem() {
        Item selected = itemJList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this item?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                itemList.remove(selected);
                refreshItemList();
                addNewItem(); // clear fields
            }
        }
    }

    private void changeStock(boolean add) {
        Item selected = itemJList.getSelectedValue();
        if (selected != null) {
            String input = JOptionPane.showInputDialog(this,
                    add ? "Enter amount to add:" : "Enter amount to deplete:");
            if (input != null && !input.isEmpty()) {
                int amt = Integer.parseInt(input);
                if (add) {
                    selected.addtostock(amt);
                } else {
                    selected.depleteStock(amt);
                }
                txtAmountInStock.setText(String.valueOf(selected.getAmountInStock()));
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Item> items = new ArrayList<>();
    
        new Interface(items);
    }
}
