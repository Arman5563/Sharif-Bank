import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Loan implements Cloneable{
    Member receiver;
    long amount;
    long paid;
    long loanDebt;
    int interest;
    int penalty;
    Bank bank;
    LocalDate start;
    LocalDate ban;
    LocalDate expiry;
    ArrayList<Long> paymentList;
    ArrayList<LocalDate> paymentDates;
    CurrentAccount currentAccount;
    Loan(Member member, long initialAmount, int interest,Bank bank,CurrentAccount currentAccount){
        receiver=member;
        this.interest=interest;
        this.amount=initialAmount*(100+interest)/100;
        penalty=Bank.LOAN_PENALTY;
        paid=0;
        loanDebt=0;
        this.bank=bank;
        start=MyDate.getGlobalDate();
        expiry=start.plusYears(4);
        paymentDates=new ArrayList<>();
        paymentList=new ArrayList<>();
        this.currentAccount=currentAccount;
    }
    void show(){
        System.out.println("Bank name:"+bank.name);
        System.out.println("Account: "+currentAccount.ID);
        System.out.println("Total amount which has to be paid: "+amount);
        System.out.println("Paid: "+paid);
        System.out.println("Total interest: "+interest);/////
        System.out.println("Date received: "+start.toString());
        System.out.println("Expiry: "+expiry.toString());
        System.out.println("Payments: ");///////
        for (int i=0;i<paymentList.size();i++){
            System.out.println("Payment amount: "+paymentList.get(i));
            System.out.println("Date: "+paymentDates.get(i).toString());
        }
    }
    void check(){
        LocalDate date=MyDate.getGlobalDate();
        if (date.getDayOfMonth()==start.getDayOfMonth()&&!date.isAfter(expiry)) {//includes the expiry date
            long b= ChronoUnit.MONTHS.between(date,expiry)+1; //Period.between(MyDate.getGlobalDate(),expiry).getMonths() is wrong
            long monthly = (amount - paid- loanDebt) / b;
            if (bank.isBankrupt){
                expiry=expiry.plusMonths(1);
                if (currentAccount.bankDebt>monthly){
                    currentAccount.holder.debt-=monthly;
                    bank.debt-=monthly;
                    currentAccount.bankDebt-=monthly;
                    paid+=monthly;
                    paymentList.add(monthly);
                    paymentDates.add(date);
                    enable(amount);
                }
            }
            else if (receiver.isBanned) {
                loanDebt+=monthly;
                if (ChronoUnit.MONTHS.between(ban, date) >= 3) {
                    amount += (amount - paid) * penalty / 100;
                }
            } else {
                if ( (currentAccount.money < monthly) ){
                    receiver.isBanned = true;
                    ban = date;
                    loanDebt+=monthly;
                }
                else {
                    currentAccount.holder.debt-=monthly;
                    currentAccount.add(-monthly);
                    bank.money+=monthly;
                    paid+=monthly;
                    paymentList.add(monthly);
                    paymentDates.add(date);
                    enable(amount);
                }
            }
        }
    }
    void manualPay(long amount){
        if (paid+amount>this.amount){
            System.out.println("Error: The remaining amount is "+(this.amount-paid)+" and you cannot pay more than that.");
        }
        LocalDate date=MyDate.getGlobalDate();
        this.receiver.debt-=amount;
        bank.money+=amount;
        paid+=amount;
        paymentList.add(amount);
        paymentDates.add(date);
        enable(amount);
        System.out.println("Loan was paid off successfully. Date: "+MyDate.getGlobalDate().toString());
    }
    void enable(long amount){
        if ((receiver.isBanned)){
            loanDebt-=amount;
            if (loanDebt<=0) {
                receiver.isBanned = false;
                loanDebt = 0;
            }
        }
    }
}
