package carScraperServer.scrapeEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutotraderSearchHelper {
    private static final Map<String, String> makesMap = new HashMap<>();
    private static final Map<String, String> modelsMap = new HashMap<>();

    static {

        makesMap.put("Any Make", "");
        makesMap.put("Acura", "ACURA");
        makesMap.put("Alfa Romeo", "ALFA");
        makesMap.put("AMC", "AMC");
        makesMap.put("Aston Martin", "ASTON");
        makesMap.put("Audi", "AUDI");
        makesMap.put("Bentley", "BENTL");
        makesMap.put("BMW", "BMW");
        makesMap.put("Buick", "BUICK");
        makesMap.put("Cadillac", "CAD");
        makesMap.put("Chevrolet", "CHEV");
        makesMap.put("Chrysler", "CHRY");
        makesMap.put("Daewoo", "DAEW");
        makesMap.put("Datsun", "DATSUN");
        makesMap.put("DeLorean", "DELOREAN");
        makesMap.put("Dodge", "DODGE");
        makesMap.put("Eagle", "EAGLE");
        makesMap.put("Ferrari", "FER");
        makesMap.put("FIAT", "FIAT");
        makesMap.put("Fisker", "FISK");
        makesMap.put("Ford", "FORD");
        makesMap.put("Freightliner", "FREIGHT");
        makesMap.put("Geo", "GEO");
        makesMap.put("GMC", "GMC");
        makesMap.put("Honda", "HONDA");
        makesMap.put("HUMMER", "AMGEN");
        makesMap.put("Hyundai", "HYUND");
        makesMap.put("Infiniti", "INFIN");
        makesMap.put("Isuzu", "ISU");
        makesMap.put("Jaguar", "JAG");
        makesMap.put("Jeep", "JEEP");
        makesMap.put("Kia", "KIA");
        makesMap.put("Lamborghini", "LAM");
        makesMap.put("Land Rover", "ROV");
        makesMap.put("Lexus", "LEXUS");
        makesMap.put("Lincoln", "LINC");
        makesMap.put("Lotus", "LOTUS");
        makesMap.put("Maserati", "MAS");
        makesMap.put("Maybach", "MAYBACH");
        makesMap.put("Mazda", "MAZDA");
        makesMap.put("McLaren", "MCLAREN");
        makesMap.put("Mercedes-Benz", "MB");
        makesMap.put("Mercury", "MERC");
        makesMap.put("MINI", "MINI");
        makesMap.put("Mitsubishi", "MIT");
        makesMap.put("Nissan", "NISSAN");
        makesMap.put("Oldsmobile", "OLDS");
        makesMap.put("Plymouth", "PLYM");
        makesMap.put("Pontiac", "PONT");
        makesMap.put("Porsche", "POR");
        makesMap.put("RAM", "RAM");
        makesMap.put("Rolls-Royce", "RR");
        makesMap.put("Saab", "SAAB");
        makesMap.put("Saturn", "SATURN");
        makesMap.put("Scion", "SCION");
        makesMap.put("smart", "SMART");
        makesMap.put("SRT", "SRT");
        makesMap.put("Subaru", "SUB");
        makesMap.put("Suzuki", "SUZUKI");
        makesMap.put("Tesla", "TESLA");
        makesMap.put("Toyota", "TOYOTA");
        makesMap.put("Volkswagen", "VOLKS");
        makesMap.put("Volvo", "VOLVO");
        makesMap.put("Yugo", "YUGO");

        modelsMap.put("cl", "ACUCL");
        modelsMap.put("ilx", "ILX");
        modelsMap.put("integra", "INTEG");
        modelsMap.put("legend", "LEGEND");
        modelsMap.put("mdx", "MDX");
        modelsMap.put("nsx", "NSX");
        modelsMap.put("rdx", "RDX");
        modelsMap.put("rl", "RL");
        modelsMap.put("rlx", "RLX");
        modelsMap.put("rsx", "RSX");
        modelsMap.put("slx", "SLX");
        modelsMap.put("tl", "TL");
        modelsMap.put("tlx", "ACUTLX");
        modelsMap.put("tsx", "TSX");
        modelsMap.put("vigor", "VIGOR");
        modelsMap.put("zdx", "ZDX");
        modelsMap.put("other acura models", "ACUOTH");
    }

    public static String getMakeIdByName(String name) {
        return makesMap.get(name.toLowerCase());
    }

    public static String getModelIdByName(String name) {
        return modelsMap.get(name.toLowerCase());
    }

    public static Set<String> getMakesNames() {
        return makesMap.keySet();
    }

    public static Set<String> getModelNames() {
        return modelsMap.keySet();
    }
}
