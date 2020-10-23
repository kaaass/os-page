package net.kaaass.ospage.util;

import net.kaaass.ospage.simu.PageEntry;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.Vector;
import java.util.stream.Stream;

public class GuiUtils {

    public static void mapPageEntry(Vector<Vector<String>> dstVec, Collection<PageEntry> entries, String[] col) {
        entries.forEach(page -> {
            var newVec = new Vector<String>();
            Stream.concat(Stream.of(PageEntry.COMMON_COLUMNS), Stream.of(col))
                    .map(page::getAttribute)
                    .map(Object::toString)
                    .forEach(newVec::add);
            dstVec.add(newVec);
        });
    }

    public static void autoFitTableColumns(JTable myTable) {
        var header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();

        var columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            var column = (TableColumn) columns.nextElement();

            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier(), false, false,
                            -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferredWidth = (int) myTable.getCellRenderer(row, col)
                        .getTableCellRendererComponent(myTable, myTable.getValueAt(row, col),
                                false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferredWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }
}
