package bbidder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return getContract().isLegalBid(bid);
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
        int start = 0;
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid bid = bids.get(i);
            if (bid.isSuitBid()) {
                start = i;
                break;
            }
        }
        Contract contract = Contract.FIRST;
        for (int j = start; j < bids.size(); j++) {
            contract = contract.addBid(bids.get(j));
        }
        return contract;
    }

    /**
     * @return The last bid in the sequence. empty if the list of bids is empty.
     */
    public Optional<Bid> getLastBid() {
        if (bids.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bids.get(bids.size() - 1));
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
