package catalog.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringListConverters {

    public static String listToString(List<String> listToTransfer) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String actual : listToTransfer) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(actual);
        }
        return sb.toString();
    }

    public static List<String> stringToList(String stringToTransfer) {
        String[] parts = stringToTransfer.split(", ");
        List<String> result = new ArrayList<>();
        Collections.addAll(result, parts);
        return result;
    }
}
