// Main.java - Students version
import java.io.*;
import java.util.*;
import java.nio.file.Paths;

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
            if (commodities[i].equals(name))
                return i;//bu methotda girdiğimiz commoditie ile karşılaştırma yapar aynı mı diye
        }
        return -1;
    }

    private static boolean validMonth(int m) {
        return m >= 0 && m < MONTHS;
    }

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    //spesifik bir dosyanın içindeki verileri almayı sağlayan kısım.
    public static void loadData() {
        String[] months = {
                "January.txt", "February.txt", "March.txt", "April.txt",
                "May.txt", "June.txt", "July.txt", "August.txt",
                "September.txt", "October.txt", "November.txt", "December.txt"
        };
        for (int m = 0; m < 12; m++) {
            try {
                Scanner sc = new Scanner(new File("Data_Files/" + months[m]));

                // ilk satırı atla
                if (sc.hasNextLine()) {
                    sc.nextLine();
                }

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    int day = Integer.parseInt(parts[0]) - 1;
                    String commodity = parts[1];
                    int profit = Integer.parseInt(parts[2]);

                    int cIndex = -1;

                    if (commodity.equals("Gold")) cIndex = 0;
                    else if (commodity.equals("Oil")) cIndex = 1;
                    else if (commodity.equals("Silver")) cIndex = 2;
                    else if (commodity.equals("Wheat")) cIndex = 3;
                    else if (commodity.equals("Copper")) cIndex = 4;

                    profits[m][day][cIndex] = profit;
                }
                sc.close();
            } catch (Exception e) {
                System.out.println("Could not read file: " + months[m]);
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========
    /* 1.Method
    Bir ay içindeki en değerli comm
     */
    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS)
            return "Invalid month";

        long[] totals = new long[COMMS];

        for (int d = 0; d < DAYS; d++) {
            for (int c = 0; c < COMMS; c++) {
                totals[c] += profits[month][d][c];
            }
        }
        int best = 0;
        for (int c = 1; c < COMMS; c++) {
            if (totals[c] > totals[best])
                best = c;
        }
        return commodities[best];
    }

    /* 2.Method
       Bir günün toplam karı
     */
    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 0 || day >= DAYS) return -1;

        int sum = 0;
        for (int c = 0; c < COMMS; c++)
            sum += profits[month][day][c];

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
        // 0-based month kontrolü
        if (month < 0 || month >= MONTHS) return -1;
        int bestDay = 0;

        long bestProfit = Long.MIN_VALUE;

        for (int d = 0; d < DAYS; d++) {
            long sum = 0;

            for (int c = 0; c < COMMS; c++) {
                sum += profits[month][d][c];
            }

            if (sum > bestProfit) {
                bestProfit = sum;
                bestDay = d;
            }
        }
        return bestDay; // 0-based day index
    }

    /* 5.Method
    Her bir comm için en karlı ay
     */
    public static String bestMonthForCommodity(String comm) {
        int index = indexOfCommodity(comm);
        if (index == -1) return "INVALID";

        long best = Long.MIN_VALUE; // eğer en küçük değere atarsak karşısına çıkan her ilk değer en büyük değer olarak atanır
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

    /* 8.Method
    Bir ay içindeki her gün için en yüksek ve en düşük profits değerlerini hesaplar değer olarak en değişken(max-min) günü returnler
     */
    public static int biggestDailySwing(int month) {

        if (month < 0 || month >= MONTHS) return -1;

        int bestSwing = 0;

        for (int d = 0; d < DAYS; d++) {

            int max = profits[month][d][0];
            int min = profits[month][d][0];

            for (int c = 1; c < COMMS; c++) {
                int value = profits[month][d][c];
                if (value > max) max = value; //verilen ilk değerden büyük yada küçük olmasına göre her seferinde kendini yeni değere atıyo
                if (value < min) min = value;
            }
            int swing = max - min;
            if (swing > bestSwing)
                bestSwing = swing;
        }
        return bestSwing;
}
    /* 9.Method
    2 tane girilen spesifik comm un yıl boyu olan profit miktarını kıyaslar
     */
    public static String compareTwoCommodities(String c1, String c2) {
        int firstComm = indexOfCommodity(c1);
        int secondComm = indexOfCommodity(c2);
        if (firstComm == -1 || secondComm == -1) return "INVALID";

        long sumForFirst = 0, sumForSecond = 0;

        for (int m = 0; m < MONTHS; m++) {//spesifik bir comm için 1yıl boyunca her gün ki profitsini toplar
            for (int d = 0; d < DAYS; d++) {
                sumForFirst += profits[m][d][firstComm];
                sumForSecond += profits[m][d][secondComm];
            }
        }
        if (sumForFirst > sumForSecond) return c1 + " is better by " + (sumForFirst - sumForSecond);
        else return c2 + " is better by " + (sumForSecond - sumForFirst);
    }
    /* 10.Method
    Bir ay içinde profits değeri en yüksek haftayı bulur
     */
    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month > MONTHS) return "INVALID";
        int m = month;

        long best = Long.MIN_VALUE;
        int bestWeek = 1;

        for (int week = 0; week < 4; week++) {
            int sum = 0;
            for (int d = week * 7; d < week * 7 + 7; d++) {//hafta 7 gün,ay 28 gün koşulu
                for (int c = 0; c < COMMS; c++) sum += profits[m][d][c];
            }
            if (sum > best) {
                best = sum;
                bestWeek = week + 1; //array aralığı 0-3 olduğu için + 1 konulur çünkü 0.hafta diye bir şey yok ve ay 4 haftadan oluşur
            }
        }
        return "Week " + bestWeek; //bestWeek değerim kaçıncı hafta olduğunu ifade eder
    }
    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}