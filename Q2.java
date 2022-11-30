public class Q2 {
    public static void main(String[] args) {
        MyDate.getGlobalDate();
        InputProcessor inputProcessor=new InputProcessor();
        CentralBank.getInstance();
        boolean exit=false;
        while (!exit){
            exit=inputProcessor.run();
        }
    }
}