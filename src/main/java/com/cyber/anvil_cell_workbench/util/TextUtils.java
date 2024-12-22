package com.cyber.anvil_cell_workbench.util;

public class TextUtils {

    public static TextUtils instance = new TextUtils();

    public String cleanInputOfStyling(String string) {
        if (string == null) {
            return null;
        }
        String regex = "§[0-9a-fk-or]";
        return string.replaceAll(regex, "");
    }
}
