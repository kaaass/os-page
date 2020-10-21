package net.kaaass.ospage.simu;

import net.kaaass.ospage.util.StringUtils;

import java.util.Random;

/**
 * 内存地址
 */
public class Address {

    private int addr;

    private Address(int addr) {
        this.addr = addr;
    }

    public int raw() {
        return addr;
    }

    public int prefix() {
        return addr >> PageEntry.FRAME_SIZE_BIT_LEN;
    }

    public int offset() {
        return addr & (PageEntry.FRAME_SIZE - 1);
    }

    public static Address of(int value) {
        return new Address(value);
    }

    public static Address of(int prefix, int offset) {
        return of((prefix << PageEntry.FRAME_SIZE_BIT_LEN) | (offset & (PageEntry.FRAME_SIZE - 1)));
    }

    public static Address map(Address org, int dstPrefix) {
        return of(dstPrefix, org.offset());
    }

    public static Address random() {
        var rand = new Random();
        return of(rand.nextInt((1 << 16) - 1));
    }

    @Override
    public String toString() {
        return String.format("Address{ prefix = %d, offset = %s }",
                prefix(),
                StringUtils.padBinary(offset(), 12));
    }
}
