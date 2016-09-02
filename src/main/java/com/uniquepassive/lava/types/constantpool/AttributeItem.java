package com.uniquepassive.lava.types.constantpool;

public class AttributeItem {

    private int nameIndex;
    private byte[] info;

    public AttributeItem(int nameIndex, byte[] info) {
        this.nameIndex = nameIndex;
        this.info = info;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }
}
