package bbidder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.generalities.AndGenerality;
import bbidder.generalities.FitEstablished;
import bbidder.generalities.IAmTwoSuitedGenerality;
import bbidder.generalities.IBidSuitGenerality;
import bbidder.generalities.PartnerBidSuitGenerality;
import bbidder.generalities.PartnerIsTwoSuitedGenerality;
import bbidder.generalities.TrueGenerality;
import bbidder.generalities.UnbidSuitGenerality;
import bbidder.generalities.WeAreThreeSuited;

/**
 * The registry of all possible inferences.
 *
 * @author goffster
 *
 */
public final class GeneralityParser {
    public static final Pattern FIT_ESTABLISHED = Pattern.compile("fit_established\\s+(.*)");
    public static final Pattern I_AM_2SUITED = Pattern.compile("i_am_two_suited\\s+(.*)\\s+(.*)");
    public static final Pattern P_IS_2SUITED = Pattern.compile("partner_is_two_suited\\s+(.*)\\s+(.*)");
    public static final Pattern I_BID_SUIT = Pattern.compile("i_bid_suit\\s+(.*)");
    public static final Pattern P_BID_SUIT = Pattern.compile("partner_bid_suit\\s+(.*)");
    public static final Pattern WE_ARE_3SUITED = Pattern.compile("we_are_three_suited");
    public static final Pattern UNBID_SUIT = Pattern.compile("unbid_suit\\s+(.*)");


    public static Generality parseGenerality(String str) {
        if (str == null) {
            return null;
        }
        int pos = str.indexOf(",");
        if (pos >= 0) {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos + 1);
            return AndGenerality.create(parseGenerality(str1), parseGenerality(str2));
        }
        if (str.trim().equals("") || str.trim().equals("true")) {
            return TrueGenerality.T;
        }
        {
            Matcher m = FIT_ESTABLISHED.matcher(str.trim());
            if (m.matches()) {
                Symbol symbol = SymbolParser.parseSymbol(m.group(1));
                if (symbol != null) {
                    return new FitEstablished(symbol);
                }
            }
        }
        {
            Matcher m = I_AM_2SUITED.matcher(str.trim());
            if (m.matches()) {
                Symbol longer = SymbolParser.parseSymbol(m.group(1));
                Symbol shorter = SymbolParser.parseSymbol(m.group(2));
                if (longer != null && shorter != null) {
                    return new IAmTwoSuitedGenerality(longer, shorter);
                }
            }
        }
        {
            Matcher m = I_BID_SUIT.matcher(str.trim());
            if (m.matches()) {
                Symbol symbol = SymbolParser.parseSymbol(m.group(1));
                if (symbol != null) {
                    return new IBidSuitGenerality(symbol);
                }
            }
        }
        {
            Matcher m = P_BID_SUIT.matcher(str.trim());
            if (m.matches()) {
                Symbol symbol = SymbolParser.parseSymbol(m.group(1));
                if (symbol != null) {
                    return new PartnerBidSuitGenerality(symbol);
                }
            }
        }
        {
            Matcher m = P_IS_2SUITED.matcher(str.trim());
            if (m.matches()) {
                Symbol longer = SymbolParser.parseSymbol(m.group(1));
                Symbol shorter = SymbolParser.parseSymbol(m.group(2));
                if (longer != null && shorter != null) {
                    return new PartnerIsTwoSuitedGenerality(longer, shorter);
                }
            }
        }
        {
            Matcher m = WE_ARE_3SUITED.matcher(str.trim());
            if (m.matches()) {
                return new WeAreThreeSuited();
            }
        }
        {
            Matcher m = UNBID_SUIT.matcher(str.trim());
            if (m.matches()) {
                Symbol symbol = SymbolParser.parseSymbol(m.group(1));
                if (symbol != null) {
                    return new UnbidSuitGenerality(symbol);
                }
            }
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
