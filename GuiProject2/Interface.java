package GuiProject2;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Interface extends JFrame {

    private ArrayList<Item> itemList;
    private DefaultListModel<Item> listModel;
    private JList<Item> itemJList;
    private JComboBox<Item> cbItems;

    private JTextField txtInventoryNumber, txtName, txtAmountInStock, txtMinStock, txtUnitPrice, txtExpiry, txtWarranty;
    private JButton btnAddItem, btnDeleteItem, btnAddStock, btnDepleteStock, btnSave;
    private int nextInventoryNumber = 1000;

    public Interface(ArrayList<Item> items) {
        this.itemList = items;
        setTitle("Inventory System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Sale", createSalePanel());
        add(tabbedPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

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

        listModel = new DefaultListModel<>();
        itemJList = new JList<>(listModel);
        itemJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemJList);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        refreshItemList();

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
        ArrayList<Sale> saleHist = new ArrayList<>();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbItems = new JComboBox<>();
        JTextField txtQty = new JTextField(5);

        JButton btnAdd = new JButton("Add");
        JButton btnRemove = new JButton("Remove");
        JButton btnRefresh = new JButton("Refresh");
        //panel
        topPanel.add(new JLabel("Item:"));
        topPanel.add(cbItems);
        topPanel.add(new JLabel("Qty:"));
        topPanel.add(txtQty);
        topPanel.add(btnAdd);
        topPanel.add(btnRemove);
        topPanel.add(btnRefresh);

        panel.add(topPanel, BorderLayout.NORTH);

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

        Runnable reloadDropdown = () -> {
            cbItems.removeAllItems();
            for (Item i : itemList) {
                cbItems.addItem(i);
            }
        };

        reloadDropdown.run();
        
        btnAdd.addActionListener(e -> {
            Item selected = (Item) cbItems.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select an item.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int qty = Integer.parseInt(txtQty.getText().trim());
                if (qty <= 0) {
                    throw new InvalidEntryException("Quantity must be positive.");
                }

                if (qty > selected.getAmountInStock()) {
                    throw new NotEnoughStockException("Not enough stock available. Available: " + selected.getAmountInStock());
                }

                double price = selected.calculatePrice();
                double lineTotal = qty * price;

                saleModel.addRow(new Object[]{
                    selected.getInventoryNumber(),
                    selected.getName(),
                    qty,
                    price,
                    lineTotal
                });

                updateTotal.run();
                txtQty.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid integer quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidEntryException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Entry", JOptionPane.ERROR_MESSAGE);
            } catch (NotEnoughStockException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRemove.addActionListener(e -> {
            int row = saleTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            saleModel.removeRow(row);
            updateTotal.run();
        });

        btnRefresh.addActionListener(e -> reloadDropdown.run());

        btnComplete.addActionListener(e -> {
            if (saleModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items in sale.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Sale sale = new Sale(new ArrayList<>());
            double totalSale = 0;

            for (int i = 0; i < saleModel.getRowCount(); i++) {
                int inv = (int) saleModel.getValueAt(i, 0);
                String name = (String) saleModel.getValueAt(i, 1);
                int qty = (int) saleModel.getValueAt(i, 2);
                double price = (double) saleModel.getValueAt(i, 3);
                double lineTotal = (double) saleModel.getValueAt(i, 4);

                for (Item it : itemList) {
                    if (it.getInventoryNumber() == inv) {
                        try {
                            it.depleteStock(qty);
                            if (it.getAmountInStock() < it.getMinimumStock()) {
                                JOptionPane.showMessageDialog(this,
                                        "Warning: '" + it.getName() + "' is below minimum stock.",
                                        "Stock Warning", JOptionPane.WARNING_MESSAGE);
                            }
                            if (it instanceof Perishable) {
                                LocalDate exp = ((Perishable) it).getExpirationDate();
                                if (exp != null && java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), exp) <= 5) {
                                    JOptionPane.showMessageDialog(this,
                                            "Warning: '" + it.getName() + "' is within 5 days of expiry.",
                                            "Stock Warning", JOptionPane.WARNING_MESSAGE);
                                }
                            }

                        } catch (InvalidEntryException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid quantity: " + ex.getMessage(),
                                    "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                            return;
                        } catch (NotEnoughStockException ex) {
                            JOptionPane.showMessageDialog(this, "Stock error: " + ex.getMessage(),
                                    "Not Enough Stock", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                SaleItem si = new SaleItem(name, inv, price, qty);
                sale.getSaleItems().add(si);
                totalSale += lineTotal;
            }

            saleHist.add(sale);
            saleModel.setRowCount(0);
            txtQty.setText("");
            txtTotal.setText("0.00");

            JOptionPane.showMessageDialog(this,
                    "Sale completed successfully!\nInvoice #: " + sale.getInvoicenum() + 
                    "\nTotal: $" + String.format("%.2f", totalSale),
                    "Sale Completed", JOptionPane.INFORMATION_MESSAGE);
            
            refreshItemList();
            reloadDropdown.run();
        });

        return panel;
    }

    private void refreshItemList() {
        listModel.clear();
        for (Item item : itemList) {
            listModel.addElement(item);
        }
        
        if (cbItems != null) {
            SwingUtilities.invokeLater(() -> {
                cbItems.removeAllItems();
                for (Item i : itemList) {
                    cbItems.addItem(i);
                }
            });
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

            txtInventoryNumber.setEditable(false);
            txtAmountInStock.setEditable(false);

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
            try {
                if (txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                selected.setName(txtName.getText());
                selected.setMinimumStock(Integer.parseInt(txtMinStock.getText()));
                selected.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));

                if (selected instanceof Perishable) {
                    ((Perishable) selected).setExpirationDate(LocalDate.parse(txtExpiry.getText()));
                } else if (selected instanceof nonPerishable) {
                    ((nonPerishable) selected).setWarranty(Integer.parseInt(txtWarranty.getText()));
                }
                
                refreshItemList();
                JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please check your numeric inputs.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No item selected. Click 'Add Item' to create a new item.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addNewItem() {
        try {
            int invNum = nextInventoryNumber;
            String name = JOptionPane.showInputDialog(this, "Enter item name:");
            if (name == null || name.trim().isEmpty()) return;
            
            String minStockStr = JOptionPane.showInputDialog(this, "Enter minimum stock:");
            if (minStockStr == null || minStockStr.trim().isEmpty()) return;
            int minStock = Integer.parseInt(minStockStr);
            
            String unitPriceStr = JOptionPane.showInputDialog(this, "Enter unit price:");
            if (unitPriceStr == null || unitPriceStr.trim().isEmpty()) return;
            double unitPrice = Double.parseDouble(unitPriceStr);
            
            for (Item item : itemList) {
                if (item.getName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "Item name already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Object[] options = {"Perishable", "Non-Perishable"};
            int choice = JOptionPane.showOptionDialog(this,
                "Select item type:",
                "Item Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
            
            Item newItem;
            if (choice == 0) {
                String expiryStr = JOptionPane.showInputDialog(this, "Enter expiry date (YYYY-MM-DD):");
                if (expiryStr == null || expiryStr.trim().isEmpty()) return;
                LocalDate expiry = LocalDate.parse(expiryStr);
                newItem = new Perishable(name, invNum, unitPrice, minStock, 0, expiry);
            } else {
                String warrantyStr = JOptionPane.showInputDialog(this, "Enter warranty (months):");
                if (warrantyStr == null || warrantyStr.trim().isEmpty()) return;
                int warranty = Integer.parseInt(warrantyStr);
                newItem = new nonPerishable(name, invNum, unitPrice, minStock, 0, warranty);
            }
            
            itemList.add(newItem);
            nextInventoryNumber++;
            refreshItemList();
            
            JOptionPane.showMessageDialog(this, "Item added successfully!\nInventory Number: " + invNum, "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteItem() {
        Item selected = itemJList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete '" + selected.getName() + "'?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                itemList.remove(selected);
                refreshItemList();
                txtInventoryNumber.setText("");
                txtName.setText("");
                txtAmountInStock.setText("");
                txtMinStock.setText("");
                txtUnitPrice.setText("");
                txtExpiry.setText("");
                txtWarranty.setText("");
                JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void changeStock(boolean add) {
        Item selected = itemJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                add ? "Enter amount to add to '" + selected.getName() + "':" : "Enter amount to deplete from '" + selected.getName() + "':");
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int amt = Integer.parseInt(input.trim());

            if (add) {
                selected.addStock(amt);
                JOptionPane.showMessageDialog(this, "Stock added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                selected.depleteStock(amt);

                if (selected.getAmountInStock() < selected.getMinimumStock()) {
                    JOptionPane.showMessageDialog(this,
                            "Warning: '" + selected.getName() + "' is below minimum stock.",
                            "Stock Warning", JOptionPane.WARNING_MESSAGE);
                }
                if (selected instanceof Perishable) {
                    LocalDate exp = ((Perishable) selected).getExpirationDate();
                    if (exp != null && java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), exp) <= 5) {
                        JOptionPane.showMessageDialog(this,
                                "Warning: '" + selected.getName() + "' is within 5 days of expiry.",
                                "Stock Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(this, "Stock depleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            txtAmountInStock.setText(String.valueOf(selected.getAmountInStock()));
            refreshItemList();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidEntryException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Entry", JOptionPane.ERROR_MESSAGE);
        } catch (NotEnoughStockException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        ArrayList<Item> items = new ArrayList<>();
    
        items.add(new Perishable("Milk", 1001, 2.99, 10, 25, LocalDate.now().plusDays(7)));
        items.add(new nonPerishable("Canned Beans", 1002, 1.49, 15, 30, 24));
        
        new Interface(items);
    }
}