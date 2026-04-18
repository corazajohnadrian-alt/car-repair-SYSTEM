/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
/**
 *
 * @author Administrator
 */
public class ModernTable extends JTable {

    public ModernTable() {
        // 1. Basic Table Styling
        setRowHeight(40); // Matches the height of your custom buttons
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(0, 102, 115, 40)); // Transparent teal selection
        setSelectionForeground(Color.BLACK);
        setFocusable(false);
        
        // 2. Style the Header
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setCursor(new Cursor(Cursor.HAND_CURSOR));
        getTableHeader().setDefaultRenderer(new HeaderRenderer());
        
        // 3. Style the Rows
        setDefaultRenderer(Object.class, new RowRenderer());
    }

    // Custom Header Styling
    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(new Color(0, 102, 115)); // Your FIXO Teal
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 13f));
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            return this;
        }
    }

    // Custom Row Styling (Alternating Colors)
    private class RowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (isSelected) {
                c.setBackground(new Color(0, 102, 115, 50));
            } else {
                // Alternating light grey rows for CTU standard professionalism
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
            }
            
            setBorder(noFocusBorder);
            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }
}
