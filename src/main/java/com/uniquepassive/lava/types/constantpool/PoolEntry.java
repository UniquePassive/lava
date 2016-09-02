package com.uniquepassive.lava.types.constantpool;

public class PoolEntry {

    private PoolTag tag;
    private Object value;

    public PoolEntry(PoolTag tag, Object value) {
        this.tag = tag;
        this.value = value;
    }

    public boolean isValid() {
        return tag != PoolTag.PHANTOM
                && tag != PoolTag.UNKNOWN;
    }

    public PoolTag getTag() {
        return tag;
    }

    public void setTag(PoolTag tag) {
        this.tag = tag;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
