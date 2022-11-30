import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DepositAccount extends Account{
    CurrentAccount currentAccount;
    boolean type;//false for short-term
    LocalDate expiry;
    LocalDate lastPayment;
    int interest;
    DepositAccount(Member holder,String duration, long money, Bank bank, CurrentAccount currentAccount){
        this.ID=CentralBank.IDGenerator(currentAccount.bank);
        this.money=money;
        this.bank=bank;
        this.holder=holder;
        this.currentAccount=currentAccount;
        this.creationDate=MyDate.getGlobalDate();
        this.lastPayment=creationDate;
        if (duration.equals("long-term")){
            expiry=creationDate.plusYears(1);
            type=true;
            this.interest=bank.longTimeProfit;
        }
        else {
            expiry = creationDate.plusMonths(6);
            type = false;
            this.interest=bank.shortTimeProfit;
        }
    }

    long close() {
        long m;
        if (!MyDate.getGlobalDate().isAfter(expiry)){//includes the expiry date
            double a=(ChronoUnit.MONTHS.between(MyDate.getGlobalDate(),expiry)*0.5)/100.0*money;
            m=money-(long) a;
            bank.money-=m;
            bank.debt-=money;
            money=0;
            return m;
        }
        m=money;
        bank.addOrDeduct(-money);
        this.holder.money-=money;
        currentAccount.add(money);
        currentAccount.depositAccount.remove(this);
        money=0;
        return m;
    }
    long getPay(){
        return money*interest/100L;
    }

    @Override
    void show() {
        super.show();
        if (type)
            System.out.println("Type is long-term");
        else
            System.out.println("Type is short-term");
        System.out.println("Interest: "+interest);
        System.out.println("Current account ID: "+currentAccount.ID);
        System.out.println("Expiry date: "+expiry.toString());
    }
}
