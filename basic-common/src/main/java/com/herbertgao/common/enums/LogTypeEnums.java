package com.herbertgao.common.enums;

public enum LogTypeEnums {

    HTTP("HTTP"),
    DUBBO("DUBBO"),
    Null;

    private String type;

    LogTypeEnums() {}

    LogTypeEnums(String type) {
        this.type = type;
    }

    public static LogTypeEnums valueOfType(String type) {
        for (LogTypeEnums obj : LogTypeEnums.values()) {
            if (java.util.Objects.equals(obj.type, type)) {
                return obj;
            }
        }
        return Null;
    }

    public String getType() {
        return type;
    }
}
