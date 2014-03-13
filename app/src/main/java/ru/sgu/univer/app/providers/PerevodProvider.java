package ru.sgu.univer.app.providers;

/**
 * Created by NIK on 13.03.14.
 */
public class PerevodProvider {
    public static int two = 10;
    public static int three = 20;
    public static int four = 30;
    public static int five = 40;

    public static int getOchen(int bal) {
        if (bal < two) return 2;
        if (bal < three) return 2;
        if (bal < four) return 3;
        if (bal < five) return 4;
        return 5;
    }
}
