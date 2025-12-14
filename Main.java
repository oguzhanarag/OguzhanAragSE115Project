// Main.java - Students version
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

    private static int indexOfCommodity(String name) {//ileride bizden istenen comm larla işlem yaparken kullanıcaz
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) return i;//bu methotda girdiğimiz commoditie ile karşılaştırma yapar aynı mı diye
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


    /* 1.Method
    Bir ay içindeki en değerli comm
     */
    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 1 || month > MONTHS) return "Invalid month";//ocaktan önce ay yok yani birşey returnleyemeyiz
        int m = month - 1;//arrayler 0 dan başladığı için mesela kullanıcı ocaak ayını isterse 0.indexe denk gelir bu yüzden 1 çıkarmak laızm
        long[] totals = new long[COMMS];
        for (int d = 0; d < DAYS; d++) {
            for (int c = 0; c < COMMS; c++) totals[c] += profits[m][d][c];//datalar daha yüklenmedi çünkü bilmiom
        }
        int best = 0;
        for (int c = 1; c < COMMS; c++) if (totals[c] > totals[best]) best = c;
        return commodities[best]; //eğer herhangi bir total diğerinden daha karlı ise her seferinde onu en iyi olarak atiyom
    }
    /* 2.Method
       Bir günün toplam karı
     */
    public static int totalProfitOnDay(int month, int day) {
        if (month < 1 || month > MONTHS || day < 1 || day > DAYS) return -1;//geçersiz giriş
        int m = month - 1;//arrayler 0 dan başladığı için -1 koymamız lazım
        int d = day - 1;
        int sum = 0;
        for (int c = 0; c < COMMS; c++) sum += profits[m][d][c]; //
        return sum;
    }
    /* 3.Method
    Bir commun belirli aralıkta toplam karını hesaplar(son günden girilen ilk güne kadar olan total kar)
     */
    public static int commodityProfitInRange(String commodity, int from, int to) {
        int index = indexOfCommodity(commodity);//bu comm kaçıncı indexde onu buluyo
        if (index == -1 || from < 1 || to < 1 || from > to) return -1;//geçersiz değer
        int sum = 0;
        //tüm yılı geziyor nested loop yaparak ocak ayı 1-28 yaparken ilk loopda m 1 olunca şubat ayında 1-28 yapıyor
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                int dayNumber = m * DAYS + d + 1;//burası önemli ay ve gün arasındaki ilişkiyi kuruyo ve ocak 28 çekerken şubata geçince 29. günden devam ediyor
                if (dayNumber >= from && dayNumber <= to) { //eğer yukarıdaki aralığın içindeyse karı sum a ekliyor
                    sum += profits[m][d][index];
                }
            }
        }
        return sum;
    }
    /* 4.Method
     Ayın en karlı günü bulma
     */
    public static int bestDayOfMonth(int month) {
        if (month < 1 || month > MONTHS) return -1; //ocaktan önce ay yok geçersiz
        int m = month - 1;
        long best = Long.MIN_VALUE; // bu javanın kendi kütüphanesinden bir sabit kendinden daha küçük bir değer görürse otomatik güncellenir
        int bestDay = 1;

        for (int d = 0; d < DAYS; d++) {
            long sum = 0;
            for (int c = 0; c < COMMS; c++) sum += profits[m][d][c];
            if (sum > best) {
                best = sum;
                bestDay = d + 1;   // d bir array indexi o yüzden 0 dan başlar fakat normal bi gün 0 olamicağı için +1 koyuyoz
            }
        }
        return bestDay;
    }
    /* 5.Method
    Her bir comm için en karlı ay
     */
    public static String bestMonthForCommodity(String comm) {
        int index = indexOfCommodity(comm);
        if (index == -1) return "INVALID";

        long best = Long.MIN_VALUE;// eğer en küçük değere atarsak karşısına çıkan her ilk değer en büyük değer olarak atanır
        int bestMonth = 1;

        for (int m = 0; m < MONTHS; m++) {
            long sum = 0;
            for (int d = 0; d < DAYS; d++) sum += profits[m][d][index];
            if (sum > best) {
                best = sum;
                bestMonth = m + 1;
            }
        }
        return months[bestMonth - 1]; //şuanda datalar yüklenmediği için ilk maks değer january yani january returnlicek
    }
    /* 6.Method
        istenen comm için zarar streaklerini bulucaz
     */
    public static int consecutiveLossDays(String comm) {
        int index = indexOfCommodity(comm);
        if (index == -1) return -1;
        int longest = 0;//en uzun streak
        int current = 0;//güncel streak sayısı

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profits[m][d][index] < 0) {// kazanç 0 dan küçükse zarardasındır :D
                    current++;
                    if (current > longest) longest = current;
                } else {
                    current = 0;
                }
            }
        }
        return longest;
    }
    /*7.Method
       verilen comm istenen eşik değerin kaç gün boyunca üzerinde olduğunu hesaplama
     */
    public static int daysAboveThreshold(String comm, int threshold) {
        int index = indexOfCommodity(comm);
        if (index == -1) return -1;

        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profits[m][d][index] > threshold) count++; //verilen günde kar eşik değerin üstündeyse count 1 artar
            }
        }
        return count;
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