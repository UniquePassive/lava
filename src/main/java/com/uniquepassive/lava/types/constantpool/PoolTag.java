package com.uniquepassive.lava.types.constantpool;

import java.util.HashMap;
import java.util.Map;

public enum PoolTag {

    UNKNOWN(-2),
    PHANTOM(-1),
    UTF8_STRING(1),
    INTEGER(3),
    FLOAT(4),
    LONG(5),
    DOUBLE(6),
    CLASS_REF(7),
    STRING_REF(8),
    FIELD_REF(9),
    METHOD_REF(10),
    INTERFACE_METHOD_REF(11),
    NAME_AND_TYPE_DESC(12),
    METHOD_HANDLE(15),
    METHOD_TYPE(16),
    INVOKE_DYNAMIC(18);

    private int id;

    PoolTag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Map<Integer, PoolTag> values = new HashMap<>();
    static {
        for (PoolTag tag : PoolTag.values()) {
            values.put(tag.id, tag);
        }
    }

    public static PoolTag getForId(int id) {
        PoolTag tag = values.get(id);

        if (tag != null) {
            return tag;
        }

        return UNKNOWN;
    }
}
