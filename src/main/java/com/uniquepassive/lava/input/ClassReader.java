package com.uniquepassive.lava.input;

import com.uniquepassive.lava.types.ShiftedArrayList;
import com.uniquepassive.lava.types.constantpool.AttributeItem;
import com.uniquepassive.lava.types.constantpool.PoolEntry;
import com.uniquepassive.lava.types.constantpool.PoolTag;
import com.uniquepassive.lava.types.constantpool.tags.ClassMemberReference;
import com.uniquepassive.lava.types.constantpool.tags.InvokeDynamic;
import com.uniquepassive.lava.types.constantpool.tags.MethodHandle;
import com.uniquepassive.lava.types.constantpool.tags.NameAndTypeDesc;
import com.uniquepassive.lava.types.info.RawClassInfo;
import com.uniquepassive.lava.types.info.RawFieldInfo;
import com.uniquepassive.lava.types.info.RawMethodInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassReader {

    public RawClassInfo parse(byte[] classBytes)
            throws IOException {

        if (classBytes.length < 4) {
            throw new ClassReadingException("Class bytes length must be >= 4");
        }

        RawClassInfo rawClassInfo;

        try (DataInputStream classStream = new DataInputStream(new ByteArrayInputStream(classBytes))) {
            verifyMagic(classStream);

            rawClassInfo = new RawClassInfo();

            parseVersion(rawClassInfo, classStream);
            parseConstantPool(rawClassInfo, classStream);
            parseAccessFlags(rawClassInfo, classStream);
            parseClassIndices(rawClassInfo, classStream);
            parseInterfaces(rawClassInfo, classStream);
            parseFields(rawClassInfo, classStream);
            parseMethods(rawClassInfo, classStream);
            parseAttributes(rawClassInfo, classStream);
        }

        return rawClassInfo;
    }

    private void parseAttributes(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        int attributeCount = classStream.readUnsignedShort();

        rawClassInfo.setAttributes(parseAttributes(classStream, attributeCount));
    }

    private List<AttributeItem> parseAttributes(DataInputStream classStream, int attributeCount)
            throws IOException {

        List<AttributeItem> attributes = new ArrayList<>(attributeCount);

        for (int j = 0; j < attributeCount; j++) {
            int attributeNameIndex = classStream.readUnsignedShort();
            int attributeLength = classStream.readInt();

            byte[] attributeInfo = new byte[attributeLength];

            classStream.readFully(attributeInfo);

            attributes.add(new AttributeItem(attributeNameIndex, attributeInfo));
        }

        return attributes;
    }

    private void parseMethods(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        int methodCount = classStream.readUnsignedShort();

        List<RawMethodInfo> methods = new ArrayList<>(methodCount);

        for (int i = 0; i < methodCount; i++) {
            RawMethodInfo rawMethodInfo = new RawMethodInfo();

            rawMethodInfo.setAccessFlags(classStream.readUnsignedShort());
            rawMethodInfo.setNameIndex(classStream.readUnsignedShort());
            rawMethodInfo.setDescIndex(classStream.readUnsignedShort());
            int attributeCount = classStream.readUnsignedShort();

            rawMethodInfo.setAttributes(parseAttributes(classStream, attributeCount));

            methods.add(rawMethodInfo);
        }

        rawClassInfo.setMethods(methods);
    }

    private void parseFields(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        int fieldCount = classStream.readUnsignedShort();

        List<RawFieldInfo> fields = new ArrayList<>(fieldCount);

        for (int i = 0; i < fieldCount; i++) {
            RawFieldInfo rawFieldInfo = new RawFieldInfo();

            rawFieldInfo.setAccessFlags(classStream.readUnsignedShort());
            rawFieldInfo.setNameIndex(classStream.readUnsignedShort());
            rawFieldInfo.setDescIndex(classStream.readUnsignedShort());
            int attributeCount = classStream.readUnsignedShort();

            rawFieldInfo.setAttributes(parseAttributes(classStream, attributeCount));

            fields.add(rawFieldInfo);
        }

        rawClassInfo.setFields(fields);
    }

    private void parseInterfaces(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        int interfaceCount = classStream.readUnsignedShort();

        List<Integer> interfaceIndices = new ArrayList<>(interfaceCount);

        for (int i = 0; i < interfaceCount; i++) {
            interfaceIndices.add(classStream.readUnsignedShort());
        }

        rawClassInfo.setInterfaceIndices(interfaceIndices);
    }

    private void parseClassIndices(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        rawClassInfo.setClassIndex(classStream.readUnsignedShort());
        rawClassInfo.setSuperClassIndex(classStream.readUnsignedShort());
    }

    private void parseAccessFlags(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        rawClassInfo.setAccessFlags(classStream.readUnsignedShort());
    }

    private void parseConstantPool(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        int constantPoolCount = classStream.readUnsignedShort();

        ShiftedArrayList<PoolEntry> constantPool = new ShiftedArrayList<>(1, constantPoolCount);

        for (int i = 1; i < constantPoolCount; i++) {
            int rawTag = classStream.readUnsignedByte();
            PoolTag tag = PoolTag.getForId(rawTag);

            switch (tag) {
                case UTF8_STRING:
                    String utfString = classStream.readUTF();
                    constantPool.add(new PoolEntry(tag, utfString));
                    break;

                case INTEGER: {
                    int value = classStream.readInt();
                    constantPool.add(new PoolEntry(tag, value));
                    break;
                }

                case FLOAT: {
                    float value = classStream.readFloat();
                    constantPool.add(new PoolEntry(tag, value));
                    break;
                }

                case LONG: {
                    long value = classStream.readLong();
                    constantPool.add(new PoolEntry(tag, value));
                    constantPool.add(new PoolEntry(PoolTag.PHANTOM, null));
                    i++;
                    break;
                }

                case DOUBLE: {
                    double value = classStream.readDouble();
                    constantPool.add(new PoolEntry(tag, value));
                    constantPool.add(new PoolEntry(PoolTag.PHANTOM, null));
                    i++;
                    break;
                }

                case CLASS_REF: {
                    int nameIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, nameIndex));
                    break;
                }

                case STRING_REF:
                    int stringIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, stringIndex));
                    break;

                case FIELD_REF: {
                    ClassMemberReference classMemberReference = new ClassMemberReference();
                    classMemberReference.classIndex = classStream.readUnsignedShort();
                    classMemberReference.nameAndTypeIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, classMemberReference));
                    break;
                }

                case METHOD_REF: {
                    ClassMemberReference classMemberReference = new ClassMemberReference();
                    classMemberReference.classIndex = classStream.readUnsignedShort();
                    classMemberReference.nameAndTypeIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, classMemberReference));
                    break;
                }

                case INTERFACE_METHOD_REF: {
                    ClassMemberReference classMemberReference = new ClassMemberReference();
                    classMemberReference.classIndex = classStream.readUnsignedShort();
                    classMemberReference.nameAndTypeIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, classMemberReference));
                    break;
                }

                case NAME_AND_TYPE_DESC: {
                    NameAndTypeDesc nameAndTypeDesc = new NameAndTypeDesc();
                    nameAndTypeDesc.nameIndex = classStream.readUnsignedShort();
                    nameAndTypeDesc.descIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, nameAndTypeDesc));
                    break;
                }

                case METHOD_HANDLE:
                    MethodHandle methodHandle = new MethodHandle();
                    methodHandle.referenceType = classStream.readUnsignedByte();
                    methodHandle.referenceIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, methodHandle));
                    break;

                case METHOD_TYPE: {
                    int descIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, descIndex));
                    break;
                }

                case INVOKE_DYNAMIC: {
                    InvokeDynamic invokeDynamic = new InvokeDynamic();
                    invokeDynamic.bootStrapMethodIndex = classStream.readUnsignedShort();
                    invokeDynamic.nameAndTypeIndex = classStream.readUnsignedShort();
                    constantPool.add(new PoolEntry(tag, invokeDynamic));
                    break;
                }

                default: {
                    throw new ClassReadingException("Discovered unknown tag " + rawTag);
                }
            }
        }

        rawClassInfo.setConstantPool(constantPool);
    }

    private void parseVersion(RawClassInfo rawClassInfo, DataInputStream classStream)
            throws IOException {

        rawClassInfo.setMinorVersion(classStream.readUnsignedShort());
        rawClassInfo.setMajorVersion(classStream.readUnsignedShort());
    }

    private void verifyMagic(DataInputStream classStream)
            throws IOException {

        long magic = classStream.readInt() & 0xFFFFFFFFL;
        if (magic != 0xCAFEBABEL) {
            throw new ClassReadingException("Expected file magic CAFEBABE, but found "
                    + Long.toHexString(magic).toUpperCase());
        }
    }
}
