import java.time.LocalDate;

public class Card {
    static final int MAX_DAILY_WITHDRAWN=2000;
    static final int MAX_DAILY_TRANSACTION=5000;
    long ID;
    int firstPassword;
    int secondPassword;
    int cvv2;
    private int dailyWithdrawn;
    private int dailyTransaction;
    private boolean secondPassActivated;
    CurrentAccount currentAccount;
    Member holder;
    private LocalDate creation;
    private LocalDate expiry;
    private Card(){}
    Card(CurrentAccount currentAccount ){
        CentralBank.CardGenerator(this);
        secondPassActivated=false;
        dailyWithdrawn=0;
        dailyTransaction=0;
        ID=currentAccount.ID;
        holder= currentAccount.holder;
        this.currentAccount=currentAccount;
        creation=MyDate.getGlobalDate();
        expiry=creation.plusYears(4);
    }

    public int getFirstPassword() {
        return firstPassword;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    boolean extend(){
        if (!MyDate.getGlobalDate().isAfter(expiry))
            return false;
        expiry=MyDate.getGlobalDate().plusYears(4);
        return true;
    }
    public static boolean transact(long ID,int firstPassword, long receiverID, long amount){
        CentralBank cb=CentralBank.getInstance();
        if (!cardPrimaryChecks(ID,firstPassword))
            return false;
        Card c=cb.cardHashMap.get(ID);
        boolean b=c.isActive();
        if (!b&&c.currentAccount.holder.isBanned) {
            System.out.println("Error: This person is banned");
            return false;
        }
        if (amount+c.dailyTransaction>=MAX_DAILY_TRANSACTION){
            System.out.println("Error: The amount exceeds the daily maximum amount. Go to the bank to do the transaction");
            return false;
        }
        else{
            c.dailyTransaction+=amount;
            //c.currentAccount.money-=amount;//?
            cb.cardTransact(c.ID,receiverID,amount);
            System.out.println("Transaction was successfully done. CardNumber: "+ID+" amount: "+amount+" receiverCardNumber: "+receiverID);
            return true;
        }
    }
    public static void cardWithdraw(long ID, int firstPassword, int amount){
        if (cardPrimaryChecks(ID, firstPassword)){
            CentralBank.getInstance().cardHashMap.get(ID).withdraw(amount);
        }
    }
    boolean withdraw(int amount){
        boolean b=this.isActive();
        if (!b){
            System.out.println("Error: Card is not active");
            return false;
        }
        if (this.currentAccount.holder.isBanned) {
            System.out.println("Error: This person is banned");
            return false;
        }
        if (amount+dailyWithdrawn>=MAX_DAILY_WITHDRAWN){
            System.out.println("THe amount exceeds the daily maximum amount. Go to the bank to withdraw this amount");
            return false;
        }
        else{
            boolean b1=currentAccount.withdraw(amount);
            if (b1) {
                dailyWithdrawn+=amount;
                System.out.println("Money was successfully withdrawn. Here is the money:" + amount + "$");
            }
            else
                System.out.println("Error: Account does not have enough money");
            return true;
        }
    }
    private boolean isActive(){
        if (MyDate.getGlobalDate().isAfter(expiry)){
            System.out.println("This card has expired and you cannot receive any services. Please extend the expiry date");
            return false;
        }
        return true;
    }

    static boolean changeFirstPassword(long CardNumber, int oldPassword, int newPassword){
        CentralBank cb=CentralBank.getInstance();
        if (cb.cardHashMap.containsKey(CardNumber)){
            if (cb.cardHashMap.get(CardNumber).firstPassword==oldPassword){
                cb.cardHashMap.get(CardNumber).firstPassword=newPassword;
                System.out.println("Card password has changed to "+newPassword);
                return true;
            }
            System.out.println("Error: Password does not match");
        }
        System.out.println("The card does not exist");
        return false;
    }


    public static boolean changeSecondPassword(long CardNumber, int firstPassword, int newPassword) {
        if (cardPrimaryChecks(CardNumber,firstPassword)) {
            if (!CentralBank.getInstance().cardHashMap.get(CardNumber).changeSecondPassword(newPassword)){
                System.out.println("Second password is inactive");
                return false;}
            return true;
        }
        return false;
    }

    public static boolean setSecondPassword(long CardNumber,int firstPassword, int secondPassword) {
        CentralBank cb=CentralBank.getInstance();
        if (cb.cardHashMap.containsKey(CardNumber)) {
            Card c=cb.cardHashMap.get(CardNumber);
            if (c.firstPassword==firstPassword) {
                if (!c.secondPassActivated) {
                    c.secondPassActivated = true;
                    c.secondPassword = secondPassword;
                    System.out.println("Second password was set successfully");
                    return true;
                }
                System.out.println("Second password is already activated");
                return false;
            }
        }
        System.out.println("Card was not found");
        return false;
    }
    public boolean changeSecondPassword(int secondPassword){
        if (secondPassActivated){
            this.secondPassword=secondPassword;
            return true;
        }
        return false;
    }
    public String getBalance(int firstPassword){
        if (this.firstPassword==firstPassword){
            return "The balance is:"+this.currentAccount.money;
        }
        else
            return "Error:Incorrect password";
    }
    public static boolean cardPrimaryChecks(long ID,int firstPassword){
        CentralBank cb=CentralBank.getInstance();
        if (!cb.cardHashMap.containsKey(ID)){
            System.out.println("Card was not found");
            return false;
        }
        Card c=cb.cardHashMap.get(ID);
        if (c.firstPassword!=firstPassword){
            System.out.println("Password did not match");
            return false;
        }
        return true;
    }
}
