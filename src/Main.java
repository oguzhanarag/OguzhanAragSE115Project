// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};



    //bu verileri işleyebilmek için 3 boyutlu bir array
    static int[][][] profits = new int[MONTHS][DAYS][COMMS];

    private static int indexOfCommodity(String name) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) return i;//parametre olarak gelen isimle aynı olup olmadığını kontrol eder
        }
        return -1;
    }

    private static boolean validMonth(int m) {
        return m >= 0 && m < MONTHS;
    }


    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    //spesifik bir dosyanın içindeki verileri almayı sağlayan kısım.
    public static void loadData() {
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 1 || month > MONTHS) return "Invalid month";//ocaktan önce ay yok yani birşey returnleyemeyiz
        int m = month - 1;
        long[] totals = new long[COMMS];
        for (int d = 0; d < DAYS; d++) {
            for (int c = 0; c < COMMS; c++) totals[c] += data[m][d][c];//datalar daha yüklenmedi çünkü bilmiom
        }
        int best = 0;
        for (int c = 1; c < COMMS; c++) if (totals[c] > totals[best]) best = c;
        return commodities[best];//eğer herhangi bir comm diğerinden daha iyiyse her seferinde onu en iyi olarak atiyom fonksiyon sonunda en iyi oluyo
    }

    public static int totalProfitOnDay(int month, int day) {
        return 1234;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        return 1234;
    }

    public static int bestDayOfMonth(int month) {
        return 1234;
    }

    public static String bestMonthForCommodity(String comm) {
        return "DUMMY";
    }

    public static int consecutiveLossDays(String comm) {
        return 1234;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        return 1234;
    }

    public static int biggestDailySwing(int month) {
        return 1234;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        return "DUMMY is better by 1234";
    }

    public static String bestWeekOfMonth(int month) {
        return "DUMMY";
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}