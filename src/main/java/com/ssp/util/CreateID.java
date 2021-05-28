package com.ssp.util;

import java.util.UUID;

public class CreateID {

    //生成一个唯一且好看点的用户ID
    public static String createID(){
        UUID id = UUID.randomUUID();//借用Java自带的UUID生成一个不重复的编号
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"};
        StringBuffer stringBuffer = new StringBuffer();
        String uid = id.toString().replace("-", "");
        for (int i=0;i<8;i++){
            String str = uid.substring(i*4,i*4+4);
            int strInteger = Integer.parseInt(str,16);
            stringBuffer.append(chars[strInteger % 0x3E]);
        }
        return stringBuffer.toString();
    }



}
