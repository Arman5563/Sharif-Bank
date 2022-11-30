import java.time.LocalDate;
import java.util.Scanner;
public class InputProcessor {
    Scanner scanner=new Scanner(System.in);

    String AddPerson="Add person [A-Z][a-z]* [a-zA-Z]+ \\d{10} \\d{4}/\\d{2}/\\d{2}";
    String AddCompany="Add company [A-Z][a-z]* \\d{10}";
    String AddBank="Add bank [A-Z]+";
    String AddBankWithBalance="Add bank [A-Z]+ \\d+";
    String SetBankIncomePercent="Set bank income percent [A-Z]+ \\d+";
    String SetBankDepositInterest="Set bank interest percent [A-Z]+ \\d+";
    String IncreaseBankBalance="increase bank balance [A-Z]+ \\d+";
    String OpenCurrentAccountP="Open current account [A-Z]+ \\d{10} \\d+";
    String OpenCurrentAccountC="Open current account [A-Z]+ [\\w_]+ \\d+";
    String OpenDepositAccountP="Open deposit account [A-Z]+ \\d{10} (long|short)-term \\d+";
    String OpenDepositAccountC="Open deposit account [A-Z]+ [\\w_]+ (long|short)-term \\d+";
    String CloseAccountP="Close account [A-Z]+ \\d{10} \\d{16}";
    String CloseAccountC="Close account [A-Z]+ [\\w_]+ \\d{16}";
    String ChangeCardPassword="Change card password \\d{16} \\d{4} \\d{4}";
    String SetCardSecondPassword="Set card second password \\d{16} \\d{4} \\d{6}";
    String ChangeCardSecondPassword="Change card second password \\d{16} \\d{4} \\d{6}";
    String ExtendExpiration="Extend the expiration date [A-Z]+ \\d{16} \\d{10}";
    String DepositMoney="Deposit money [A-Z]+ \\d{16} \\d+";
    String WithdrawFromCard="Withdraw money \\d{16} \\d{4} \\d+";
    String WithdrawFromAccountP="Withdraw money [A-Z]+ \\d{16} \\d{10} \\d+";
    String WithdrawFromAccountC="Withdraw money [A-Z]+ \\d{16} [\\w_]+ \\d+";
    String GetAccountBalance="Get account balance \\d{16} \\d{4}";
    String TransferCard="Transfer money to another account \\d{16} \\d{4} \\d{16} \\d+";
    String TransferAccountP="Transfer money to another account [A-Z]+ \\d{16} \\d{10} \\d{16} \\d+";
    String TransferAccountC="Transfer money to another account [A-Z]+ \\d{16} [\\w_]+ \\d{16} \\d+";
    String ReceiveLoanP="Receive loan [A-Z]+ \\d{10} \\d+";
    String ReceiveLoanC="Receive loan [A-Z]+ [\\w_]+ \\d+";
    //whatToDo?
    String PayLoanP="Pay off the loan [A-Z]+ \\d{10} \\d+";
    String PayLoanC="Pay off the loan [A-Z]+ [\\w_]+ \\d+";
    String GoNextDay="Go next day";
    String GoNextMonth="Go next month";
    String GoNextYear="Go next year";
    String GoDays="Go for \\d+ days";
    String GoMonth="Go for \\d+ months";
    String GoYear="Go for \\d+ years";
    String GoToDate="Go to date \\d{2}/\\d{2}/\\d{4}";
    String ShowBanks="Show all banks";
    String ShowPersons="Show all persons";
    String ShowCompanies="Show all companies";
    String ShowAccounts="Show all accounts";
    String ShowLoans="Show all loans";
    String ShowPersonAccounts="Show accounts for \\d{10}";
    String ShowCompanyAccounts="Show accounts for [\\w_]+";
    String ShowLoanDetailsPerson="Show details of the loan for \\d{10}";
    String ShowLoanDetailsCompany="Show details of the loan for [\\w_]+";
    String ShowBankInterest="Show bank interest [A-Z]+";
    String ShowBankBalance="Show bank balance [A-Z]+";
    String ShowCentralBankBalance="Show central bank balance";
    String ShowDate="Show date";
    InputProcessor(){}
    public boolean run(){
        String input=scanner.nextLine();
        if (input.equals("exit"))
            return true;
        String[] a=input.split(" ");
        if (input.matches(AddPerson)){
            a[5]=a[5].replaceAll("/","-");
            if ( Person.createPerson( a[2],a[3],a[4],a[5]) )
                System.out.println("Person was created successfully");
            else
                System.out.println("Error: Person already exists");

        }
        else if (input.matches(AddCompany)){
            String temp=Company.createCompany(a[2], a[3]);
            System.out.println(temp);

        }
        else if (input.matches(AddBank)){
            if (Bank.newBank(a[2]))
                System.out.println("Bank "+a[2]+" was created successfully");
            else
                System.out.println("Bank name is invalid or it already exists");
        }
        else if (input.matches(AddBankWithBalance)){
            long temp=Long.parseLong(a[3]);
            if (Bank.newBank(a[2],temp))
                System.out.println("Bank "+a[2]+" was created successfully");
            else
                System.out.println("Bank name is invalid or it already exists");
        }
        else if (input.matches(SetBankIncomePercent)){
            if(Bank.setInvestmentProfit(a[4],Integer.parseInt(a[5])))
                System.out.println("Investment profit changed successfully");
            else
                System.out.println("Bank name is invalid");
        }
        else if (input.matches(SetBankDepositInterest)){
            if(Bank.setShortTimeProfit(a[4],Integer.parseInt(a[5])))
                System.out.println("Short-term deposit account profit changed successfully");
            else
                System.out.println("Bank name is invalid");
        }
        else if (input.matches(IncreaseBankBalance)){
            if (Bank.setMoney(a[3],Long.parseLong(a[4])))
                System.out.println("Bank balance was changed successfully");
            else
                System.out.println("Bank name is invalid");
        }
        else if (input.matches(OpenCurrentAccountP)){
            System.out.println( Bank.createCurrentAccount(a[3],a[4],Integer.parseInt(a[5])) );
        }
        else if (input.matches(OpenCurrentAccountC)){
            System.out.println( Bank.createCurrentAccount(a[3],a[4],Integer.parseInt(a[5])) );
        }
        else if (input.matches(OpenDepositAccountP)||input.matches(OpenDepositAccountC)){
            System.out.println( Bank.createDepositAccount(a[3],a[4],a[5],Long.parseLong(a[6])) );
        }
        else if (input.matches(CloseAccountP)||input.matches(CloseAccountC)){
            System.out.println( Bank.removeAccount(a[2],a[3],Long.parseLong(a[4])) );
        }
        else if (input.matches(ChangeCardPassword)){
            Card.changeFirstPassword(Long.parseLong(a[3]),Integer.parseInt(a[4]),Integer.parseInt(a[5]));
        }
        else if (input.matches(SetCardSecondPassword)){
            Card.setSecondPassword(Long.parseLong(a[4]),Integer.parseInt(a[5]),Integer.parseInt(a[6]));
        }
        else if (input.matches(ChangeCardSecondPassword)){
            Card.changeSecondPassword(Long.parseLong(a[4]),Integer.parseInt(a[5]),Integer.parseInt(a[6]));
        }
        else if (input.matches(ExtendExpiration)){
            Bank.cardExtend(a[4],Long.parseLong(a[5]),a[6]);
        }
        else if (input.matches(DepositMoney)){
            Bank.depositMoney(a[2],Long.parseLong(a[3]),Long.parseLong(a[4]));
        }
        else if (input.matches(WithdrawFromCard)){
            Card.cardWithdraw(Long.parseLong(a[2]),Integer.parseInt(a[3]),Integer.parseInt(a[4]));
        }
        else if (input.matches(WithdrawFromAccountP)||input.matches(WithdrawFromAccountC)){
            Bank.withdraw(a[2],Long.parseLong(a[3]),a[4],Long.parseLong(a[5]));
        }
        else if (input.matches(GetAccountBalance)){
            CentralBank.getCardMoney(Long.parseLong(a[3]),Integer.parseInt(a[4]));
        }
        else if (input.matches(TransferCard)){
            Card.transact(Long.parseLong(a[5]),Integer.parseInt(a[6]),Long.parseLong(a[7]),Long.parseLong(a[8]));
        }
        else if (input.matches(TransferAccountP)||input.matches(TransferAccountC)){
            Bank.accountTransact(a[5],Long.parseLong(a[6]),a[7],Long.parseLong(a[8]),Long.parseLong(a[9]));
        }
        else if (input.matches(ReceiveLoanP)||input.matches(ReceiveLoanC)){
            System.out.println(Bank.getNewLoan(a[2],a[3],Long.parseLong(a[4])));
        }
        else if (input.matches(PayLoanP)||input.matches(PayLoanC)){
            Member.payLoan(a[4],a[5],Long.parseLong(a[6]));
        }
        else if (input.matches(GoNextDay)){
            MyDate.goDay();
        }
        else if (input.matches(GoNextMonth)){
            MyDate.goMonth();
        }
        else if (input.matches(GoNextYear)){
            MyDate.goYear();
        }
        else if (input.matches(GoDays)){
            MyDate.goDays(Integer.parseInt(a[2]));
        }
        else if (input.matches(GoMonth)){
            MyDate.goMonths(Integer.parseInt(a[2]));
        }
        else if (input.matches(GoYear)){
            MyDate.goYears(Integer.parseInt(a[2]));
        }
        else if (input.matches(GoToDate)){
            a[3]=a[3].replaceAll("/","-");
            MyDate.goToDate(LocalDate.parse(a[3]));
        }
        else if (input.matches(ShowBanks)){
            CentralBank.showBanks();
        }
        else if (input.matches(ShowPersons)){
            Person.showPersons();
        }
        else if (input.matches(ShowCompanies)){
            Company.showCompanies();
        }
        else if (input.matches(ShowAccounts)){
            CentralBank.showAccounts();
        }
        else if (input.matches(ShowLoans)){
            CentralBank.showLoans();
        }
        else if (input.matches(ShowPersonAccounts)||input.matches(ShowCompanyAccounts)){
            Member.showAccounts(a[3]);
        }
        else if (input.matches(ShowLoanDetailsPerson)||input.matches(ShowLoanDetailsCompany)){
            Member.showLoan(a[6]);
        }
        else if (input.matches(ShowBankInterest)){
            Bank.showBankInterest(a[3]);
        }
        else if (input.matches(ShowBankBalance)){
            Bank.showBankBalance(a[3]);
        }
        else if (input.matches(ShowCentralBankBalance)){
            CentralBank.showCentralBankBalance();
        }
        else if (input.matches(ShowDate)){
            System.out.println(MyDate.getGlobalDate().toString());
        }
        else {
            System.out.println("The input command was invalid.");
            System.out.println("Consider these tips:\n1: Bank names must be entered in all capitals");
            System.out.println("2: Valid national numbers have 10 digits only");
            System.out.println("3: Valid card and account numbers have 16 digits only");
            System.out.println("4: Numbers cannot be negative");
            System.out.println("5: Valid names cannot include numbers or special characters");
            //System.out.println("6: Put hyphens (-) between date numbers");
        }
       return false;
    }
}
