package pl.mnykolaichuk.trade.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor @Builder
public class BuyOfferDto implements Comparable<BuyOfferDto> {
    private Long buyOfferId;
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double maxPrice;
    private Long timestamp;
    private Boolean actual;

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param buyOfferDto the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@NotNull BuyOfferDto buyOfferDto) {

        // Спочатку порівнюємо за ціною
        int priceComparison = Double.compare(this.maxPrice, buyOfferDto.maxPrice);
        if (priceComparison != 0) {
            return priceComparison;
        }
        // Якщо ціна однакова, порівнюємо за timestamp (старіші мають вищий пріоритет)
        return Long.compare(this.timestamp, buyOfferDto.timestamp);
    }

}
