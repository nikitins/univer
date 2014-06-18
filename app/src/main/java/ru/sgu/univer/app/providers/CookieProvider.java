package ru.sgu.univer.app.providers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CookieProvider {
    public static final String cookie = "__utma=84755787.1464350218.1401944832.1402257886.1402333625.17; __utmz=84755787.1401944832.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); JSESSIONID=D0AC429A5F68EAEE4B4BBD063A2E35A2; __utmb=84755787.2.10.1402333625; __utmc=84755787; SPRING_SECURITY_REMEMBER_ME_COOKIE=cG96ZG55YWtvdnZhOjE0MDM1NDMyMjgwNTY6YjVkNjI3MjZjNGQxYmUyNzAxNDZmYTYwMmYxYjIzMTE";

    public static String getCookie(){
        File file = new File("/sdcard/Download/filec.txt");
        String s = "";
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                s += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }
}
