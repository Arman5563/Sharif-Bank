import java.time.LocalDate;

public abstract class Account {
    Member holder;
    Bank bank;
    long ID;
    long money;
    LocalDate creationDate;
    abstract long close();
    void show(){
        System.out.println("Bank name:"+bank.name);
        System.out.println("Number: "+ID);
        System.out.println("Creation date: "+creationDate.toString());
        System.out.println("Money: "+money);
    }
}
