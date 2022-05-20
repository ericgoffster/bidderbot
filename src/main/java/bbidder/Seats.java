package bbidder;

import java.util.Objects;

public class Seats {
    public static final Seats NONE = new Seats(0);
    public static final Seats ALL = new Seats(0xf);
    public final int seats;

    private Seats(int seats) {
        super();
        this.seats = seats;
    }
    
    public Seats addSeat(int seat) {
        return new Seats(seats | (1 << seat));
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(seats);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Seats other = (Seats) obj;
        return seats == other.seats;
    }

    public boolean hasSeat(int s) {
        return (seats & (1 << s)) != 0; 
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int s = 0; s < 4; s++) {
            if (hasSeat(s)) {
                sb.append(String.valueOf(s + 1));
            }
        }
        return "seats" + sb;
    }
}
