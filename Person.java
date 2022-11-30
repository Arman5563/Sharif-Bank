import java.time.LocalDate;
public class Person extends Member{
    String firstName;
    String lastname;
    public static String personIDRegex="\\d+";

    Person(){}

    private Person(String firstName,String lastname, String nationalCode,String date) {
        startDay=LocalDate.parse(date);
        this.firstName = firstName;
        this.lastname=lastname;
        this.ID = nationalCode;
        this.money = 0;
        this.debt=0;
        isBanned=false;
    }
    static boolean createPerson(String firstName,String lastname, String nationalCode,String date){
        if (memberHashMap.containsKey(nationalCode)){
            return false;
        }
        Person temp=new Person(firstName,lastname, String.valueOf(nationalCode),date);
        memberHashMap.put(String.valueOf(nationalCode),temp);
        return true;
    }
    void show() {
        System.out.println("National Code: "+ID);
        System.out.println("Full name: "+firstName+" "+lastname);
        System.out.println("Birthdate: "+startDay);
        super.show();
    }
    static void showPersons(){
        for (Member m:Member.memberHashMap.values()){
            if (m instanceof Person)
                m.show();
        }
    }
}
