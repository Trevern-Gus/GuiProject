package GuiProject2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        tabbedPane.addTab("Sale", createSalePanel()); // Placeholder for Sale Tab
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

 private JPanel createSalePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // Save Sale history
    ArrayList<Sale> saleHist = new ArrayList<>();


    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    JComboBox<Item> cbItems = new JComboBox<>();
    JTextField txtQty = new JTextField(5);

    JButton btnAdd = new JButton("Add");
    JButton btnRemove = new JButton("Remove");
    JButton btnRefresh = new JButton("Refresh");

    topPanel.add(new JLabel("Item:"));
    topPanel.add(cbItems);

    topPanel.add(new JLabel("Qty:"));
    topPanel.add(txtQty);

    topPanel.add(btnAdd);
    topPanel.add(btnRemove);
    topPanel.add(btnRefresh);

    panel.add(topPanel, BorderLayout.NORTH);

    Runnable reloadDropdown = () -> {
        cbItems.removeAllItems();
        for (Item i : itemList) {
            cbItems.addItem(i); // uses Item.toString()
        }
    };

    reloadDropdown.run();

    String[] cols = {"Inv#", "Name", "Qty", "Unit Price", "Line Total"};
    DefaultTableModel saleModel = new DefaultTableModel(cols, 0);
    JTable saleTable = new JTable(saleModel);

    panel.add(new JScrollPane(saleTable), BorderLayout.CENTER);
    JPanel bottomPanel = new JPanel(new BorderLayout());

    JTextField txtTotal = new JTextField("0.00");
    txtTotal.setEditable(false);
    txtTotal.setFont(new Font("Arial", Font.BOLD, 16));

    JButton btnComplete = new JButton("Complete Sale");

    bottomPanel.add(txtTotal, BorderLayout.CENTER);
    bottomPanel.add(btnComplete, BorderLayout.EAST);

    panel.add(bottomPanel, BorderLayout.SOUTH);



    Runnable updateTotal = () -> {
        double total = 0;
        for (int i = 0; i < saleModel.getRowCount(); i++) {
            total += (double) saleModel.getValueAt(i, 4);
        }
        txtTotal.setText(String.format("%.2f", total));
    };
    //Add sale item to table
    btnAdd.addActionListener(e -> {

        Item selected = (Item) cbItems.getSelectedItem();
        if (selected == null) return;

        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText());
            if (qty <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer quantity.");
            return;
        }

        double price = selected.getUnitPrice();
        double lineTotal = qty * price;

        saleModel.addRow(new Object[]{
                selected.getInventoryNumber(),
                selected.getName(),
                qty,
                price,
                lineTotal
        });

        updateTotal.run();
    });
 // Remove item on line
    btnRemove.addActionListener(e -> {
        int row = saleTable.getSelectedRow();
        if (row != -1) {
            saleModel.removeRow(row);
            updateTotal.run();
        }
    });

    //refresh Dropdown
    btnRefresh.addActionListener(e -> reloadDropdown.run());

    // Finish Sale
    btnComplete.addActionListener(e -> {

        if (saleModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in sale.");
            return;
        }

        Sale sale = new Sale();

        // deduct stock and form sale items
        for (int i = 0; i < saleModel.getRowCount(); i++) {

            int inv = (int) saleModel.getValueAt(i, 0);
            String name = (String) saleModel.getValueAt(i, 1);
            int qty = (int) saleModel.getValueAt(i, 2);
            double price = (double) saleModel.getValueAt(i, 3);

            // Find item in itemList
            for (Item it : itemList) {
                if (it.getInventoryNumber() == inv) {
                    try {
                        it.depleteStock(qty);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                        return; 
                    }
                }
            }

            SaleItem si = new SaleItem(name, inv, price, qty);
            sale.items.add(si);
        }

        saleHist.add(sale); // Save the sale

        // Should probably hopefully Clear UI
        saleModel.setRowCount(0);
        txtQty.setText("");
        txtTotal.setText("0.00");

        JOptionPane.showMessageDialog(this,
                "Sale completed.\nTotal: $" + sale.calculatePrice());
    });



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
