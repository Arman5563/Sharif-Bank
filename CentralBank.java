import java.util.*;

public class CentralBank {
    static Random r=new Random();
    ArrayList<Bank> bankArrayList=Bank.bankArrayList;
    HashMap<Long,Account> accountMap=new HashMap<>();
    HashMap<Long,Card> cardHashMap=new HashMap<>();
    HashMap < Member,ArrayList<Account> > accountIDHashMap=new HashMap<>();
    HashMap < Member,Loan > loanHashMap=new HashMap<>();
    private static CentralBank c;

    public static CentralBank getInstance(){
        if (c==null)
            c=new CentralBank();
        return c;
    }

    public static long IDGenerator(Bank b){
        long ID=b.ID*10000000000L;
        ID+=(b.accountMap.size()+1)* 10L;//this account has not been added yet
        ID+=r.nextInt(100)*100000000L;
        long temp=ID;
        int cnt=0;
        int num=0;
        while (temp>0){
            if (cnt%2==0)
                num+=temp%10;
            else {
                num+=(temp%10)*2;
                if ((temp%10)*2>10)
                    num-=9;
            }
            temp=temp/10L;
            cnt++;
        }
        ID+=10-num%10;
        return ID;
    }
    public static void CardGenerator(Card c) {
        while (c.cvv2<100)
            c.cvv2=r.nextInt(1000);
        while (c.firstPassword<1000)
            c.firstPassword=r.nextInt(10000);
    }
    public void cardTransact(long senderId, long receiverID, long amount){
        if (!cardHashMap.containsKey(receiverID)) {
            System.out.println("Error: The receiver card number does not exist");
            return;
        }
        boolean b=cardHashMap.get(senderId).currentAccount.withdraw(amount);
        if (b)
            ( cardHashMap.get(receiverID).currentAccount ).add(amount);
        else {
            cardHashMap.get(senderId).currentAccount.add(amount);
            System.out.println("Error: Account does not have enough money");
        }
    }
    public boolean accountTransact(long senderId, long receiverID, long amount){
        if (!accountMap.containsKey(receiverID)){
            System.out.println("Error: The receiver account number does not exist");
            return false;
        }
        if (accountMap.get(receiverID) instanceof DepositAccount){
            System.out.println("Error: The account is a deposit account");
            return false;
        }
        boolean b=((CurrentAccount)accountMap.get(senderId)).withdraw(amount);
        if (b) {
            ((CurrentAccount) accountMap.get(receiverID)).add(amount);
            return true;
        }
        else {
            System.out.println("Error: Account does not have enough money");
            return false;
        }
    }
    public static void getCardMoney(long cardNum, int password){
        CentralBank cb=CentralBank.getInstance();
        if (cb.cardHashMap.containsKey(cardNum)){
            System.out.println(cb.cardHashMap.get(cardNum).getBalance(password));
            return;
        }
        System.out.println("Card was not found");
    }
    public void update(){
        for (Bank bank:bankArrayList){
            bank.update();
        }
    }
    public static void showLoans(){
        CentralBank cb=CentralBank.getInstance();
        for (Loan loan:cb.loanHashMap.values()){
            loan.show();
        }
    }
    public static void showAccounts(){
        CentralBank cb=CentralBank.getInstance();
        for (Account account:cb.accountMap.values())
            account.show();
    }
    public static void showBanks(){
        CentralBank cb=CentralBank.getInstance();
        for (Bank bank: cb.bankArrayList){
            bank.show();
        }
    }

    public static void showCentralBankBalance(){
        CentralBank cb=CentralBank.getInstance();
        long balance=0;
        for (Bank b:cb.bankArrayList){
            balance+=b.money-b.debt;
        }
        System.out.println("Balance is: "+balance);
    }
}
