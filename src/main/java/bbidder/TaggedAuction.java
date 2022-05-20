package bbidder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TaggedAuction {
    private final List<TaggedBid> bids;

    private TaggedAuction(List<TaggedBid> bids) {
        super();
        this.bids = bids;
    }

    /**
     * @return the immutable list of bids.
     */
    public List<TaggedBid> getBids() {
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
    public static TaggedAuction create(List<TaggedBid> bids) {
        TaggedAuction bl = new TaggedAuction(List.of());
        for (TaggedBid b : bids) {
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
    public TaggedAuction withBidAdded(TaggedBid bid) {
        if (!isLegalBid(bid.bid)) {
            throw new IllegalArgumentException("Invalid bid '" + bid + "'");
        }
        List<TaggedBid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new TaggedAuction(newBids);
    }

    /**
     * @return the current contract. (returning a contract of "P" if no suit bids have been made)
     */
    public Contract getContract() {
        int start = 0;
        for (int i = bids.size() - 1; i >= 0; i--) {
            TaggedBid bid = bids.get(i);
            if (bid.bid.isSuitBid()) {
                start = i;
                break;
            }
        }
        Contract contract = Contract.FIRST;
        for (int j = start; j < bids.size(); j++) {
            contract = contract.addBid(bids.get(j).bid);
        }
        return contract;
    }

    /**
     * @param n
     *            The number of bids to return
     * @return An auction of just the first "n" bids,
     */
    public TaggedAuction firstN(int n) {
        if (bids.size() < n) {
            return this;
        }
        return new TaggedAuction(bids.subList(0, n));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (TaggedBid bid : bids) {
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
        TaggedAuction other = (TaggedAuction) obj;
        return Objects.equals(bids, other.bids);
    }
}
