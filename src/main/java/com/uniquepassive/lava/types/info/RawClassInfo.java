package com.uniquepassive.lava.types.info;

import com.uniquepassive.lava.types.ShiftedArrayList;
import com.uniquepassive.lava.types.constantpool.AttributeItem;
import com.uniquepassive.lava.types.constantpool.PoolEntry;

import java.util.List;

public class RawClassInfo {

    private int majorVersion;
    private int minorVersion;

    private ShiftedArrayList<PoolEntry> constantPool;

    private int accessFlags;

    private int classIndex;
    private int superClassIndex;

    private List<Integer> interfaceIndices;

    private List<RawFieldInfo> fields;
    private List<RawMethodInfo> methods;

    private List<AttributeItem> attributes;

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public ShiftedArrayList<PoolEntry> getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ShiftedArrayList<PoolEntry> constantPool) {
        this.constantPool = constantPool;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public int getSuperClassIndex() {
        return superClassIndex;
    }

    public void setSuperClassIndex(int superClassIndex) {
        this.superClassIndex = superClassIndex;
    }

    public List<Integer> getInterfaceIndices() {
        return interfaceIndices;
    }

    public void setInterfaceIndices(List<Integer> interfaceIndices) {
        this.interfaceIndices = interfaceIndices;
    }

    public List<RawFieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<RawFieldInfo> fields) {
        this.fields = fields;
    }

    public List<RawMethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<RawMethodInfo> methods) {
        this.methods = methods;
    }

    public List<AttributeItem> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeItem> attributes) {
        this.attributes = attributes;
    }
}
