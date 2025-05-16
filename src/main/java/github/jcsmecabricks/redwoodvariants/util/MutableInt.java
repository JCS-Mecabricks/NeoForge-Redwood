package github.jcsmecabricks.redwoodvariants.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.Mutable;

public class MutableInt extends Number implements Comparable<org.apache.commons.lang3.mutable.MutableInt>, Mutable<Number> {
    private static final long serialVersionUID = 512176391864L;
    private int value;
    public MutableInt() {
    }

    public MutableInt(final int value) {
        this.value = value;
    }

    public MutableInt(final Number value) {
        this.value = value.intValue();
    }


    public MutableInt(final String value) {
        this.value = Integer.parseInt(value);
    }

    public void add(final int operand) {
        this.value += operand;
    }

    public void add(final Number operand) {
        this.value += operand.intValue();
    }

    public int addAndGet(final int operand) {
        this.value += operand;
        return value;
    }

    public int addAndGet(final Number operand) {
        this.value += operand.intValue();
        return value;
    }

    @Override
    public int compareTo(final org.apache.commons.lang3.mutable.MutableInt other) {
        return NumberUtils.compare(this.value, other.intValue());
    }


    public void decrement() {
        value--;
    }

    public int decrementAndGet() {
        value--;
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof org.apache.commons.lang3.mutable.MutableInt) {
            return value == ((org.apache.commons.lang3.mutable.MutableInt) obj).intValue();
        }
        return false;
    }

    @Override
    public float floatValue() {
        return value;
    }

    public int getAndAdd(final int operand) {
        final int last = value;
        this.value += operand;
        return last;
    }

    public int getAndAdd(final Number operand) {
        final int last = value;
        this.value += operand.intValue();
        return last;
    }

    public int getAndDecrement() {
        final int last = value;
        value--;
        return last;
    }

    public int getAndIncrement() {
        final int last = value;
        value++;
        return last;
    }

    @Override
    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    @Override
    public int hashCode() {
        return value;
    }

    public void increment() {
        value++;
    }

    public int incrementAndGet() {
        value++;
        return value;
    }

    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.intValue();
    }

    public void subtract(final int operand) {
        this.value -= operand;
    }

    public void subtract(final Number operand) {
        this.value -= operand.intValue();
    }

    public Integer toInteger() {
        return Integer.valueOf(intValue());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
