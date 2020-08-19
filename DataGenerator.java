import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    private static final DataGenerator instance = new DataGenerator();
    private static Random rand=new Random();
    public static String latinString="QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
    public static String russianString="ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁйцукенгшщзхъфывапролджэячсмитьбюё";

    private DataGenerator() {
    }

    public static DataGenerator getInstance() {
        return instance;
    }

    private static  String getRandomDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        Date date=new Date(randomMillisSinceEpoch);
        StringBuilder sb=new StringBuilder();
        sb.append(date.getDay()+1).append(".").append(date.getMonth()+1).append(".").append(date.getYear()+1900);
        return sb.toString();
    }

    private static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private static String getFraction(){
        StringBuilder sb=new StringBuilder();
        sb.append(randInt(1, 20)).append(".");
        for(int i=0; i<8; i++){
            sb.append(randInt(0, 9));
        }
        return sb.toString();
    }

    private static String getRandomAlphaString(String alphaString)
    {
        // chose a Character random from alphaString
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(alphaString
                    .charAt(randInt(0, alphaString.length()-1 )));
        }

        return sb.toString();
    }

    public static String getGeneratedString(){
        StringBuilder sb=new StringBuilder();
        sb.append(getRandomDate(new Date(115, 8, 7),
                new Date(120,8, 7)));
        sb.append("-");
        sb.append(getRandomAlphaString(latinString));
        sb.append("-");
        sb.append(getRandomAlphaString(russianString));
        sb.append("-");
        sb.append(randInt(1, 50000000)*2);
        sb.append("-");
        sb.append(getFraction());
        return sb.toString();
    }
}
