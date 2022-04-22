package bbidder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import bbidder.utils.SplitUtil;

/**
 * A immutable list of bids guaranteed to represent a valid auction.
 * 
 * @author goffster
 *
 */
public final class Auction {
    private final List<Bid> bids;

    private Auction(List<Bid> bids) {
        super();
        this.bids = bids;
    }

    /**
     * @return the immutable list of bids.
     */
    public List<Bid> getBids() {
        return Collections.unmodifiableList(bids);
    }

    /**
     * Creates an auction from a list of bids.
     * 
     * @param bids
     *            The list of bids.
     * @return The new auction.
     * @throws IllegalArgumentException
     *             If the auction is not valid.
     */
    public static Auction create(List<Bid> bids) {
        Auction bl = new Auction(List.of());
        for (Bid b : bids) {
            bl = bl.withBidAdded(b);
        }
        return bl;
    }

    /**
     * 
     * @param bid
     *            The bid to test
     * @return true if the bid represents a legal bid in the auction
     */
    public boolean isLegalBid(Bid bid) {
        Contract contract = getContract();
        if (contract.winningBid != Bid.P && bid.isSuitBid() && bid.ordinal() < contract.winningBid.ordinal()) {
            return false;
        }

        if (bid == Bid.XX) {
            if (contract.redoubled) {
                return false;
            }
            if (!contract.doubled) {
                return false;
            }
            if (contract.position % 2 != bids.size() % 2) {
                return false;
            }
        }
        if (bid == Bid.X) {
            if (contract.redoubled || contract.doubled) {
                return false;
            }
            if (contract.winningBid == Bid.P) {
                return false;
            }
            if (contract.position % 2 == bids.size() % 2) {
                return false;
            }
        }

        if (contract.isCompleted()) {
            return false;
        }
        return true;
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new bid list with the given bid added
     * @throws IllegalArgumentException
     *             If the auction is not valid.
     */
    public Auction withBidAdded(Bid bid) {
        if (!isLegalBid(bid)) {
            throw new IllegalArgumentException("Invalid bid '" + bid + "'");
        }
        List<Bid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new Auction(newBids);
    }

    /**
     * @return the current contract. (returning a contract of "P" if no suit bids have been made)
     */
    public Contract getContract() {
        boolean redoubled = false;
        boolean doubled = false;
        int numPasses = 0;
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid bid = bids.get(i);
            if (bid.isSuitBid()) {
                return new Contract(i, bid, doubled, redoubled, numPasses);
            }
            if (bid == Bid.X) {
                doubled = true;
            }
            if (bid == Bid.XX) {
                redoubled = true;
            }
            if (bid == Bid.P) {
                if (!doubled && !redoubled) {
                    numPasses++;
                }
            }
        }
        return new Contract(0, Bid.P, false, false, numPasses);
    }

    /**
     * @return The last bid in the sequence. null if empty.
     */
    public Bid getLastBid() {
        if (bids.isEmpty()) {
            return null;
        }
        return bids.get(bids.size() - 1);
    }

    /**
     * @return An auction of all bids except the last bid. No-op if already empty.
     */
    public Auction exceptLast() {
        if (bids.isEmpty()) {
            return this;
        }
        return new Auction(bids.subList(0, bids.size() - 1));
    }

    /**
     * @param n
     *            The number of bids to return
     * @return An auction of just the first "n" bids,
     */
    public Auction firstN(int n) {
        if (bids.size() < n) {
            return this;
        }
        return new Auction(bids.subList(0, n));
    }

    /**
     * @param str
     *            The string to parse
     * @return A bid list parsed from the string
     * @throws IllegalArgumentException
     *             If the auction is not valid or bids cant be parsed.
     */
    public static Auction valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+");
        List<Bid> bids = new ArrayList<>();
        boolean we = false;
        boolean first = true;
        for (String part : parts) {
            if (part.startsWith("(") && part.endsWith(")")) {
                if (!first && !we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part.substring(1, part.length() - 1));
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = false;
            } else {
                if (!first && we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part);
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = true;
            }
            first = false;
        }
        return create(bids);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (Bid bid : bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Auction other = (Auction) obj;
        return Objects.equals(bids, other.bids);
    }
}
