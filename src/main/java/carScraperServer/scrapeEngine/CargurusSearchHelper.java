package carScraperServer.scrapeEngine;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CargurusSearchHelper {

    private static final Map<String, String> makesMap = new HashMap<>();
    private static final Map<String, String> modelsMap = new HashMap<>();

    static {

        makesMap.put("acura", "m4");
        makesMap.put("audi", "m19");
        makesMap.put("bmw", "m3");
        makesMap.put("buick", "m21");
        makesMap.put("cadillac", "m22");
        makesMap.put("chevrolet", "m1");
        makesMap.put("chrysler", "m23");
        makesMap.put("dodge", "m24");
        makesMap.put("fiat", "m98");
        makesMap.put("ford", "m2");
        makesMap.put("gmc", "m26");
        makesMap.put("honda", "m6");
        makesMap.put("hyundai", "m28");
        makesMap.put("infiniti", "m84");
        makesMap.put("jaguar", "m31");
        makesMap.put("jeep", "m32");
        makesMap.put("kia", "m33");
        makesMap.put("land rover", "m35");
        makesMap.put("lexus", "m37");
        makesMap.put("lincoln", "m38");
        makesMap.put("maserati", "m40");
        makesMap.put("mazda", "m42");
        makesMap.put("mercedes-benz", "m43");
        makesMap.put("mercury", "m44");
        makesMap.put("mini", "m45");
        makesMap.put("mitsubishi", "m46");
        makesMap.put("nissan", "m12");
        makesMap.put("pontiac", "m47");
        makesMap.put("porsche", "m48");
        makesMap.put("ram", "m191");
        makesMap.put("saturn", "m51");
        makesMap.put("scion", "m52");
        makesMap.put("subaru", "m53");
        makesMap.put("toyota", "m7");
        makesMap.put("volkswagen", "m55");
        makesMap.put("volvo", "m56");
        makesMap.put("alfa romeo", "m124");
        makesMap.put("am general", "m79");
        makesMap.put("amc", "m133");
        makesMap.put("aston martin", "m110");
        makesMap.put("austin", "m119");
        makesMap.put("austin-healey", "m139");
        makesMap.put("bentley", "m20");
        makesMap.put("bricklin", "m196");
        makesMap.put("bugatti", "m113");
        makesMap.put("daewoo", "m81");
        makesMap.put("daihatsu", "m95");
        makesMap.put("datsun", "m96");
        makesMap.put("de tomaso", "m179");
        makesMap.put("delorean", "m97");
        makesMap.put("eagle", "m82");
        makesMap.put("edsel", "m174");
        makesMap.put("ferrari", "m25");
        makesMap.put("fisker", "m183");
        makesMap.put("freightliner", "m99");
        makesMap.put("geo", "m83");
        makesMap.put("hillman", "m144");
        makesMap.put("hudson", "m186");
        makesMap.put("hummer", "m27");
        makesMap.put("international harvester", "m158");
        makesMap.put("isuzu", "m30");
        makesMap.put("jensen", "m200");
        makesMap.put("lamborghini", "m34");
        makesMap.put("lancia", "m100");
        makesMap.put("lotus", "m39");
        makesMap.put("maybach", "m41");
        makesMap.put("mclaren", "m141");
        makesMap.put("merkur", "m101");
        makesMap.put("mg", "m102");
        makesMap.put("morgan", "m150");
        makesMap.put("nash", "m199");
        makesMap.put("oldsmobile", "m85");
        makesMap.put("packard", "m176");
        makesMap.put("panoz", "m86");
        makesMap.put("plymouth", "m87");
        makesMap.put("rolls-royce", "m49");
        makesMap.put("rover", "m131");
        makesMap.put("saab", "m50");
        makesMap.put("saleen", "m108");
        makesMap.put("shelby", "m135");
        makesMap.put("smart", "m111");
        makesMap.put("srt", "m194");
        makesMap.put("studebaker", "m164");
        makesMap.put("sunbeam", "m132");
        makesMap.put("suzuki", "m54");
        makesMap.put("tesla", "m112");
        makesMap.put("triumph", "m137");


        modelsMap.put("cl", "d191");
        modelsMap.put("ilx", "d2137");
        modelsMap.put("integra", "d36");
        modelsMap.put("mdx", "d16");
        modelsMap.put("rdx", "d921");
        modelsMap.put("rl", "d18");
        modelsMap.put("rlx", "d2214");
        modelsMap.put("rsx", "d3");
        modelsMap.put("tl", "d19");
        modelsMap.put("tlx", "d2278");
        modelsMap.put("tsx", "d20");
        modelsMap.put("zdx", "d2065");
        modelsMap.put("legend", "d37");
        modelsMap.put("nsx", "d17");
        modelsMap.put("slx", "d192");
        modelsMap.put("vigor", "d38");


        modelsMap.put("a3", "d24");
        modelsMap.put("a3 sportback", "d2326");
        modelsMap.put("a4", "d25");
        modelsMap.put("a4 avant", "d26");
        modelsMap.put("a5", "d1034");
        modelsMap.put("a6", "d27");
        modelsMap.put("a7", "d2113");
        modelsMap.put("a8", "d29");
        modelsMap.put("allroad", "d2201");
        modelsMap.put("allroad quattro", "d683");
        modelsMap.put("q3", "d2129");
        modelsMap.put("q5", "d1988");
        modelsMap.put("q5 hybrid", "d2157");
        modelsMap.put("q7", "d930");
        modelsMap.put("r8", "d1019");
        modelsMap.put("rs 5", "d2136");
        modelsMap.put("rs 7", "d2230");
        modelsMap.put("s3", "d1183");
        modelsMap.put("s4", "d30");
        modelsMap.put("s5", "d1055");
        modelsMap.put("s6", "d687");
        modelsMap.put("s7", "d2156");
        modelsMap.put("s8", "d688");
        modelsMap.put("sq5", "d2237");
        modelsMap.put("tt", "d32");
        modelsMap.put("tts", "d2176");
        modelsMap.put("100", "d679");
        modelsMap.put("5000", "d1255");
        modelsMap.put("80", "d681");
        modelsMap.put("90", "d682");
        modelsMap.put("a6 avant", "d28");
        modelsMap.put("cabriolet", "d684");
        modelsMap.put("coupe", "d685");
        modelsMap.put("rs 4", "d992");
        modelsMap.put("rs 6", "d686");
        modelsMap.put("s4 avant", "d31");
        modelsMap.put("tt rs", "d2177");
        modelsMap.put("v8", "d689");


        modelsMap.put("1 series", "d1052");
        modelsMap.put("2 series", "d2262");
        modelsMap.put("3 series", "d1512");
        modelsMap.put("3 series gran turismo", "d2240");
        modelsMap.put("4 series", "d2244");
        modelsMap.put("5 series", "d1628");
        modelsMap.put("5 series gran turismo", "d2075");
        modelsMap.put("6 series", "d1513");
        modelsMap.put("7 series", "d1517");
        modelsMap.put("activehybrid 3", "d2186");
        modelsMap.put("activehybrid 5", "d2180");
        modelsMap.put("activehybrid 7", "d2181");
        modelsMap.put("alpina b6", "d2275");
        modelsMap.put("alpina b7", "d1035");
        modelsMap.put("i3", "d2263");
        modelsMap.put("i8", "d2274");
        modelsMap.put("m3", "d390");
        modelsMap.put("m4", "d2258");
        modelsMap.put("m5", "d391");
        modelsMap.put("m6", "d825");
        modelsMap.put("x1", "d2160");
        modelsMap.put("x3", "d392");
        modelsMap.put("x4", "d2271");
        modelsMap.put("x5", "d393");
        modelsMap.put("x5 m", "d2120");
        modelsMap.put("x6", "d1137");
        modelsMap.put("x6 m", "d2139");
        modelsMap.put("z3", "d394");
        modelsMap.put("z4", "d395");
        modelsMap.put("1m", "d2172");
        modelsMap.put("3.0", "d2380");
        modelsMap.put("8 series", "d1627");
        modelsMap.put("m1", "d1697");
        modelsMap.put("z3 m", "d2161");
        modelsMap.put("z4 m", "d2162");
        modelsMap.put("z8", "d396");


        modelsMap.put("century", "d269");
        modelsMap.put("enclave", "d1029");
        modelsMap.put("encore", "d2128");
        modelsMap.put("lacrosse", "d272");
        modelsMap.put("lesabre", "d273");
        modelsMap.put("lucerne", "d844");
        modelsMap.put("park avenue", "d274");
        modelsMap.put("rainier", "d275");
        modelsMap.put("regal", "d277");
        modelsMap.put("rendezvous", "d278");
        modelsMap.put("riviera", "d279");
        modelsMap.put("terraza", "d282");
        modelsMap.put("verano", "d2119");
        modelsMap.put("electra", "d270");
        modelsMap.put("estate wagon", "d271");
        modelsMap.put("grand national", "d1336");
        modelsMap.put("reatta", "d276");
        modelsMap.put("roadmaster", "d280");
        modelsMap.put("skyhawk", "d1949");
        modelsMap.put("skylark", "d281");
        modelsMap.put("somerset", "d1780");
        modelsMap.put("special", "d1765");
        modelsMap.put("wildcat", "d1740");


        modelsMap.put("ats", "d2138");
        modelsMap.put("ats coupe", "d2269");
        modelsMap.put("ats-v", "d2302");
        modelsMap.put("cts", "d138");
        modelsMap.put("cts coupe", "d2084");
        modelsMap.put("cts-v", "d139");
        modelsMap.put("cts-v coupe", "d2085");
        modelsMap.put("deville", "d140");
        modelsMap.put("dts", "d732");
        modelsMap.put("eldorado", "d141");
        modelsMap.put("elr", "d2204");
        modelsMap.put("escalade", "d142");
        modelsMap.put("escalade esv", "d143");
        modelsMap.put("escalade ext", "d144");
        modelsMap.put("fleetwood", "d145");
        modelsMap.put("seville", "d146");
        modelsMap.put("srx", "d148");
        modelsMap.put("sts", "d149");
        modelsMap.put("xlr", "d150");
        modelsMap.put("xts", "d2141");
        modelsMap.put("allante", "d135");
        modelsMap.put("brougham", "d136");
        modelsMap.put("catera", "d137");
        modelsMap.put("lasalle", "d2330");
        modelsMap.put("series 62", "d2328");
        modelsMap.put("sixty special", "d147");
        modelsMap.put("sts-v", "d832");
        modelsMap.put("xlr-v", "d833");


        modelsMap.put("astro", "d597");
        modelsMap.put("astro cargo van", "d598");
        modelsMap.put("avalanche", "d599");
        modelsMap.put("aveo", "d600");
        modelsMap.put("bel air", "d774");
        modelsMap.put("blazer", "d602");
        modelsMap.put("c/k 1500", "d755");
        modelsMap.put("c/k 2500", "d752");
        modelsMap.put("c/k 3500", "d753");
        modelsMap.put("c10", "d1560");
        modelsMap.put("camaro", "d606");
        modelsMap.put("caprice", "d607");
        modelsMap.put("captiva sport", "d1713");
        modelsMap.put("cavalier", "d608");
        modelsMap.put("chevelle", "d739");
        modelsMap.put("city express", "d2280");
        modelsMap.put("classic", "d612");
        modelsMap.put("cobalt", "d613");
        modelsMap.put("colorado", "d614");
        modelsMap.put("corvette", "d1");
        modelsMap.put("cruze", "d2076");
        modelsMap.put("el camino", "d1102");
        modelsMap.put("equinox", "d616");
        modelsMap.put("express", "d617");
        modelsMap.put("express cargo", "d618");
        modelsMap.put("hhr", "d716");
        modelsMap.put("impala", "d619");
        modelsMap.put("lumina", "d620");
        modelsMap.put("malibu", "d622");
        modelsMap.put("malibu maxx", "d623");
        modelsMap.put("monte carlo", "d625");
        modelsMap.put("nova", "d1106");
        modelsMap.put("prizm", "d626");
        modelsMap.put("s-10", "d628");
        modelsMap.put("silverado 1500", "d630");
        modelsMap.put("silverado 1500 ss", "d631");
        modelsMap.put("silverado 1500hd", "d632");
        modelsMap.put("silverado 2500", "d633");
        modelsMap.put("silverado 2500hd", "d634");
        modelsMap.put("silverado 3500", "d635");
        modelsMap.put("silverado 3500hd", "d1027");
        modelsMap.put("silverado classic 1500", "d1030");
        modelsMap.put("silverado classic 2500hd", "d1031");
        modelsMap.put("silverado classic 3500", "d1028");
        modelsMap.put("sonic", "d2112");
        modelsMap.put("spark", "d2008");
        modelsMap.put("spark ev", "d2205");
        modelsMap.put("ss", "d2217");
        modelsMap.put("ssr", "d637");
        modelsMap.put("suburban", "d638");
        modelsMap.put("tahoe", "d639");
        modelsMap.put("tracker", "d641");
        modelsMap.put("trailblazer", "d642");
        modelsMap.put("trailblazer ext", "d643");
        modelsMap.put("traverse", "d1521");
        modelsMap.put("trax", "d2272");
        modelsMap.put("uplander", "d644");
        modelsMap.put("venture", "d645");
        modelsMap.put("volt", "d2012");
        modelsMap.put("210", "d2299");
        modelsMap.put("3100", "d2283");
        modelsMap.put("3200", "d2339");
        modelsMap.put("3600", "d2345");
        modelsMap.put("apache", "d2245");
        modelsMap.put("beretta", "d601");
        modelsMap.put("biscayne", "d775");
        modelsMap.put("c/k 10", "d1610");
        modelsMap.put("c/k 20", "d1611");
        modelsMap.put("c/k 30", "d1612");
        modelsMap.put("celebrity", "d609");
        modelsMap.put("chevy van", "d610");
        modelsMap.put("chevy van classic", "d611");
        modelsMap.put("corsica", "d615");
        modelsMap.put("corvair", "d1179");
        modelsMap.put("delray", "d776");
        modelsMap.put("deluxe", "d2336");
        modelsMap.put("fleetline", "d2340");
        modelsMap.put("lumina minivan", "d621");
        modelsMap.put("master", "d2386");
        modelsMap.put("metro", "d624");
        modelsMap.put("monza", "d1344");
        modelsMap.put("nomad", "d1692");
        modelsMap.put("r/v 3500 series", "d756");
        modelsMap.put("s-10 blazer", "d629");
        modelsMap.put("silverado hybrid", "d2059");
        modelsMap.put("special deluxe", "d2236");
        modelsMap.put("sportvan", "d636");
        modelsMap.put("styleline", "d2232");
        modelsMap.put("superior", "d2353");
        modelsMap.put("vega", "d1632");


        modelsMap.put("200", "d2106");
        modelsMap.put("300", "d165");
        modelsMap.put("300m", "d167");
        modelsMap.put("aspen", "d945");
        modelsMap.put("cirrus", "d168");
        modelsMap.put("concorde", "d169");
        modelsMap.put("crossfire", "d170");
        modelsMap.put("lhs", "d175");
        modelsMap.put("pacifica", "d177");
        modelsMap.put("pt cruiser", "d179");
        modelsMap.put("sebring", "d180");
        modelsMap.put("town &amp; country", "d182");
        modelsMap.put("voyager", "d183");
        modelsMap.put("conquest", "d1532");
        modelsMap.put("cordoba", "d1913");
        modelsMap.put("crossfire srt-6", "d171");
        modelsMap.put("fifth avenue", "d1096");
        modelsMap.put("grand voyager", "d172");
        modelsMap.put("imperial", "d173");
        modelsMap.put("laser", "d1551");
        modelsMap.put("le baron", "d174");
        modelsMap.put("new yorker", "d176");
        modelsMap.put("newport", "d1804");
        modelsMap.put("prowler", "d178");
        modelsMap.put("saratoga", "d1871");
        modelsMap.put("tc", "d181");
        modelsMap.put("valiant", "d2390");
        modelsMap.put("windsor", "d2331");


        modelsMap.put("avenger", "d646");
        modelsMap.put("caliber", "d932");
        modelsMap.put("caravan", "d647");
        modelsMap.put("challenger", "d894");
        modelsMap.put("charger", "d733");
        modelsMap.put("dakota", "d649");
        modelsMap.put("dart", "d896");
        modelsMap.put("durango", "d651");
        modelsMap.put("grand caravan", "d653");
        modelsMap.put("intrepid", "d654");
        modelsMap.put("journey", "d1135");
        modelsMap.put("magnum", "d655");
        modelsMap.put("neon", "d657");
        modelsMap.put("nitro", "d899");
        modelsMap.put("ram 1500", "d665");
        modelsMap.put("ram 2500", "d667");
        modelsMap.put("ram 3500", "d668");
        modelsMap.put("ram srt-10", "d911");
        modelsMap.put("ram van", "d669");
        modelsMap.put("sprinter cargo", "d675");
        modelsMap.put("stratus", "d677");
        modelsMap.put("viper", "d678");
        modelsMap.put("440", "d1235");
        modelsMap.put("600", "d1744");
        modelsMap.put("a100", "d2346");
        modelsMap.put("aries", "d1591");
        modelsMap.put("aspen", "d1472");
        modelsMap.put("coronet", "d895");
        modelsMap.put("daytona", "d650");
        modelsMap.put("diplomat", "d897");
        modelsMap.put("dynasty", "d652");
        modelsMap.put("luxury liner", "d2338");
        modelsMap.put("monaco", "d656");
        modelsMap.put("neon srt-4", "d658");
        modelsMap.put("omni", "d659");
        modelsMap.put("polara", "d1703");
        modelsMap.put("power wagon", "d900");
        modelsMap.put("raider", "d1326");
        modelsMap.put("ram", "d902");
        modelsMap.put("ram 150", "d660");
        modelsMap.put("ram 250", "d661");
        modelsMap.put("ram 350", "d662");
        modelsMap.put("ram 50 pickup", "d663");
        modelsMap.put("ram cargo", "d664");
        modelsMap.put("ram chassis 3500", "d2227");
        modelsMap.put("ram chassis 4500", "d2228");
        modelsMap.put("ram wagon", "d670");
        modelsMap.put("ramcharger", "d671");
        modelsMap.put("rampage", "d903");
        modelsMap.put("shadow", "d672");
        modelsMap.put("spirit", "d673");
        modelsMap.put("sprinter", "d674");
        modelsMap.put("stealth", "d676");
        modelsMap.put("super bee", "d904");


        modelsMap.put("500", "d1327");
        modelsMap.put("500e", "d2239");
        modelsMap.put("500l", "d2199");
        modelsMap.put("500x", "d2306");


        modelsMap.put("bronco", "d320");
        modelsMap.put("c-max", "d1315");
        modelsMap.put("contour", "d322");
        modelsMap.put("crown victoria", "d324");
        modelsMap.put("e-150", "d325");
        modelsMap.put("e-250", "d326");
        modelsMap.put("e-350", "d327");
        modelsMap.put("e-series cargo", "d1140");
        modelsMap.put("e-series passenger", "d1139");
        modelsMap.put("edge", "d923");
        modelsMap.put("escape", "d330");
        modelsMap.put("escape hybrid", "d759");
        modelsMap.put("escort", "d331");
        modelsMap.put("excursion", "d332");
        modelsMap.put("expedition", "d333");
        modelsMap.put("explorer", "d334");
        modelsMap.put("explorer sport", "d335");
        modelsMap.put("explorer sport trac", "d336");
        modelsMap.put("f-100", "d1395");
        modelsMap.put("f-150", "d337");
        modelsMap.put("f-150 heritage", "d338");
        modelsMap.put("f-150 svt lightning", "d339");
        modelsMap.put("f-250", "d340");
        modelsMap.put("f-250 super duty", "d341");
        modelsMap.put("f-350", "d342");
        modelsMap.put("f-350 super duty", "d343");
        modelsMap.put("f-450 super duty", "d1022");
        modelsMap.put("f-550 super duty", "d2224");
        modelsMap.put("fiesta", "d1060");
        modelsMap.put("five hundred", "d345");
        modelsMap.put("flex", "d1049");
        modelsMap.put("focus", "d346");
        modelsMap.put("freestar", "d348");
        modelsMap.put("freestyle", "d349");
        modelsMap.put("fusion", "d845");
        modelsMap.put("fusion energi", "d2197");
        modelsMap.put("model a", "d1564");
        modelsMap.put("mustang", "d2");
        modelsMap.put("mustang svt cobra", "d352");
        modelsMap.put("ranger", "d354");
        modelsMap.put("shelby gt350", "d2303");
        modelsMap.put("shelby gt500", "d924");
        modelsMap.put("taurus", "d355");
        modelsMap.put("taurus x", "d1020");
        modelsMap.put("thunderbird", "d357");
        modelsMap.put("transit cargo", "d1067");
        modelsMap.put("transit connect", "d2037");
        modelsMap.put("transit passenger", "d2270");
        modelsMap.put("windstar", "d358");
        modelsMap.put("aerostar", "d318");
        modelsMap.put("aspire", "d319");
        modelsMap.put("bronco ii", "d321");
        modelsMap.put("classic", "d741");
        modelsMap.put("contour svt", "d323");
        modelsMap.put("country squire", "d742");
        modelsMap.put("courier", "d2028");
        modelsMap.put("crestline", "d1965");
        modelsMap.put("custom", "d2311");
        modelsMap.put("customline", "d2246");
        modelsMap.put("deluxe", "d2378");
        modelsMap.put("econoline cargo", "d328");
        modelsMap.put("econoline pickup", "d2183");
        modelsMap.put("econoline wagon", "d329");
        modelsMap.put("elite", "d743");
        modelsMap.put("exp", "d1932");
        modelsMap.put("fairlane", "d1307");
        modelsMap.put("fairmont", "d1694");
        modelsMap.put("falcon", "d1341");
        modelsMap.put("festiva", "d344");
        modelsMap.put("focus svt", "d347");
        modelsMap.put("galaxie", "d1213");
        modelsMap.put("granada", "d1165");
        modelsMap.put("gt", "d350");
        modelsMap.put("ltd", "d1394");
        modelsMap.put("ltd crown victoria", "d351");
        modelsMap.put("maverick", "d1293");
        modelsMap.put("model 40", "d2350");
        modelsMap.put("model b", "d1393");
        modelsMap.put("model t", "d2049");
        modelsMap.put("pinto", "d1571");
        modelsMap.put("probe", "d353");
        modelsMap.put("ranchero", "d1362");
        modelsMap.put("tempo", "d356");
        modelsMap.put("torino", "d1198");
        modelsMap.put("transit connect electric", "d2198");
        modelsMap.put("windstar cargo", "d359");


        modelsMap.put("acadia", "d925");
        modelsMap.put("canyon", "d103");
        modelsMap.put("envoy", "d104");
        modelsMap.put("envoy xl", "d105");
        modelsMap.put("envoy xuv", "d106");
        modelsMap.put("jimmy", "d107");
        modelsMap.put("safari", "d112");
        modelsMap.put("savana", "d114");
        modelsMap.put("savana cargo", "d115");
        modelsMap.put("sierra 1500", "d116");
        modelsMap.put("sierra 1500hd", "d117");
        modelsMap.put("sierra 2500", "d118");
        modelsMap.put("sierra 2500hd", "d119");
        modelsMap.put("sierra 2500hd classic", "d972");
        modelsMap.put("sierra 3500", "d120");
        modelsMap.put("sierra 3500hd", "d973");
        modelsMap.put("sierra classic 1500", "d122");
        modelsMap.put("sonoma", "d125");
        modelsMap.put("suburban", "d126");
        modelsMap.put("terrain", "d2042");
        modelsMap.put("terrain denali", "d2159");
        modelsMap.put("yukon", "d130");
        modelsMap.put("yukon denali", "d131");
        modelsMap.put("yukon xl", "d132");
        modelsMap.put("yukon xl denali", "d2335");
        modelsMap.put("c/k 2500 series", "d771");
        modelsMap.put("c/k 30", "d1617");
        modelsMap.put("c/k 3500 series", "d772");
        modelsMap.put("caballero", "d2221");
        modelsMap.put("r/v 3500 series", "d773");
        modelsMap.put("rally wagon", "d109");
        modelsMap.put("s-15", "d110");
        modelsMap.put("s-15 jimmy", "d111");
        modelsMap.put("safari cargo", "d113");
        modelsMap.put("sierra", "d926");
        modelsMap.put("sierra c/k 1500", "d975");
        modelsMap.put("sierra c/k 3500", "d1614");
        modelsMap.put("sierra c3", "d121");
        modelsMap.put("sierra classic 2500", "d123");
        modelsMap.put("sierra classic 3500", "d124");
        modelsMap.put("sprint", "d1736");
        modelsMap.put("syclone", "d127");
        modelsMap.put("typhoon", "d128");
        modelsMap.put("vandura", "d129");


        modelsMap.put("accord", "d585");
        modelsMap.put("accord coupe", "d976");
        modelsMap.put("accord crosstour", "d2067");
        modelsMap.put("accord hybrid", "d2256");
        modelsMap.put("civic", "d586");
        modelsMap.put("civic coupe", "d977");
        modelsMap.put("cr-v", "d589");
        modelsMap.put("cr-z", "d2081");
        modelsMap.put("crosstour", "d2184");
        modelsMap.put("element", "d590");
        modelsMap.put("fit", "d744");
        modelsMap.put("hr-v", "d1271");
        modelsMap.put("insight", "d591");
        modelsMap.put("odyssey", "d592");
        modelsMap.put("passport", "d593");
        modelsMap.put("pilot", "d594");
        modelsMap.put("prelude", "d595");
        modelsMap.put("ridgeline", "d734");
        modelsMap.put("s2000", "d596");
        modelsMap.put("accord plug-in hybrid", "d2155");
        modelsMap.put("civic crx", "d587");
        modelsMap.put("civic del sol", "d588");
        modelsMap.put("fit ev", "d2175");


        modelsMap.put("accent", "d91");
        modelsMap.put("azera", "d831");
        modelsMap.put("elantra", "d92");
        modelsMap.put("elantra coupe", "d2143");
        modelsMap.put("elantra gt", "d2144");
        modelsMap.put("elantra touring", "d2033");
        modelsMap.put("entourage", "d942");
        modelsMap.put("equus", "d2087");
        modelsMap.put("genesis", "d1510");
        modelsMap.put("genesis coupe", "d2034");
        modelsMap.put("santa fe", "d94");
        modelsMap.put("sonata", "d96");
        modelsMap.put("sonata hybrid", "d2107");
        modelsMap.put("sonata plug-in hybrid", "d2322");
        modelsMap.put("tiburon", "d97");
        modelsMap.put("tucson", "d98");
        modelsMap.put("veloster", "d2124");
        modelsMap.put("veloster turbo", "d2142");
        modelsMap.put("veracruz", "d943");
        modelsMap.put("xg350", "d100");
        modelsMap.put("excel", "d93");
        modelsMap.put("xg300", "d99");


        modelsMap.put("ex35", "d1138");
        modelsMap.put("fx35", "d573");
        modelsMap.put("fx37", "d2174");
        modelsMap.put("fx45", "d574");
        modelsMap.put("g20", "d575");
        modelsMap.put("g25", "d2088");
        modelsMap.put("g35", "d576");
        modelsMap.put("g37", "d1036");
        modelsMap.put("i30", "d577");
        modelsMap.put("i35", "d578");
        modelsMap.put("jx35", "d2148");
        modelsMap.put("m35", "d735");
        modelsMap.put("m37", "d2073");
        modelsMap.put("m45", "d581");
        modelsMap.put("q40", "d2287");
        modelsMap.put("q45", "d582");
        modelsMap.put("q50", "d2207");
        modelsMap.put("q50 hybrid", "d2208");
        modelsMap.put("q60", "d2251");
        modelsMap.put("q60 ipl", "d2252");
        modelsMap.put("q70", "d2265");
        modelsMap.put("q70l", "d2289");
        modelsMap.put("qx4", "d583");
        modelsMap.put("qx50", "d2247");
        modelsMap.put("qx56", "d584");
        modelsMap.put("qx60", "d2243");
        modelsMap.put("qx60 hybrid", "d2253");
        modelsMap.put("qx70", "d2242");
        modelsMap.put("qx80", "d2248");
        modelsMap.put("ex37", "d2182");
        modelsMap.put("fx50", "d1990");
        modelsMap.put("ipl g coupe", "d2165");
        modelsMap.put("j30", "d579");
        modelsMap.put("m hybrid", "d2109");
        modelsMap.put("m30", "d580");
        modelsMap.put("m56", "d2074");


        modelsMap.put("f-type", "d2209");
        modelsMap.put("s-type", "d283");
        modelsMap.put("x-type", "d285");
        modelsMap.put("xf", "d1136");
        modelsMap.put("xj-series", "d286");
        modelsMap.put("xjr", "d287");
        modelsMap.put("xk-series", "d288");
        modelsMap.put("e-type", "d1406");
        modelsMap.put("mark 1", "d1718");
        modelsMap.put("mark 2", "d1809");
        modelsMap.put("s-type r", "d284");
        modelsMap.put("xj-s", "d1367");
        modelsMap.put("xk120", "d2249");
        modelsMap.put("xk140", "d1494");


        modelsMap.put("cherokee", "d488");
        modelsMap.put("commander", "d849");
        modelsMap.put("compass", "d905");
        modelsMap.put("grand cherokee", "d490");
        modelsMap.put("liberty", "d492");
        modelsMap.put("patriot", "d906");
        modelsMap.put("renegade", "d2268");
        modelsMap.put("wrangler", "d494");
        modelsMap.put("cj5", "d1575");
        modelsMap.put("cj7", "d1549");
        modelsMap.put("cj8", "d1576");
        modelsMap.put("comanche", "d489");
        modelsMap.put("grand wagoneer", "d491");
        modelsMap.put("wagoneer", "d493");


        modelsMap.put("amanti", "d157");
        modelsMap.put("borrego", "d1169");
        modelsMap.put("cadenza", "d2215");
        modelsMap.put("forte", "d2043");
        modelsMap.put("forte koup", "d2044");
        modelsMap.put("forte5", "d2153");
        modelsMap.put("k900", "d2264");
        modelsMap.put("optima", "d158");
        modelsMap.put("optima hybrid", "d2154");
        modelsMap.put("rio", "d159");
        modelsMap.put("rio5", "d2152");
        modelsMap.put("sedona", "d160");
        modelsMap.put("sorento", "d162");
        modelsMap.put("soul", "d2020");
        modelsMap.put("soul ev", "d2290");
        modelsMap.put("spectra", "d163");
        modelsMap.put("sportage", "d164");
        modelsMap.put("sephia", "d161");


        modelsMap.put("discovery", "d152");
        modelsMap.put("discovery sport", "d2304");
        modelsMap.put("lr2", "d927");
        modelsMap.put("lr3", "d155");
        modelsMap.put("lr4", "d2041");
        modelsMap.put("range rover", "d156");
        modelsMap.put("range rover evoque", "d2121");
        modelsMap.put("range rover sport", "d834");
        modelsMap.put("defender", "d151");
        modelsMap.put("discovery series ii", "d153");
        modelsMap.put("freelander", "d154");


        modelsMap.put("ct 200h", "d2115");
        modelsMap.put("es 300", "d557");
        modelsMap.put("es 300h", "d2163");
        modelsMap.put("es 330", "d558");
        modelsMap.put("es 350", "d917");
        modelsMap.put("gs 200t", "d2351");
        modelsMap.put("gs 300", "d559");
        modelsMap.put("gs 350", "d920");
        modelsMap.put("gs 430", "d561");
        modelsMap.put("gs 450h", "d918");
        modelsMap.put("gs f", "d2366");
        modelsMap.put("gx 460", "d2063");
        modelsMap.put("gx 470", "d562");
        modelsMap.put("hs 250h", "d2038");
        modelsMap.put("is 200t", "d1733");
        modelsMap.put("is 250", "d853");
        modelsMap.put("is 300", "d563");
        modelsMap.put("is 350", "d854");
        modelsMap.put("is c", "d2057");
        modelsMap.put("is f", "d1404");
        modelsMap.put("ls 400", "d564");
        modelsMap.put("ls 430", "d565");
        modelsMap.put("ls 460", "d934");
        modelsMap.put("ls 600h l", "d1046");
        modelsMap.put("lx 470", "d567");
        modelsMap.put("lx 570", "d1518");
        modelsMap.put("nx 200t", "d2293");
        modelsMap.put("nx 300h", "d2294");
        modelsMap.put("rc 200t", "d2363");
        modelsMap.put("rc 300", "d2364");
        modelsMap.put("rc 350", "d2296");
        modelsMap.put("rc f", "d2297");
        modelsMap.put("rx 300", "d568");
        modelsMap.put("rx 330", "d569");
        modelsMap.put("rx 350", "d919");
        modelsMap.put("rx 400h", "d736");
        modelsMap.put("rx 450h", "d2053");
        modelsMap.put("sc 430", "d572");
        modelsMap.put("es 250", "d556");
        modelsMap.put("gs 400", "d560");
        modelsMap.put("gs 460", "d1514");
        modelsMap.put("lfa", "d2082");
        modelsMap.put("lx 450", "d566");
        modelsMap.put("sc 300", "d570");
        modelsMap.put("sc 400", "d571");


        modelsMap.put("aviator", "d524");
        modelsMap.put("continental", "d526");
        modelsMap.put("ls", "d527");
        modelsMap.put("mark lt", "d762");
        modelsMap.put("mkc", "d2259");
        modelsMap.put("mks", "d1152");
        modelsMap.put("mkt", "d2032");
        modelsMap.put("mkx", "d928");
        modelsMap.put("mkz", "d974");
        modelsMap.put("navigator", "d530");
        modelsMap.put("town car", "d531");
        modelsMap.put("zephyr", "d763");
        modelsMap.put("blackwood", "d525");
        modelsMap.put("mark vii", "d528");
        modelsMap.put("mark viii", "d529");


        modelsMap.put("ghibli", "d1456");
        modelsMap.put("granturismo", "d1465");
        modelsMap.put("quattroporte", "d402");
        modelsMap.put("coupe", "d400");
        modelsMap.put("gransport", "d401");
        modelsMap.put("merak", "d2060");
        modelsMap.put("spyder", "d403");


        modelsMap.put("626", "d210");
        modelsMap.put("b-series pickup", "d212");
        modelsMap.put("cx-3", "d2301");
        modelsMap.put("cx-5", "d2133");
        modelsMap.put("cx-7", "d935");
        modelsMap.put("cx-9", "d1023");
        modelsMap.put("mazda2", "d1655");
        modelsMap.put("mazda3", "d214");
        modelsMap.put("mazda5", "d840");
        modelsMap.put("mazda6", "d215");
        modelsMap.put("mazdaspeed3", "d941");
        modelsMap.put("millenia", "d218");
        modelsMap.put("mpv", "d219");
        modelsMap.put("mx-5 miata", "d221");
        modelsMap.put("protege", "d224");
        modelsMap.put("protege5", "d225");
        modelsMap.put("rx-8", "d227");
        modelsMap.put("tribute", "d228");
        modelsMap.put("929", "d211");
        modelsMap.put("b-series truck", "d213");
        modelsMap.put("mazdaspeed mx-5 miata", "d216");
        modelsMap.put("mazdaspeed protege", "d217");
        modelsMap.put("mazdaspeed6", "d841");
        modelsMap.put("mx-3", "d220");
        modelsMap.put("mx-6", "d222");
        modelsMap.put("navajo", "d223");
        modelsMap.put("rx-7", "d226");
        modelsMap.put("truck", "d229");


        modelsMap.put("300-class", "d59");
        modelsMap.put("380-class", "d2308");
        modelsMap.put("amg gt", "d2282");
        modelsMap.put("b-class", "d2071");
        modelsMap.put("c-class", "d66");
        modelsMap.put("cl-class", "d71");
        modelsMap.put("cla-class", "d2216");
        modelsMap.put("clk-class", "d74");
        modelsMap.put("cls-class", "d751");
        modelsMap.put("e-class", "d76");
        modelsMap.put("g-class", "d78");
        modelsMap.put("gl-class", "d936");
        modelsMap.put("gla-class", "d2286");
        modelsMap.put("glc-class", "d2361");
        modelsMap.put("gle-class", "d2317");
        modelsMap.put("glk-class", "d1618");
        modelsMap.put("m-class", "d80");
        modelsMap.put("metris", "d2348");
        modelsMap.put("metris cargo", "d2349");
        modelsMap.put("r-class", "d829");
        modelsMap.put("s-class", "d82");
        modelsMap.put("s-class coupe", "d2276");
        modelsMap.put("sl-class", "d84");
        modelsMap.put("slk-class", "d87");
        modelsMap.put("sls-class", "d2078");
        modelsMap.put("sprinter", "d1830");
        modelsMap.put("sprinter cargo", "d2219");
        modelsMap.put("190-class", "d58");
        modelsMap.put("220", "d1973");
        modelsMap.put("240", "d2192");
        modelsMap.put("280", "d1182");
        modelsMap.put("350-class", "d60");
        modelsMap.put("400-class", "d61");
        modelsMap.put("420-class", "d62");
        modelsMap.put("450-class", "d2089");
        modelsMap.put("500-class", "d63");
        modelsMap.put("560-class", "d64");
        modelsMap.put("600-class", "d65");
        modelsMap.put("slr mclaren", "d90");


        modelsMap.put("cougar", "d361");
        modelsMap.put("grand marquis", "d362");
        modelsMap.put("mariner", "d364");
        modelsMap.put("mariner hybrid", "d760");
        modelsMap.put("milan", "d846");
        modelsMap.put("montego", "d365");
        modelsMap.put("monterey", "d366");
        modelsMap.put("mountaineer", "d367");
        modelsMap.put("sable", "d369");
        modelsMap.put("villager", "d372");
        modelsMap.put("capri", "d360");
        modelsMap.put("comet", "d1265");
        modelsMap.put("lynx", "d1918");
        modelsMap.put("marauder", "d363");
        modelsMap.put("marquis", "d1561");
        modelsMap.put("monarch", "d1447");
        modelsMap.put("mystique", "d368");
        modelsMap.put("park lane", "d1796");
        modelsMap.put("topaz", "d370");
        modelsMap.put("tracer", "d371");
        modelsMap.put("zephyr", "d1881");


        modelsMap.put("cooper", "d436");
        modelsMap.put("cooper clubman", "d1044");
        modelsMap.put("cooper coupe", "d2123");
        modelsMap.put("cooper paceman", "d2222");
        modelsMap.put("countryman", "d2098");
        modelsMap.put("roadster", "d2130");


        modelsMap.put("3000gt", "d415");
        modelsMap.put("diamante", "d416");
        modelsMap.put("eclipse", "d417");
        modelsMap.put("eclipse spyder", "d418");
        modelsMap.put("endeavor", "d419");
        modelsMap.put("galant", "d421");
        modelsMap.put("i-miev", "d2166");
        modelsMap.put("lancer", "d422");
        modelsMap.put("lancer evolution", "d423");
        modelsMap.put("lancer sportback", "d424");
        modelsMap.put("mirage", "d426");
        modelsMap.put("montero", "d427");
        modelsMap.put("montero sport", "d428");
        modelsMap.put("outlander", "d429");
        modelsMap.put("outlander sport", "d2093");
        modelsMap.put("raider", "d761");
        modelsMap.put("mighty max pickup", "d425");


        modelsMap.put("300zx", "d235");
        modelsMap.put("350z", "d236");
        modelsMap.put("370z", "d2018");
        modelsMap.put("altima", "d237");
        modelsMap.put("altima coupe", "d1038");
        modelsMap.put("armada", "d238");
        modelsMap.put("cube", "d1764");
        modelsMap.put("frontier", "d240");
        modelsMap.put("gt-r", "d1103");
        modelsMap.put("juke", "d2072");
        modelsMap.put("leaf", "d2077");
        modelsMap.put("maxima", "d242");
        modelsMap.put("murano", "d243");
        modelsMap.put("nv cargo", "d2188");
        modelsMap.put("nv passenger", "d2189");
        modelsMap.put("nv200", "d2200");
        modelsMap.put("pathfinder", "d245");
        modelsMap.put("quest", "d248");
        modelsMap.put("rogue", "d1047");
        modelsMap.put("rogue select", "d2273");
        modelsMap.put("sentra", "d249");
        modelsMap.put("titan", "d251");
        modelsMap.put("truck", "d955");
        modelsMap.put("versa", "d937");
        modelsMap.put("versa note", "d2234");
        modelsMap.put("xterra", "d253");
        modelsMap.put("200sx", "d233");
        modelsMap.put("240sx", "d234");
        modelsMap.put("280zx", "d1216");
        modelsMap.put("king cab", "d241");
        modelsMap.put("murano crosscabriolet", "d2151");
        modelsMap.put("pathfinder hybrid", "d2254");
        modelsMap.put("pickup", "d246");
        modelsMap.put("stanza", "d250");


        modelsMap.put("aztek", "d464");
        modelsMap.put("bonneville", "d465");
        modelsMap.put("firebird", "d466");
        modelsMap.put("g5", "d944");
        modelsMap.put("g6", "d467");
        modelsMap.put("g8", "d979");
        modelsMap.put("grand am", "d468");
        modelsMap.put("grand prix", "d469");
        modelsMap.put("gto", "d470");
        modelsMap.put("montana", "d472");
        modelsMap.put("montana sv6", "d473");
        modelsMap.put("solstice", "d737");
        modelsMap.put("sunfire", "d475");
        modelsMap.put("torrent", "d738");
        modelsMap.put("vibe", "d477");
        modelsMap.put("6000", "d463");
        modelsMap.put("catalina", "d1539");
        modelsMap.put("chieftain", "d1542");
        modelsMap.put("fiero", "d978");
        modelsMap.put("g3", "d2017");
        modelsMap.put("grand ville", "d2013");
        modelsMap.put("le mans", "d471");
        modelsMap.put("parisienne", "d1785");
        modelsMap.put("phoenix", "d1843");
        modelsMap.put("pursuit", "d1562");
        modelsMap.put("star chief", "d1541");
        modelsMap.put("sunbird", "d474");
        modelsMap.put("tempest", "d1806");
        modelsMap.put("trans sport", "d476");
        modelsMap.put("ventura", "d1459");


        modelsMap.put("911", "d404");
        modelsMap.put("918 spyder", "d2291");
        modelsMap.put("boxster", "d408");
        modelsMap.put("cayenne", "d410");
        modelsMap.put("cayman", "d993");
        modelsMap.put("macan", "d2261");
        modelsMap.put("panamera", "d1037");
        modelsMap.put("356", "d949");
        modelsMap.put("550 spyder", "d951");
        modelsMap.put("912", "d950");
        modelsMap.put("914", "d952");
        modelsMap.put("924", "d953");
        modelsMap.put("928", "d405");
        modelsMap.put("944", "d406");
        modelsMap.put("959", "d954");
        modelsMap.put("964", "d1399");
        modelsMap.put("968", "d407");
        modelsMap.put("carrera gt", "d409");


        modelsMap.put("1500", "d2110");
        modelsMap.put("2500", "d2102");
        modelsMap.put("3500", "d2103");
        modelsMap.put("3500 ram chassis", "d2226");
        modelsMap.put("c/v", "d2220");
        modelsMap.put("dakota", "d2105");
        modelsMap.put("promaster", "d2229");
        modelsMap.put("promaster city", "d2279");
        modelsMap.put("4500 ram chassis", "d2225");


        modelsMap.put("astra", "d1048");
        modelsMap.put("aura", "d938");
        modelsMap.put("ion", "d532");
        modelsMap.put("l-series", "d534");
        modelsMap.put("l300", "d535");
        modelsMap.put("outlook", "d929");
        modelsMap.put("relay", "d536");
        modelsMap.put("s-series", "d537");
        modelsMap.put("sky", "d939");
        modelsMap.put("vue", "d538");
        modelsMap.put("ion red line", "d533");


        modelsMap.put("fr-s", "d2140");
        modelsMap.put("ia", "d2320");
        modelsMap.put("im", "d2319");
        modelsMap.put("iq", "d2122");
        modelsMap.put("tc", "d433");
        modelsMap.put("xa", "d434");
        modelsMap.put("xb", "d435");
        modelsMap.put("xd", "d1033");


        modelsMap.put("b9 tribeca", "d847");
        modelsMap.put("baja", "d373");
        modelsMap.put("brz", "d2134");
        modelsMap.put("crosstrek", "d2387");
        modelsMap.put("crosstrek hybrid", "d2388");
        modelsMap.put("forester", "d374");
        modelsMap.put("impreza", "d375");
        modelsMap.put("impreza wrx", "d2233");
        modelsMap.put("impreza wrx sti", "d376");
        modelsMap.put("legacy", "d378");
        modelsMap.put("outback", "d380");
        modelsMap.put("tribeca", "d1039");
        modelsMap.put("wrx", "d2292");
        modelsMap.put("wrx sti", "d2341");
        modelsMap.put("xv crosstrek", "d2179");
        modelsMap.put("xv crosstrek hybrid", "d2255");
        modelsMap.put("gl", "d1793");
        modelsMap.put("loyale", "d379");
        modelsMap.put("svx", "d381");


        modelsMap.put("4runner", "d290");
        modelsMap.put("avalon", "d291");
        modelsMap.put("camry", "d292");
        modelsMap.put("camry solara", "d293");
        modelsMap.put("celica", "d294");
        modelsMap.put("corolla", "d295");
        modelsMap.put("echo", "d297");
        modelsMap.put("fj cruiser", "d826");
        modelsMap.put("highlander", "d298");
        modelsMap.put("highlander hybrid", "d757");
        modelsMap.put("land cruiser", "d299");
        modelsMap.put("matrix", "d300");
        modelsMap.put("mirai", "d2359");
        modelsMap.put("mr2 spyder", "d302");
        modelsMap.put("pickup", "d304");
        modelsMap.put("prius", "d15");
        modelsMap.put("prius c", "d2127");
        modelsMap.put("prius plug-in", "d2191");
        modelsMap.put("prius v", "d2150");
        modelsMap.put("rav4", "d306");
        modelsMap.put("rav4 hybrid", "d2318");
        modelsMap.put("sequoia", "d307");
        modelsMap.put("sienna", "d308");
        modelsMap.put("tacoma", "d311");
        modelsMap.put("tundra", "d313");
        modelsMap.put("venza", "d1516");
        modelsMap.put("yaris", "d827");
        modelsMap.put("corona", "d1443");
        modelsMap.put("cressida", "d296");
        modelsMap.put("fj40", "d1248");
        modelsMap.put("mr2", "d301");
        modelsMap.put("paseo", "d303");
        modelsMap.put("previa", "d305");
        modelsMap.put("supra", "d309");
        modelsMap.put("t100", "d310");
        modelsMap.put("tercel", "d312");


        modelsMap.put("beetle", "d201");
        modelsMap.put("cabrio", "d193");
        modelsMap.put("cc", "d2014");
        modelsMap.put("e-golf", "d2284");
        modelsMap.put("eos", "d915");
        modelsMap.put("golf", "d198");
        modelsMap.put("golf r", "d2131");
        modelsMap.put("golf sportwagen", "d2307");
        modelsMap.put("gti", "d199");
        modelsMap.put("jetta", "d200");
        modelsMap.put("jetta sportwagen", "d2094");
        modelsMap.put("passat", "d202");
        modelsMap.put("r32", "d204");
        modelsMap.put("rabbit", "d839");
        modelsMap.put("routan", "d1522");
        modelsMap.put("tiguan", "d1104");
        modelsMap.put("touareg", "d205");
        modelsMap.put("touareg 2", "d1045");
        modelsMap.put("cabriolet", "d194");
        modelsMap.put("corrado", "d195");
        modelsMap.put("eurovan", "d196");
        modelsMap.put("fox", "d197");
        modelsMap.put("gli", "d1519");
        modelsMap.put("karmann ghia", "d1540");
        modelsMap.put("microbus", "d913");
        modelsMap.put("phaeton", "d203");
        modelsMap.put("scirocco", "d912");
        modelsMap.put("super beetle", "d1971");
        modelsMap.put("thing", "d1583");
        modelsMap.put("type 2", "d1984");
        modelsMap.put("vanagon", "d206");


        modelsMap.put("c30", "d1043");
        modelsMap.put("c70", "d508");
        modelsMap.put("s40", "d510");
        modelsMap.put("s60", "d511");
        modelsMap.put("s60l", "d2334");
        modelsMap.put("s70", "d513");
        modelsMap.put("s80", "d514");
        modelsMap.put("v50", "d517");
        modelsMap.put("v60", "d2266");
        modelsMap.put("v70", "d518");
        modelsMap.put("xc60", "d1629");
        modelsMap.put("xc70", "d522");
        modelsMap.put("xc90", "d523");
        modelsMap.put("142", "d1931");
        modelsMap.put("240", "d501");
        modelsMap.put("740", "d502");
        modelsMap.put("760", "d503");
        modelsMap.put("780", "d504");
        modelsMap.put("850", "d505");
        modelsMap.put("940", "d506");
        modelsMap.put("960", "d507");
        modelsMap.put("coupe", "d509");
        modelsMap.put("s60 r", "d512");
        modelsMap.put("s90", "d515");
        modelsMap.put("v40", "d516");
        modelsMap.put("v70 r", "d519");
        modelsMap.put("v90", "d520");
        modelsMap.put("xc", "d521");


        modelsMap.put("4c", "d2277");
        modelsMap.put("spider", "d1149");


        modelsMap.put("hummer", "d134");


        modelsMap.put("db9", "d908");
        modelsMap.put("v8 vantage", "d910");
        modelsMap.put("db6", "d1440");
        modelsMap.put("db7", "d907");
        modelsMap.put("dbs", "d1301");
        modelsMap.put("rapide", "d2086");
        modelsMap.put("v12 vanquish", "d909");
        modelsMap.put("v12 vantage", "d2061");
        modelsMap.put("vanquish", "d2223");
        modelsMap.put("virage", "d1880");


        modelsMap.put("100", "d1600");
        modelsMap.put("100/6", "d1603");
        modelsMap.put("100m", "d1601");
        modelsMap.put("3000", "d1604");


        modelsMap.put("continental flying spur", "d34");
        modelsMap.put("continental gt", "d35");
        modelsMap.put("continental gtc", "d399");
        modelsMap.put("flying spur", "d2288");
        modelsMap.put("mulsanne", "d2051");
        modelsMap.put("arnage", "d397");
        modelsMap.put("azure", "d398");
        modelsMap.put("brooklands", "d2009");
        modelsMap.put("continental supersports", "d2167");
        modelsMap.put("s2", "d2381");
        modelsMap.put("s3", "d2171");
        modelsMap.put("turbo r", "d2025");


        modelsMap.put("sv-1", "d2178");


        modelsMap.put("veyron", "d1042");


        modelsMap.put("lanos", "d551");
        modelsMap.put("leganza", "d552");
        modelsMap.put("nubira", "d553");


        modelsMap.put("1500", "d1595");
        modelsMap.put("2000", "d1597");
        modelsMap.put("240z", "d1334");
        modelsMap.put("260z", "d1335");
        modelsMap.put("280z", "d1240");


        modelsMap.put("dmc-12", "d1291");


        modelsMap.put("summit", "d188");
        modelsMap.put("talon", "d189");
        modelsMap.put("vision", "d190");


        modelsMap.put("ranger", "d1696");


        modelsMap.put("360", "d437");
        modelsMap.put("458 italia", "d2064");
        modelsMap.put("california", "d2005");
        modelsMap.put("f430", "d443");
        modelsMap.put("250 gto", "d1841");
        modelsMap.put("308", "d1846");
        modelsMap.put("328", "d1451");
        modelsMap.put("330", "d1739");
        modelsMap.put("348", "d1218");
        modelsMap.put("365", "d1652");
        modelsMap.put("430 scuderia", "d1134");
        modelsMap.put("456m", "d438");
        modelsMap.put("512tr", "d1825");
        modelsMap.put("550", "d439");
        modelsMap.put("575m", "d440");
        modelsMap.put("599 gtb fiorano", "d959");
        modelsMap.put("612 scaglietti", "d441");
        modelsMap.put("dino 246", "d1205");
        modelsMap.put("enzo", "d442");
        modelsMap.put("f12berlinetta", "d2135");
        modelsMap.put("f355", "d1120");
        modelsMap.put("f40", "d1162");
        modelsMap.put("ff", "d2126");
        modelsMap.put("mondial", "d1570");
        modelsMap.put("superamerica", "d444");
        modelsMap.put("testarossa", "d1121");


        modelsMap.put("karma", "d1978");


        modelsMap.put("sprinter", "d2310");


        modelsMap.put("metro", "d314");
        modelsMap.put("prizm", "d315");
        modelsMap.put("spectrum", "d1481");
        modelsMap.put("storm", "d316");
        modelsMap.put("tracker", "d317");


        modelsMap.put("hornet", "d1996");


        modelsMap.put("h2", "d231");
        modelsMap.put("h2 sut", "d232");
        modelsMap.put("h3", "d843");
        modelsMap.put("h1", "d230");
        modelsMap.put("h1 alpha", "d842");
        modelsMap.put("h3t", "d1524");


        modelsMap.put("scout", "d1397");


        modelsMap.put("ascender", "d540");
        modelsMap.put("rodeo", "d546");
        modelsMap.put("trooper", "d549");
        modelsMap.put("amigo", "d539");
        modelsMap.put("axiom", "d541");
        modelsMap.put("hombre", "d542");
        modelsMap.put("i-series", "d851");
        modelsMap.put("oasis", "d544");
        modelsMap.put("pickup", "d545");
        modelsMap.put("rodeo sport", "d547");
        modelsMap.put("vehicross", "d550");


        modelsMap.put("interceptor", "d2343");


        modelsMap.put("gallardo", "d255");
        modelsMap.put("aventador", "d2125");
        modelsMap.put("countach", "d1163");
        modelsMap.put("diablo", "d254");
        modelsMap.put("espada", "d1768");
        modelsMap.put("huracan", "d2285");
        modelsMap.put("murcielago", "d256");


        modelsMap.put("evora", "d2058");
        modelsMap.put("elise", "d554");
        modelsMap.put("esprit", "d555");
        modelsMap.put("exige", "d852");


        modelsMap.put("57", "d207");
        modelsMap.put("62", "d208");


        modelsMap.put("650s", "d2300");
        modelsMap.put("675lt", "d2365");
        modelsMap.put("mp4-12c", "d2147");
        modelsMap.put("p1", "d2260");


        modelsMap.put("xr4ti", "d1731");


        modelsMap.put("mga", "d2211");
        modelsMap.put("mgb", "d1303");


        modelsMap.put("aero 8", "d1340");


        modelsMap.put("metropolitan", "d2391");


        modelsMap.put("alero", "d446");
        modelsMap.put("aurora", "d447");
        modelsMap.put("bravada", "d448");
        modelsMap.put("cutlass", "d451");
        modelsMap.put("cutlass supreme", "d454");
        modelsMap.put("eighty-eight", "d455");
        modelsMap.put("intrigue", "d457");
        modelsMap.put("silhouette", "d461");
        modelsMap.put("442", "d1383");
        modelsMap.put("achieva", "d445");
        modelsMap.put("ciera", "d449");
        modelsMap.put("custom cruiser", "d450");
        modelsMap.put("cutlass calais", "d452");
        modelsMap.put("cutlass ciera", "d453");
        modelsMap.put("delmont 88", "d1529");
        modelsMap.put("eighty-eight royale", "d456");
        modelsMap.put("lss", "d458");
        modelsMap.put("ninety-eight", "d459");
        modelsMap.put("regency", "d460");
        modelsMap.put("starfire", "d1782");
        modelsMap.put("toronado", "d462");
        modelsMap.put("vista cruiser", "d1565");


        modelsMap.put("110", "d1961");
        modelsMap.put("clipper", "d1847");


        modelsMap.put("esperante", "d133");


        modelsMap.put("grand voyager", "d481");
        modelsMap.put("neon", "d484");
        modelsMap.put("acclaim", "d478");
        modelsMap.put("barracuda", "d1050");
        modelsMap.put("belvedere", "d1859");
        modelsMap.put("breeze", "d479");
        modelsMap.put("caravelle", "d1385");
        modelsMap.put("colt", "d480");
        modelsMap.put("duster", "d1279");
        modelsMap.put("fury", "d1051");
        modelsMap.put("gtx", "d1061");
        modelsMap.put("horizon", "d482");
        modelsMap.put("prowler", "d485");
        modelsMap.put("reliant", "d1761");
        modelsMap.put("road runner", "d1062");
        modelsMap.put("satellite", "d1650");
        modelsMap.put("savoy", "d1954");
        modelsMap.put("scamp", "d1426");
        modelsMap.put("suburban", "d2327");
        modelsMap.put("sundance", "d486");
        modelsMap.put("superbird", "d1063");
        modelsMap.put("valiant", "d1485");
        modelsMap.put("volare", "d1820");
        modelsMap.put("voyager", "d487");


        modelsMap.put("ghost", "d2193");
        modelsMap.put("wraith", "d2213");
        modelsMap.put("corniche", "d411");
        modelsMap.put("park ward", "d412");
        modelsMap.put("phantom", "d413");
        modelsMap.put("phantom coupe", "d2117");
        modelsMap.put("phantom drophead coupe", "d956");
        modelsMap.put("silver cloud", "d1487");
        modelsMap.put("silver dawn", "d2267");
        modelsMap.put("silver ghost", "d1952");
        modelsMap.put("silver seraph", "d414");
        modelsMap.put("silver shadow", "d1828");
        modelsMap.put("silver spirit", "d1883");
        modelsMap.put("silver spur", "d2212");


        modelsMap.put("9-3", "d496");
        modelsMap.put("9-5", "d497");
        modelsMap.put("9-7x", "d498");
        modelsMap.put("9-2x", "d495");
        modelsMap.put("9-3 sportcombi", "d957");
        modelsMap.put("9-4x", "d2108");
        modelsMap.put("9-5 sportcombi", "d1021");
        modelsMap.put("900", "d499");
        modelsMap.put("9000", "d500");


        modelsMap.put("s7 twin turbo", "d1515");


        modelsMap.put("cobra", "d1226");


        modelsMap.put("fortwo", "d1040");


        modelsMap.put("viper", "d2158");


        modelsMap.put("champion", "d2250");
        modelsMap.put("commander", "d1478");
        modelsMap.put("lark", "d1756");


        modelsMap.put("aerio", "d257");
        modelsMap.put("forenza", "d259");
        modelsMap.put("grand vitara", "d260");
        modelsMap.put("kizashi", "d2070");
        modelsMap.put("reno", "d261");
        modelsMap.put("sx4", "d946");
        modelsMap.put("xl-7", "d268");
        modelsMap.put("equator", "d1523");
        modelsMap.put("esteem", "d258");
        modelsMap.put("sidekick", "d263");
        modelsMap.put("swift", "d264");
        modelsMap.put("verona", "d265");
        modelsMap.put("vitara", "d266");
        modelsMap.put("x-90", "d267");


        modelsMap.put("model s", "d2039");
        modelsMap.put("roadster", "d1041");


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
