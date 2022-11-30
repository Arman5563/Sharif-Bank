import java.time.LocalDate;
import java.util.*;

public class Bank {
    int ID;
    long money;
    long debt;
    String name;
    int investmentProfit;
    int shortTimeProfit;
    int longTimeProfit;
    boolean isBankrupt;
    static ArrayList<Bank> bankArrayList=new ArrayList<>();
    HashMap <Long, Account> accountMap=new HashMap<>();
    HashMap < Member,ArrayList<Account> > memberArrayListHashMap=new HashMap<>();//AccountArraylist
    static HashMap <Member,Loan> loanHashMap=new HashMap<>();
    LocalDate investmentInterestDate;
    static final int MINIMUM_AGE=16;
    static final int MINIMUM_AGE_LOAN=18;
    static final int SHORT_TIME_MINIMUM_MONEY=5000;
    static final int LONG_TIME_MINIMUM_MONEY=10000;
    static final int LOAN_TO_WORTH_RATIO=5;
    static final int LOAN_INTEREST_PERCENTAGE=20;
    static final int LOAN_PENALTY=2;
    private Bank(BankList b){
        money=3000000000L;
        this.name=b.name;
        this.ID=b.ID;
        investmentProfit=20;
        shortTimeProfit=10;
        longTimeProfit=15;
        bankArrayList.add(this);
        isBankrupt=false;
        debt=0;
        investmentInterestDate=MyDate.getGlobalDate().plusMonths(1);
    }
    private Bank(BankList b, long initialBalance){
        money=initialBalance;
        this.name=b.name;
        this.ID=b.ID;
        investmentProfit=20;
        shortTimeProfit=10;
        longTimeProfit=15;
        bankArrayList.add(this);
        isBankrupt=false;
        debt=0;
        investmentInterestDate=MyDate.getGlobalDate().plusMonths(1);
    }
    public static boolean newBank(String name){//
        boolean success=false;
        boolean newb=true;
        for (Bank h:bankArrayList){
            if (h.name.equals(name))
                newb=false;
        }
        if (newb) {
            BankList[] g = BankList.values();
            for (int i = 0; i < g.length && !success; i++) {
                if (g[i].name.equals(name)) {
                    success = true;
                    Bank n = new Bank(g[i]);
                }
            }
        }
        return success;
    }
    public static boolean newBank(String name, long initialBalance){
        boolean success=false;
        boolean newb=true;
        for (Bank h:bankArrayList){
            if (h.name.equals(name))
                newb=false;
        }
        if (newb) {
            BankList[] g = BankList.values();
            for (int i = 0; i < g.length && !success; i++) {
                if (g[i].name.equals(name)) {
                    success = true;
                    Bank n = new Bank(g[i],initialBalance);
                }
            }
        }
        return success;
    }

    public static boolean setInvestmentProfit(String bankName,int investmentProfit) {
        int a=ifExists(bankName);
        if (a != -1) {
            bankArrayList.get(a).investmentProfit = investmentProfit;
            return true;
        }
        else
            return false;
    }

    public static boolean setShortTimeProfit(String bankName,int shortTimeProfit) {
        int a=ifExists(bankName);
        if (a != -1) {
            bankArrayList.get(a).shortTimeProfit = shortTimeProfit;
            return true;
        }
        else
            return false;
    }
    public static boolean setLongTimeProfit(String bankName,int longTimeProfit) {
        int a=ifExists(bankName);
        if (a != -1) {
            bankArrayList.get(a).longTimeProfit = longTimeProfit;
            return true;
        }
        else
            return false;
    }

    public static boolean setMoney(String bankName,long money) {
        int a=ifExists(bankName);
        if (a != -1) {
            Bank b=bankArrayList.get(a);
            b.money+=money;
            if ((b.money-b.debt>0)&&(b.isBankrupt)){
                b.isBankrupt=false;
                System.out.println("Bank is reopening ...");
                b.reopen();
            }
            return true;
        }
        else
            return false;
    }
    public static void showBankInterest(String bankName){
        int i=ifExists(bankName);
        if (i!=-1){
            System.out.println("Interest for short-term deposits:"+bankArrayList.get(i).shortTimeProfit);
            System.out.println("Interest for long-term deposits:"+bankArrayList.get(i).longTimeProfit);
            return;
        }
        System.out.println("Bank was not found");
    }
    public static void showBankBalance(String bankName){
        int i=ifExists(bankName);
        if (i!=-1){
            System.out.println("Balance is: "+(bankArrayList.get(i).money-bankArrayList.get(i).debt) );
            return;
        }
        System.out.println("Bank was not found");
    }
    private static String primaryChecks(String bankName,String ID){
        int i = ifExists(bankName);
        if (i < 0)
            return "Error: The specified bank does not exist.";
        if (!Member.memberHashMap.containsKey(ID)) {
            if (ID.matches(Person.personIDRegex))
                return "Error: This person does not Exist";
            else
                return "Error: This company does not Exist";
        }
        Bank b=bankArrayList.get(i);
        if (b.isBankrupt){
            return "Error: This bank is bankrupt and cannot offer services";
        }
        if (Member.memberHashMap.get(ID).isBanned){
            return "You haven't paid a loan and are thus banned from any action";
        }
        return "OK";
    }

    public static String createCurrentAccount(String bankName,String ID, long money){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK"))
            return s;
        Member m=Member.memberHashMap.get(ID);
        if ((m instanceof Person)&&(MyDate.getGlobalDate().minusYears(MINIMUM_AGE).isBefore(m.startDay))){
            return "Error: This person is underage";
        }
        Bank b=bankArrayList.get(ifExists(bankName));
        CurrentAccount c=new CurrentAccount(m,money,b);
        CentralBank cb=CentralBank.getInstance();
        b.accountMap.put(c.ID,c);
        if (!b.memberArrayListHashMap.containsKey(m)) {
            b.memberArrayListHashMap.put(m, new ArrayList<>());
            cb.accountIDHashMap.put(m, new ArrayList<>());
        }
        b.memberArrayListHashMap.get(m).add(c);
        cb.accountIDHashMap.get(m).add(c);
        cb.accountMap.put(c.ID,c);
        cb.cardHashMap.put(c.card.ID,c.card);
        m.addAccount(c);
        b.addOrDeduct(money);
        return "Current account was created successfully: Your account number is:"+c.ID+"\n"+"Card password and cvv2: "+c.card.firstPassword+" "+c.card.cvv2;
    }
    public static String createDepositAccount(String bankName,String ID, String duration, long money){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK"))
            return s;
        Bank b=bankArrayList.get(ifExists(bankName));
        Member m=Member.memberHashMap.get(ID);
        CentralBank cb=CentralBank.getInstance();
        if ((m instanceof Person)&&(MyDate.getGlobalDate().minusYears(MINIMUM_AGE).isBefore(m.startDay))){
            return "Error: This person is underage";
        }
        if ( (duration.equals("long-term")&&(money<LONG_TIME_MINIMUM_MONEY))||( duration.equals("short-term")&&(money<SHORT_TIME_MINIMUM_MONEY)) )
            return "Error: Your proposed amount must not be lower the the minimums for opening deposit accounts";
        DepositAccount da;
        if (!b.memberArrayListHashMap.containsKey(m)) {
            System.out.println(Bank.createCurrentAccount(bankName, ID, 0));
            CurrentAccount ca=(CurrentAccount) b.memberArrayListHashMap.get(m).get(0);
            da=new DepositAccount(m,duration,money,b, ca);
            b.memberArrayListHashMap.get(m).add(da);
            b.accountMap.put(da.ID,da);
            m.addAccount(da);
            cb.accountMap.put(da.ID,da);
            cb.accountIDHashMap.get(m).add(da);
            b.addOrDeduct(money);
            return "Deposit account was successfully created. Account number:"+da.ID+" Linked current account ID"+ca.ID;
        }
        else {
            for (Account account:b.memberArrayListHashMap.get(m)){
                if (account instanceof CurrentAccount){
                    da = new DepositAccount(m,duration, money, b, (CurrentAccount)account);
                    b.memberArrayListHashMap.get(m).add(da);
                    b.accountMap.put(da.ID,da);
                    m.addAccount(da);
                    cb.accountMap.put(da.ID,da);
                    cb.accountIDHashMap.get(m).add(da);
                    b.addOrDeduct(money);
                    return "Deposit account was successfully created. Account number:"+da.ID+" Linked current account ID "+account.ID;
                }
            }
        }
        return "impossible";//dummy
    }
    public static void depositMoney(String bankName,long ID, long money){
        int i=ifExists(bankName);
        if (i<0){
            System.out.println("Error: The specified bank does not exist.");
            return;
        }
        Bank b=CentralBank.getInstance().bankArrayList.get(i);
        if (b.accountMap.containsKey(ID)){
            if (b.accountMap.get(ID) instanceof CurrentAccount){
                b.accountMap.get(ID).money+=money;
                b.addOrDeduct(money);
                System.out.println("Money was deposited successfully");
                return;
            }
        }
        System.out.println("The specified bank does not have a current account with the entered ID");
    }
    public static void withdraw(String bankName,long accountNum,String ID,long money){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK")) {
            System.out.println(s);
            return;
        }
        Bank b=bankArrayList.get(ifExists(bankName));
        Member m=Member.memberHashMap.get(ID);
        if (b.accountMap.containsKey(accountNum)){
            if (b.accountMap.get(accountNum).holder==m&& b.accountMap.get(accountNum) instanceof CurrentAccount){
                boolean b1=((CurrentAccount)b.accountMap.get(accountNum)).withdraw(money);
                if (b1) {
                    System.out.println("Money was successfully withdrawn. Here is the money:" + money + "$");
                }
                else
                    System.out.println("Error: Account does not have enough money");
                return;
            }
        }
        System.out.println("This person does not have a current account with the specified number in this bank");
    }
    public static void accountTransact(String bankName, long senderNum, String ID, long receiverNum, long amount){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK")) {
            System.out.println(s);
            return;
        }
        Bank b=bankArrayList.get(ifExists(bankName));
        Member m=Member.memberHashMap.get(ID);
        if (b.accountMap.containsKey(senderNum)){
            if (b.accountMap.get(senderNum).holder==m&& b.accountMap.get(senderNum) instanceof CurrentAccount){
                if (CentralBank.getInstance().accountTransact(senderNum,receiverNum,amount))
                    System.out.println("Transaction was successfully done. Account number: "+senderNum+" amount: "+amount+" receiver account number: "+receiverNum);
                return;
            }
        }
        System.out.println("This person does not have a current account with the specified number in this bank");
    }
    public static boolean cardExtend(String bankName,long cardNum, String ID){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK")) {
            System.out.println(s);
            return false;
        }
        CentralBank cb=CentralBank.getInstance();
        if (cb.cardHashMap.containsKey(cardNum)) {
            Card c = cb.cardHashMap.get(cardNum);
            if (c.holder == Member.memberHashMap.get(ID) && c.currentAccount.bank.name.equals(bankName)) {
                c.extend();
                System.out.println("Card expiry date was extended successfully. New expiry date: "+c.getExpiry().toString());
                return true;
            } else {
                System.out.println("Error: The owner or the bank are not related to the card");
                return false;
            }
        }
        System.out.println("Card was not found");
        return false;
    }
    public static String removeAccount(String bankName, String ID, long accountNum) {
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK"))
            return s;
        Bank b=bankArrayList.get(ifExists(bankName));
        Member member=Member.memberHashMap.get(ID);
        if (b.memberArrayListHashMap.containsKey(member)) {
            ArrayList<Account> a = b.memberArrayListHashMap.get(member);//alternative way: like withdraw
            for (Account account:a){
                if (account.ID==accountNum){
                    if (account instanceof CurrentAccount){
                        if ( ((CurrentAccount) account).loan!=null)
                            return "Error: Cannot close an account with a loan";
                        if (! ((CurrentAccount) account).depositAccount.isEmpty())
                            return "Error: Cannot close an account related to deposit accounts";
                    }
                    long t=account.close();
                    CentralBank cb=CentralBank.getInstance();
                    cb.accountIDHashMap.get(member).remove(account);
                    a.remove(account);//b.memberArrayListHashMap.get(member).remove(account)
                    member.closeAccount(account);
                    if (account instanceof CurrentAccount)
                        cb.cardHashMap.remove(((CurrentAccount) account).card.ID);
                    //? should we delete an empty member?
                    if (cb.accountIDHashMap.get(member).isEmpty()){
                        cb.accountIDHashMap.remove(member);
                        b.memberArrayListHashMap.remove(member);
                        ///loan deleting
                    }
                    /////must be checked
                    return "Account was closed successfully. Here is the money in the account: "+t;
                }
            }
        }
        return "Error: This person does not have an account with the specified ID.";
    }
    public static String getNewLoan(String bankName,String ID,long money){
        String s=primaryChecks(bankName,ID);
        if (!s.equals("OK"))
            return s;
        Bank b=bankArrayList.get(ifExists(bankName));
        Member m=Member.memberHashMap.get(ID);
        if ((m instanceof Person)&&(MyDate.getGlobalDate().minusYears(MINIMUM_AGE_LOAN).isBefore(m.startDay))){
            return "Error: This person is underage for getting a loan";
        }
        CentralBank cb=CentralBank.getInstance();
        if (cb.loanHashMap.containsKey(m))
            return "Error:There is already a loan for this ID in the banking system";
        boolean bool=false;
        long a=0;
        for (Account account:b.memberArrayListHashMap.get(m)){
            a+=account.money;
            if (account instanceof CurrentAccount){
                bool=true;
            }
        }
        if (!bool){
            return "Error: You do not have an account in the specified bank";
        }
        if (a*LOAN_TO_WORTH_RATIO<money){
            return "Error: Your requested amount is more than "+LOAN_TO_WORTH_RATIO+" times your total amount of money in the bank";
        }
        if (b.money- b.debt-money<0)///////////////////
            return "The bank will suffer from an imbalance. So, it cannot give you the loan";
        for (Account account:b.memberArrayListHashMap.get(m)){
            if (account instanceof CurrentAccount){
                Loan l=new Loan(m,money,LOAN_INTEREST_PERCENTAGE,b,(CurrentAccount) account);
                loanHashMap.put(m,l);
                cb.loanHashMap.put(m,l);
                ((CurrentAccount) account).add(money);
                ((CurrentAccount) account).addLoan(l);
                b.money-=money;
                return "Your have received your loan successfully";
            }
        }
        return "impossible";//dummy
    }




    private static int ifExists(String bankName){
        for (int i=0;i<bankArrayList.size();i++){
            if (bankArrayList.get(i).name.equals(bankName))
                return i;
        }
        return -1;
    }
    public void show(){
        System.out.println("ID:"+ID);
        System.out.println("Net balance is:"+(money-debt));
        System.out.println("Interest rate for short-term accounts:"+shortTimeProfit);
        System.out.println("Interest rate for long-term accounts:"+longTimeProfit);
        System.out.println("Monthly investment interest rate:"+investmentProfit);
        System.out.println("Bankruptcy state: "+isBankrupt);
        System.out.println("Total debt:"+debt);



    }
    private boolean deduct(long money){
        if (!isBankrupt) {
            if (this.money-money>this.debt)
                this.money -= money;
            else {
                System.out.println("Bank "+this.name+" has become bankrupt on "+MyDate.getGlobalDate().toString());
                this.isBankrupt=true;
            }
        }
        else{
            this.debt+=money;
        }
        return !isBankrupt;
    }
    void addOrDeduct(long money){
        this.money+=money;
        this.debt+=money;
    }

    public void update(){
        LocalDate date=MyDate.getGlobalDate();
        if (date.getDayOfMonth()==investmentInterestDate.getDayOfMonth()&&!isBankrupt){
            money+=investmentProfit*money/100L;
        }
        ArrayList<Loan> toBeRemoved=new ArrayList<>();
        for (Loan loan:loanHashMap.values()){
            loan.check();
            if (loan.paid>=loan.amount)
                toBeRemoved.add(loan);
        }
        for (Loan loan:toBeRemoved){
            this.removeLoan(loan);
        }
        ArrayList <Account> toBeRemoved2=new ArrayList<>();
        for (Account account:accountMap.values()){
            if (account instanceof DepositAccount){
                if (((DepositAccount)account).expiry.isBefore(MyDate.getGlobalDate()) )
                    toBeRemoved2.add(account);
            }
        }
        for (Account account:toBeRemoved2){
            System.out.println("Deposit account with number "+account.ID+" has expired and is being closed automatically");
            long t=account.close();
            CentralBank cb=CentralBank.getInstance();
            cb.accountIDHashMap.get(account.holder).remove(account);
            this.memberArrayListHashMap.get(account.holder).remove(account);
            account.holder.closeAccount(account);
            this.accountMap.remove(account.ID);
            cb.accountMap.remove(account.ID);
            if (cb.accountIDHashMap.get(account.holder).isEmpty()){
                cb.accountIDHashMap.remove(account.holder);
                this.memberArrayListHashMap.remove(account.holder);
            }
        }
        for (Account account:accountMap.values()){
            if (account instanceof DepositAccount){
                DepositAccount da=((DepositAccount)account);
                if ( da.lastPayment.getDayOfMonth()==date.getDayOfMonth() ){
                    if (this.deduct( da.getPay() )){
                        da.currentAccount.add(da.getPay());
                    }
                    else
                        da.currentAccount.bankDebt+=da.getPay();
                }
            }
        }
    }
    public void reopen(){
        for (Account account:accountMap.values()){
            if (account instanceof CurrentAccount){
                if ( ((CurrentAccount)account).bankDebt>0 ){
                    //this.debt-=((CurrentAccount)account).bankDebt;
                    //this.money-=((CurrentAccount)account).bankDebt;// money in the account is part of a bank's money
                    account.money+=((CurrentAccount)account).bankDebt;
                    ((CurrentAccount)account).bankDebt=0;
                }
            }
        }
    }
    private void removeLoan(Loan loan){
        loanHashMap.remove(loan.receiver);
        CentralBank.getInstance().loanHashMap.remove(loan.receiver);
        loan=null;
    }

    /*
    public static String removeCurrentAccountC(String bankName, String ID, long accountNum){
        int i=ifExists(bankName);
        if (i<0)
            return "Error: The specified bank does not exist.";
        if (!Company.memberHashMap.containsKey(ID))
            return "Error: This person does not Exist";
        Company cp=(Company) Company.memberHashMap.get(ID);
        boolean b=false;
        if (bankArrayList.get(i).memberArrayListHashMap.containsKey(cp)){
            ArrayList <Account> a=bankArrayList.get(i).memberArrayListHashMap.get(cp);
            for (Account account:a){
                if ((account.ID==accountNum)&&(account instanceof CurrentAccount)){
                    b=true;
                    if ( ((CurrentAccount) account).loan!=null)
                        return "Error: Cannot close an account with a loan";
                    if ( ((CurrentAccount) account).depositAccount!=null){
                        return "Error: Cannot close an account related to deposit accounts";
                    }
                    account.withdraw( account.money );
                    /////must be checked
                    return "Account was closed successfully. Here is the money in the account: "
                }
            }
        }
        if (!b)
            return "Error: Person does not have a current account in the bank";
        return "";//dummy
    }
    */
    /*
    public static String createCurrentAccount(String bankName, long nationalCode, int money){
        int i=ifExists(bankName);
        if (i<0)
            return "Error: The specified bank does not exist.";
        if (!Person.personHashMap.containsKey(nationalCode))
            return "Error: This person does not Exist";
        Person p=Person.personHashMap.get(nationalCode);
        CurrentAccount c=new CurrentAccount(p,money,bankArrayList.get(i));
        bankArrayList.get(i).accountMap.put(c.ID,c);
        bankArrayList.get(i).memberArrayListHashMap.get(p).add(c);
        CentralBank cb=CentralBank.getInstance();
        cb.accountMap.put(c.ID,c);
        cb.accountIDHashMap.get(p).add(c);
        return "Account was created successfully: Your account number is:"+c.ID+"\n"+"Card password and cvv2: "+c.card.firstPassword+" "+c.card.cvv2;
    }
    */
       /*
    public static String removeCurrentAccountP(String bankName, long nationalCode, long accountNum){
        int i=ifExists(bankName);
        if (i<0)
            return "Error: The specified bank does not exist.";
        if (!Person.personHashMap.containsKey(nationalCode))
            return "Error: This person does not Exist";
        Person p=Person.personHashMap.get(nationalCode);
        boolean b=false;
        if (bankArrayList.get(i).memberArrayListHashMap.containsKey(p)){
            ArrayList <Account> a=bankArrayList.get(i).memberArrayListHashMap.get(p);
            for (Account account:a){
                if ((account.ID==accountNum)&&(account instanceof CurrentAccount)){
                    b=true;
                    if ( ((CurrentAccount) account).loan!=null)
                        return "Error: Cannot close an account with a loan";
                    if ( ((CurrentAccount) account).depositAccount!=null){
                        return "Error: Cannot close an account related to deposit accounts";
                    }

                    account.withdraw( account.money );
                    /////must be checked
                    return "Account was closed successfully. Here is the money in the account: "
                }
            }
        }
        if (!b)
            return "Error: Person does not have a current account in the bank";
        return "";//dummy
    }
    */
    /*
    public static String removeCurrentAccount(String bankName, String ID, long accountNum){
        int i=ifExists(bankName);
        if (i<0)
            return "Error: The specified bank does not exist.";
        if (!Member.memberHashMap.containsKey(ID)){
            if (ID.matches(Person.personIDRegex))
                return "Error: This person does not Exist";
            else
                return "Error: This company does not Exist";
        }
        Member member=Member.memberHashMap.get(ID);
        boolean b=false;
        if (bankArrayList.get(i).memberArrayListHashMap.containsKey(member)){
            ArrayList <Account> a=bankArrayList.get(i).memberArrayListHashMap.get(member);
            for (Account account:a){
                if (account.ID==accountNum){
                    if (account instanceof CurrentAccount){
                        if ( ((CurrentAccount) account).loan!=null)
                            return "Error: Cannot close an account with a loan";
                        if ( ((CurrentAccount) account).depositAccount!=null){
                            return "Error: Cannot close an account related to deposit accounts";
                        }
                    }
                    b=true;

                    String t=String.valueOf(account.money);
                    account.close();
                    a.remove(account);
                    CentralBank.getInstance().accountIDHashMap.get(member).remove(account);
                    if (account instanceof CurrentAccount)
                        CentralBank.getInstance().cardHashMap.remove(((CurrentAccount) account).card.ID);
                    /////must be checked
                    return "Account was closed successfully. Here is the money in the account: "+t;
                }
            }
        }
        if (!b)
            return "Error: Person does not have a current account in the bank";
        return "";//dummy
    }
     */
}
