public enum BankList {
    MEllI(603799,"MELLI"),
    SEPAH(589210,"SEPAH"),
    SADERAT(603769,"SADERAT"),
    KESHAVARZI(603770,"KESHAVARZI"),
    MASKAN(628023,"MASKAN"),
    EGHTESADNOVIN(627412,"EGHTESADNOVIN"),
    PARSIAN(622106,"PARSIAN"),
    SARMAYE(639607,"SARMAYE"),
    DEY(502938,"DEY"),
    MELLAT(610433,"MELLAT"),
    TEJARAT(627353,"TEJARAT");
    final int ID;
    final String name;
    BankList(int a, String b){
        ID=a;
        name=b;
    }
}
