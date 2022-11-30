public class Company extends Member{
    Person manager;

    private Company(String ID, Person manager){
        this.startDay=MyDate.getGlobalDate();
        this.ID=ID;
        this.manager=manager;
        money=0;
        debt=0;
        isBanned = false;
    }
    private Company(){}
    static String createCompany(String name, String nationalCode){
        if (!Member.memberHashMap.containsKey(nationalCode))
            return "Error: Manager doesn't exist!";
        String ID=createID(name);
        if (Member.memberHashMap.containsKey(ID))
            return "Error: Company already exists";
        Person manager=(Person) Member.memberHashMap.get(nationalCode);
        Company c=new Company(ID, manager);
        Member.memberHashMap.put(ID,c);
        return "Company was created successfully";
    }
    public static String createID(String name){
        return MyDate.getGlobalDate().toString().replaceAll("-","")+"_"+name;
    }

    void show() {
        System.out.println("ID: "+ID);
        System.out.println("Manager name: "+manager.firstName+" "+manager.lastname);
        System.out.println("Founded on: "+startDay);
        super.show();
    }
    static void showCompanies(){
        for (Member m:Member.memberHashMap.values()){
            if (m instanceof Company)
                m.show();
        }
    }
}
