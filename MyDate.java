import java.time.LocalDate;

public class MyDate {
    private static LocalDate globalDate;
    public static LocalDate getGlobalDate(){
        if (globalDate==null)
            globalDate=LocalDate.now();
        return globalDate;
    }
    public static void goDay(){
        globalDate=globalDate.plusDays(1);
        CentralBank.getInstance().update();
    }
    public static void goMonth(){
        LocalDate now=LocalDate.parse(globalDate.toString());
        while (globalDate.isBefore(now.plusMonths(1))){
            goDay();
        }
    }
    public static void goYear(){
        LocalDate now=LocalDate.parse(globalDate.toString());
        while (globalDate.isBefore(now.plusYears(1))){
            goDay();
        }
    }
    public static void goDays(int n){
        for (int i=0;i<n;i++)
            goDay();
        System.out.println(n+" days have passed");
    }
    public static void goMonths(int n){
        for (int i=0;i<n;i++)
            goMonth();
        System.out.println(n+" months have passed");
    }
    public static void goYears(int n){
        for (int i=0;i<n;i++)
            goYear();
        System.out.println(n+" years have passed");
    }
    public static void goToDate(LocalDate d){
        while (getGlobalDate().isBefore(d)){
            goDay();
        }
        System.out.println("Date has changed to "+d.toString());
    }
}
