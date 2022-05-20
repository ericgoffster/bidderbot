package bbidder.parsers;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;
import java.util.function.Predicate;

public final class Input implements Closeable {
    public final Reader rd;
    public final Stack<Integer> unread = new Stack<>();
    public int ch;

    public Input(Reader rd) throws IOException {
        super();
        this.rd = rd;
        this.ch = this.rd.read();
    }

    public void unread(int ch) throws IOException {
        unread.push(this.ch);
        this.ch = ch;
    }
    
    public void unread(String str)  throws IOException {
        for(int i = str.length() - 1; i >= 0; i--) {
            unread(str.charAt(i));
        }
    }

    public String readAny(Predicate<Character> pred) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (ch >= 0 && pred.test((char) ch)) {
            sb.append((char) ch);
            advance();
        }
        return sb.toString();
    }

    public String readToken(Predicate<Character> pred) throws IOException {
        advanceWhite();
        return readAny(ch -> !Character.isWhitespace(ch) && pred.test((char) ch));
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
        if (unread.size() > 0) {
            ch = unread.pop();
        } else {
            ch = rd.read();
        }
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
