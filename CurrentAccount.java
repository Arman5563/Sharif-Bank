import java.util.ArrayList;

public class CurrentAccount extends Account{
    Card card;
    Loan loan;
    ArrayList<DepositAccount> depositAccount;
    long bankDebt;
    CurrentAccount(Member holder, long money, Bank bank){
        creationDate= MyDate.getGlobalDate();
        this.money=money;
        ID=CentralBank.IDGenerator(bank);
        this.holder=holder;
        this.bank=bank;
        card=new Card(this);
        depositAccount=new ArrayList<>();
        bankDebt=0;
    }
    void addLoan(Loan l){
        this.loan=l;
        holder.loan=l;
        this.holder.debt+=loan.amount;
    }

    boolean withdraw(long money) {
        if (this.money<money)
            return false;
        this.money-=money;
        this.holder.money-=money;
        bank.addOrDeduct(-money);
        return true;
    }

    @Override
    long close() {
        long m=money;
        withdraw(money);
        return m;
    }

    void add(long money){
        this.money+=money;
        this.holder.money+=money;
        bank.addOrDeduct(money);
    }

    @Override
    void show() {
        super.show();
        System.out.println("Card ID: "+card.ID);
        System.out.println("Debt of the bank: "+bankDebt);
        if (loan==null)
            System.out.println("No loans");
        else
            System.out.println("Has a loan from "+loan.bank.name);
    }
}
