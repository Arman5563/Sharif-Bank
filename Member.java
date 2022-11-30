import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
public abstract class Member {
    HashMap <Long, Card> cardSet=new HashMap<>();
    Set <Account> accountList=new HashSet<>();
    Loan loan;
    long debt;
    long money;
    LocalDate startDay;
    String ID;
    boolean isBanned;
    public static HashMap<String,Member> memberHashMap=new HashMap<>();
    boolean addAccount(Account intended){
        this.accountList.add(intended);
        this.money+=intended.money;
        if (intended instanceof CurrentAccount){
            this.cardSet.put(((CurrentAccount) intended).card.ID, ((CurrentAccount) intended).card);;
        }
        return true;
    }
    boolean closeAccount(Account intended){
        this.money-=intended.money;
        if (intended instanceof CurrentAccount){
            this.cardSet.remove(((CurrentAccount) intended).card.ID, ((CurrentAccount) intended).card);
        }
        this.accountList.remove(intended);
        return true;
    }

    void show(){
        System.out.println("Net worth: "+(money-debt));
        System.out.println("Card IDs:");
        for (Card c:this.cardSet.values()){
            System.out.println(c.ID);
        }
        System.out.println("Account IDs:");
        for (Account a:this.accountList){

            if (a instanceof DepositAccount)
                System.out.print("Deposit account ID: ");
            else
                System.out.println("Current account ID: ");
            System.out.println(a.ID);
            System.out.println("Money in the account");
            System.out.println(a.money);
        }
        System.out.println("Loan");
        if (loan!=null)
            loan.show();
        else
            System.out.println("No Loan");
    }
    static boolean payLoan(String bankName,String ID, long money){
        if (memberHashMap.containsKey(ID)){
            if (memberHashMap.get(ID).loan!=null){
                if (!memberHashMap.get(ID).loan.bank.name.equals(bankName)){
                    System.out.println("Bank name does not match");
                    return false;
                }
                memberHashMap.get(ID).loan.manualPay(money);
                return true;
            }
            System.out.println("This person does not have a loan");
            return false;
        }
        System.out.println("This member does not exist");
        return false;
    }
    public void getLoan(Loan loan){this.loan=loan;}
    public static void showLoan(String ID){
        if (!Member.memberHashMap.containsKey(ID)) {
            System.out.println("Member does not exist");
            return;
        }
        if (Member.memberHashMap.get(ID).loan==null){
            System.out.println("Member does not have a loan");
            return;
        }
        Member.memberHashMap.get(ID).loan.show();
    }
    public static void showAccounts(String ID){
        if (!Member.memberHashMap.containsKey(ID)) {
            System.out.println("Member does not exist");
            return;
        }
        for (Account account:Member.memberHashMap.get(ID).accountList)
            account.show();
    }

}
