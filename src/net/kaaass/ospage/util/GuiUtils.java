package net.kaaass.ospage.util;

import net.kaaass.ospage.simu.PageEntry;

import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

public class GuiUtils {

    public static void mapPageEntry(Vector<Vector<String>> dstVec, List<PageEntry> entries, String[] col) {
        entries.forEach(page -> {
            var newVec = new Vector<String>();
            Stream.concat(Stream.of(PageEntry.COMMON_COLUMNS), Stream.of(col))
                    .map(page::getAttribute)
                    .map(Object::toString)
                    .forEach(newVec::add);
            dstVec.add(newVec);
        });
    }
}
