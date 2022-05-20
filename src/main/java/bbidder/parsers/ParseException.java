package bbidder.parsers;

public final class ParseException extends Exception {
    private static final long serialVersionUID = 1L;
    final String where;

    public ParseException(String where, Throwable e) {
        super(where + ":" + e.getMessage(), e);
        this.where = where;
    }
}
