package com.convert.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConvertToCSV {
    public static void writeCsv(List<Map<String, Object>> data, String filename) {
        try {
            FileWriter out = new FileWriter(filename);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(getHeaders(data.get(0))));

            for(Map<String, Object> row : data) {
                printer.printRecord(getValues(row));
            }

            printer.flush();
            printer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getHeaders(Map<String, Object> row) {
        return row.keySet().toArray(new String[0]);
    }

    public static Object[] getValues(Map<String, Object> row) {
        List<Object> values = new ArrayList<>();
        for(Object value : row.values()) {
            if(value == null) {
                values.add("");
            } else if(value instanceof String || value instanceof Number || value instanceof Boolean) {
                values.add(value);
            } else if(value instanceof Map) {
                values.add(convertMapToString((Map<String, Object>)value));
            } else if(value instanceof List) {
                values.add(convertListToString((List<Object>)value));
            } else if(value.getClass().isArray()) {
                values.add(convertArrayToString(value));
            } else {
                values.add(value.toString());
            }
        }
        return values.toArray();
    }

    private static String convertMapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(":");
            sb.append(entry.getValue().toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    private static String convertListToString(List<Object> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(Object obj : list) {
            if(obj instanceof String) {
                sb.append("\"");
                sb.append(obj);
                sb.append("\"");
            } else {
                sb.append(obj.toString());
            }
            sb.append(",");
        }
        if(list.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    private static String convertArrayToString(Object array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int length = java.lang.reflect.Array.getLength(array);
        for(int i = 0; i < length; i++) {
            Object value = java.lang.reflect.Array.get(array, i);
            if(value instanceof String) {
                sb.append("\"");
                sb.append(value);
                sb.append("\"");
            } else {
                sb.append(value.toString());
            }
            sb.append(",");
        }
        if(length > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
    public static void main(String[] args) {
        List<Map<String, Object>> data = new ArrayList<>();

        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("name", "John");
        row1.put("age", 30);
        row1.put("isMarried", true);
        row1.put("address", null);
        data.add(row1);

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("name", "Mary");
        row2.put("age", 25);
        row2.put("isMarried", false);
        List<String> hobbies = new ArrayList<>();
        hobbies.add("reading");
        hobbies.add("hiking");
        row2.put("hobbies", hobbies);
        data.add(row2);

        Map<String, Object> row3 = new LinkedHashMap<>();
        row3.put("name", "Bob");
        row3.put("age", 40);
        row3.put("isMarried", true);
        Map<String, Object> address = new LinkedHashMap<>();
        address.put("street", "123 Main St");
        address.put("city", "Anytown");
        address.put("state", "CA");
        row3.put("address", address);
        data.add(row3);

        Map<String, Object> row4 = new LinkedHashMap<>();
        row4.put("name", "Alice");
        row4.put("age", 28);
        row4.put("isMarried", false);
        Object[] array = new Object[] {1, 2, "three"};
        row4.put("array", array);
        data.add(row4);

        ConvertToCSV.writeCsv(data, "src/main/resources/data.csv");
    }
}
