package com.harveycoombs.journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Tools {
    public static String getURLParameter(String path) {
        String param = "";

        Pattern pattern = Pattern.compile("/[A-Za-z0-9/.]*");
        boolean valid = pattern.matcher(path.substring(1)).find();

        if (valid) {
            param = path.substring(path.lastIndexOf("/") + 1);
        }

        return param;
    }

    public static byte[] fileContents(String filepath) throws IOException {
        File target = new File(filepath);

        FileInputStream stream = new FileInputStream(target);
        byte[] data = new byte[(int)target.length()];

        stream.read(data);
        stream.close();

        return data;
    }

    public static HashMap<String, String> parseQueryString(String qs) {
        HashMap<String, String> kvPairs = new HashMap<String, String>();

        String[] pairs = qs.split("&");

        for (String pair : pairs) {
            String[] parts = pair.split("=");
            kvPairs.put(parts[0], parts[1]);
        }

        return kvPairs;
    }
}