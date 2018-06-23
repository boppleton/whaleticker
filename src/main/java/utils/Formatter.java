package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    // d-bo'd from Ilya Saunkin via https://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
    public static String kFormat(double n, int iteration) {

        if (n < 1000) {
            return lowFormat(n);
        }

        String[] c = new String[]{"k", "mil"};

        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : kFormat(d, iteration + 1));
    }

    public static String lowFormat(double size) {

        return String.format("%.0f", Math.abs(Math.rint(size)));
    }

    public static String amountFormat(int minimumTradeAmt) {

        return NumberFormat.getNumberInstance(Locale.getDefault()).format(minimumTradeAmt);
    }
}
