package com.forest.data;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DataProcess {

    private Map<String, Integer> wordsMap;
    private Map<String, String> labelsMap;
    private int sentenceLen;

    public DataProcess(String wordsFile, String labelsFile, int sentenceLen) {
        this.sentenceLen = sentenceLen;
        wordsMap = new HashMap<String, Integer>();
        labelsMap = new HashMap<String, String>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(wordsFile));
            String str = null;
            int i = 0;
            while ((str = in.readLine()) != null) {
                wordsMap.put(str, i);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        labelsMap = loadLabelsFile(labelsFile);
    }

    public List<Integer> str2index(String text) {
        List<Integer> vects = new ArrayList<Integer>();

        if (!(text == null || text.length() == 0)) {
            int len = text.length();
            for (int i = 0; i < len; i++) {
                Integer index = wordsMap.get("" + text.charAt(i));
                if (index != null) {
                    vects.add(index);
                }
            }
        }

        return vects;
    }

    public List<Integer> padding(List<Integer> vects) {
        if (!vects.isEmpty()) {
            if (vects.size() < sentenceLen) {
                for (int i = vects.size(); i < sentenceLen; i++) {
                    vects.add(7541);
                }
            }
        }

        return vects;
    }

    public String index2label(int index) {
        return labelsMap.get(String.valueOf(index));
    }

//    private Map<String, String> loadLabelsFile(String filepath) {
//        Map<String, String> maps = new HashMap<String, String>();
//        BufferedReader reader = null;
//        String laststr = "";
//        try {
//            FileInputStream fileInputStream = new FileInputStream(filepath);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            reader = new BufferedReader(inputStreamReader);
//            String tempString = null;
//            while ((tempString = reader.readLine()) != null) {
//                String retval[] = tempString.split("\t");
//                maps.put(retval[1], retval[0]);
//            }
//            reader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return maps;
//    }

    private Map<String, String> loadLabelsFile(String filepath) {
        Map<String, String> maps = new HashMap<String, String>();
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            int i = 0;
            while ((tempString = reader.readLine()) != null) {
                String s = tempString.replaceAll("\r|\n", "");
                maps.put(String.valueOf(i++), s);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maps;
    }

    public static void main(String[] args) throws java.lang.Exception {
        String msg = "Hello 你好!";
        int len = msg.length();

        for (int i = 0; i < len; i++) {
            System.out.println("Character " + msg.charAt(i));
        }
    }


    //UTF-8->GB2312
    public static String utf8Togb2312(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(
                                str.substring(i + 1, i + 3), 16));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    i += 2;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        String result = sb.toString();
        String res = null;
        try {
            byte[] inputBytes = result.getBytes("8859_1");
            res = new String(inputBytes, "UTF-8");
        } catch (Exception e) {
        }
        return res;

    }


    //GB2312->UTF-8
    public static String gb2312ToUtf8(String str) {

        String urlEncode = "";
        try {
            urlEncode = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncode;
    }


}
