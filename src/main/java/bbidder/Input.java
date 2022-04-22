package bbidder;

import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.function.Predicate;

public final class Input implements Closeable {
    public final PushbackReader rd;
    public int ch;

    public Input(Reader rd) throws IOException {
        super();
        this.rd = new PushbackReader(rd);
        this.ch = this.rd.read();
    }

    public void unread(int ch) throws IOException {
        rd.unread(this.ch);
        this.ch = ch;
    }

    public String readToken(Predicate<Character> pred) throws IOException {
        advanceWhite();
        StringBuilder sb = new StringBuilder();
        while (ch >= 0 && !Character.isWhitespace(ch) && pred.test((char) ch)) {
            sb.append((char) ch);
            advance();
        }
        return sb.toString();
    }

    public boolean readKeyword(String key) throws IOException {
        StringBuilder read = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            if (Character.toUpperCase(ch) == key.charAt(i)) {
                read.append((char) ch);
                advance();
            } else {
                for (int j = read.length() - 1; j >= 0; j--) {
                    unread(read.charAt(j));
                }
                return false;
            }
        }
        return true;
    }

    public void advance() throws IOException {
        ch = rd.read();
    }

    public void advanceWhite() throws IOException {
        while (Character.isWhitespace(ch)) {
            advance();
        }
    }

    @Override
    public void close() throws IOException {
        rd.close();
    }
}
