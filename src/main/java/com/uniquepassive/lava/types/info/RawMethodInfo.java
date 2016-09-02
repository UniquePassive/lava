package com.uniquepassive.lava.types.info;

import com.uniquepassive.lava.types.constantpool.AttributeItem;

import java.util.List;

public class RawMethodInfo {

    private int accessFlags;

    private int nameIndex;
    private int descIndex;

    private List<AttributeItem> attributes;

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getDescIndex() {
        return descIndex;
    }

    public void setDescIndex(int descIndex) {
        this.descIndex = descIndex;
    }

    public List<AttributeItem> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeItem> attributes) {
        this.attributes = attributes;
    }
}
