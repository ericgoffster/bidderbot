package bbidder;


public final class Constants {
    private Constants() {
    }

    public static final int CLUB = 0;
    public static final int DIAMOND = 1;
    public static final int HEART = 2;
    public static final int SPADE = 3;
    public static final int NOTRUMP = 4;

    public static final char CHR_CLUB = 'C';
    public static final char CHR_DIAMOND = 'D';
    public static final char CHR_HEART = 'H';
    public static final char CHR_SPADE = 'S';
    public static final char CHR_NOTRUMP = 'N';
    
    public static final char CHR_TEN = 'T';
    public static final char CHR_JACK = 'J';
    public static final char CHR_QUEEN = 'Q';
    public static final char CHR_KING = 'K';
    public static final char CHR_ACE = 'A';
    
    public static final int ACE = 12;
    public static final int KING = 11;
    public static final int QUEEN = 10;
    public static final int JACK = 9;
    public static final int TEN = 8;

    public static final String STR_CLUB = "C";
    public static final String STR_DIAMOND = "D";
    public static final String STR_HEART = "H";
    public static final String STR_SPADE = "S";
    public static final String STR_NOTRUMP = "N";
    
    public static final String STR_P = "P";
    public static final String STR_X = "X";
    public static final String STR_XX = "XX";

    public static final String STR_ALL_SUITS = "CDHSN";
    
    public static final short ALL_SUITS = (1 << CLUB) | (1 << DIAMOND) | (1 << HEART) | (1 << SPADE);
    public static final short MINORS = (1 << CLUB) | (1 << DIAMOND);
    public static final short MAJORS = (1 << HEART) | (1 << SPADE);
    public static final short REDS = (1 << HEART) | (1 << DIAMOND);
    public static final short BLACKS = (1 << SPADE) | (1 << CLUB);
    public static final short ROUND = (1 << HEART) | (1 << CLUB);
    public static final short POINTED = (1 << SPADE) | (1 << DIAMOND);
}
