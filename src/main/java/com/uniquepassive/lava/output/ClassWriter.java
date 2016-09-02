package com.uniquepassive.lava.output;

import com.uniquepassive.lava.types.ShiftedArrayList;
import com.uniquepassive.lava.types.constantpool.AttributeItem;
import com.uniquepassive.lava.types.constantpool.PoolEntry;
import com.uniquepassive.lava.types.constantpool.tags.ClassMemberReference;
import com.uniquepassive.lava.types.constantpool.tags.InvokeDynamic;
import com.uniquepassive.lava.types.constantpool.tags.MethodHandle;
import com.uniquepassive.lava.types.constantpool.tags.NameAndTypeDesc;
import com.uniquepassive.lava.types.info.RawClassInfo;
import com.uniquepassive.lava.types.info.RawFieldInfo;
import com.uniquepassive.lava.types.info.RawMethodInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ClassWriter {

    public byte[] write(RawClassInfo rawClassInfo)
            throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);

        writeMagic(outputStream);
        writeVersion(rawClassInfo, outputStream);
        writeConstantPool(rawClassInfo, outputStream);
        writeAccessFlags(rawClassInfo, outputStream);
        writeClassIndices(rawClassInfo, outputStream);
        writeInterfaces(rawClassInfo, outputStream);
        writeFields(rawClassInfo, outputStream);
        writeMethods(rawClassInfo, outputStream);
        writeAttributes(rawClassInfo, outputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private void writeAttributes(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort(rawClassInfo.getAttributes().size());

        writeAttributes(outputStream, rawClassInfo.getAttributes());
    }

    private void writeAttributes(DataOutputStream outputStream, List<AttributeItem> attributes)
            throws IOException {

        for (AttributeItem attribute : attributes) {
            outputStream.writeShort(attribute.getNameIndex());
            outputStream.writeInt(attribute.getInfo().length);
            outputStream.write(attribute.getInfo());
        }
    }

    private void writeMethods(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort(rawClassInfo.getMethods().size());

        for (RawMethodInfo method : rawClassInfo.getMethods()) {
            outputStream.writeShort(method.getAccessFlags());
            outputStream.writeShort(method.getNameIndex());
            outputStream.writeShort(method.getDescIndex());
            outputStream.writeShort(method.getAttributes().size());

            writeAttributes(outputStream, method.getAttributes());
        }
    }

    private void writeFields(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort(rawClassInfo.getFields().size());

        for (RawFieldInfo field : rawClassInfo.getFields()) {
            outputStream.writeShort(field.getAccessFlags());
            outputStream.writeShort(field.getNameIndex());
            outputStream.writeShort(field.getDescIndex());
            outputStream.writeShort(field.getAttributes().size());

            writeAttributes(outputStream, field.getAttributes());
        }
    }

    private void writeInterfaces(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort(rawClassInfo.getInterfaceIndices().size());

        for (Integer idx : rawClassInfo.getInterfaceIndices()) {
            outputStream.writeShort(idx);
        }
    }

    private void writeClassIndices(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort(rawClassInfo.getClassIndex());
        outputStream.writeShort(rawClassInfo.getSuperClassIndex());
    }

    private void writeConstantPool(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        ShiftedArrayList<PoolEntry> constantPool = rawClassInfo.getConstantPool();

        outputStream.writeShort(constantPool.size() + 1);

        for (PoolEntry item : constantPool) {
            if (item.isValid()) {
                outputStream.writeByte(item.getTag().getId());

                switch (item.getTag()) {
                    case UTF8_STRING:
                        outputStream.writeUTF((String) item.getValue());
                        break;

                    case INTEGER: {
                        outputStream.writeInt((int) item.getValue());
                        break;
                    }

                    case FLOAT: {
                        outputStream.writeFloat((float) item.getValue());
                        break;
                    }

                    case LONG: {
                        outputStream.writeLong((long) item.getValue());
                        break;
                    }

                    case DOUBLE: {
                        outputStream.writeDouble((double) item.getValue());
                        break;
                    }

                    case CLASS_REF: {
                        outputStream.writeShort((int) item.getValue());
                        break;
                    }

                    case STRING_REF:
                        outputStream.writeShort((int) item.getValue());
                        break;

                    case FIELD_REF: {
                        ClassMemberReference classMemberReference = (ClassMemberReference) item.getValue();
                        outputStream.writeShort((short) classMemberReference.classIndex);
                        outputStream.writeShort((short) classMemberReference.nameAndTypeIndex);
                        break;
                    }

                    case METHOD_REF: {
                        ClassMemberReference classMemberReference = (ClassMemberReference) item.getValue();
                        outputStream.writeShort((short) classMemberReference.classIndex);
                        outputStream.writeShort((short) classMemberReference.nameAndTypeIndex);
                        break;
                    }

                    case INTERFACE_METHOD_REF: {
                        ClassMemberReference classMemberReference = (ClassMemberReference) item.getValue();
                        outputStream.writeShort((short) classMemberReference.classIndex);
                        outputStream.writeShort((short) classMemberReference.nameAndTypeIndex);
                        break;
                    }

                    case NAME_AND_TYPE_DESC: {
                        NameAndTypeDesc nameAndTypeDesc = (NameAndTypeDesc) item.getValue();
                        outputStream.writeShort((short) nameAndTypeDesc.nameIndex);
                        outputStream.writeShort((short) nameAndTypeDesc.descIndex);
                        break;
                    }

                    case METHOD_HANDLE:
                        MethodHandle methodHandle = (MethodHandle) item.getValue();
                        outputStream.writeByte((byte) methodHandle.referenceType);
                        outputStream.writeShort((short) methodHandle.referenceIndex);
                        break;

                    case METHOD_TYPE: {
                        outputStream.writeShort((int) item.getValue());
                        break;
                    }

                    case INVOKE_DYNAMIC: {
                        InvokeDynamic invokeDynamic = (InvokeDynamic) item.getValue();
                        outputStream.writeShort((short) invokeDynamic.bootStrapMethodIndex);
                        outputStream.writeShort((short) invokeDynamic.nameAndTypeIndex);
                        break;
                    }
                }
            }
        }
    }

    private void writeAccessFlags(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort((short) rawClassInfo.getAccessFlags());
    }

    private void writeVersion(RawClassInfo rawClassInfo, DataOutputStream outputStream)
            throws IOException {

        outputStream.writeShort((short) rawClassInfo.getMinorVersion());
        outputStream.writeShort((short) rawClassInfo.getMajorVersion());
    }

    private void writeMagic(DataOutputStream outputStream)
            throws IOException {

        outputStream.writeInt(0xCAFEBABE);
    }
}
