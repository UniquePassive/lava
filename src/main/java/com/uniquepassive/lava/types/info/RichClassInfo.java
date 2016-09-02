package com.uniquepassive.lava.types.info;

import com.uniquepassive.lava.types.constantpool.PoolEntry;
import com.uniquepassive.lava.types.constantpool.PoolTag;

import java.util.Optional;

public class RichClassInfo extends RawClassInfo {

    public RichClassInfo(RawClassInfo rawClassInfo) {
        this.setMinorVersion(rawClassInfo.getMinorVersion());
        this.setMajorVersion(rawClassInfo.getMajorVersion());

        this.setConstantPool(rawClassInfo.getConstantPool());

        this.setAccessFlags(rawClassInfo.getAccessFlags());

        this.setClassIndex(rawClassInfo.getClassIndex());
        this.setSuperClassIndex(rawClassInfo.getSuperClassIndex());

        this.setInterfaceIndices(rawClassInfo.getInterfaceIndices());

        this.setFields(rawClassInfo.getFields());
        this.setMethods(rawClassInfo.getMethods());

        this.setAttributes(rawClassInfo.getAttributes());
    }

    /**
     * Extracts the name of the class through the constant pool
     * and the this_class.name_index entry.
     *
     * @return name of the class
     */
    public Optional<String> getName() {
        String name = getClassInfoName(getClassIndex());

        if (name != null) {
            return Optional.of(name);
        }

        return Optional.empty();
    }

    /**
     * Replaces the UTF8 string referenced in this_class.name_index to contain the new String.
     *
     * @param newName the new string to set the class name to
     */
    public boolean setName(String newName) {
        return setClassInfoName(getClassIndex(), newName);
    }

    /**
     * Extracts the name of the super class through the constant pool
     * and the super_class.name_index entry.
     *
     * @return name of this class's super class
     */
    public Optional<String> getSuperClassName() {
        String name = getClassInfoName(getSuperClassIndex());

        if (name != null) {
            return Optional.of(name);
        }

        return Optional.empty();
    }

    /**
     * Replaces the UTF8 string referenced in super_class.name_index to contain the new String.
     *
     * @param newName the new string to set the class super name to
     */
    public boolean setSuperClassName(String newName) {
        return setClassInfoName(getSuperClassIndex(), newName);
    }

    private String getClassInfoName(int classInfoIndex) {
        int nameIndex = (int) getConstantPool()
                .get(classInfoIndex)
                .getValue();

        PoolEntry entry = getConstantPool()
                .get(nameIndex);

        if (entry.getTag() == PoolTag.UTF8_STRING) {
            return (String) entry.getValue();
        }

        return null;
    }

    private boolean setClassInfoName(int classInfoIndex, String newName) {
        int nameIndex = (int) getConstantPool()
                .get(classInfoIndex)
                .getValue();

        PoolEntry entry = getConstantPool().get(nameIndex);
        if (entry.getTag() == PoolTag.UTF8_STRING) {
            entry.setValue(newName);
            return true;
        }

        return false;
    }
}
