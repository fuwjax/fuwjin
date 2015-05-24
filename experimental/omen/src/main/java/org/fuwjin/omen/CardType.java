package org.fuwjin.omen;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Created by fuwjax on 2/27/15.
 */
public enum CardType implements Predicate<Card> {
    Soldier,
    Beast,
    Oracle,
    Spirit,
    Hero,
    Reward,
    Feat,
    Challenge,
    Relic,
    Whim;
    public static EnumSet<CardType> Units = EnumSet.of(Soldier, Beast, Oracle, Spirit, Hero);

    @Override
    public boolean test(Card card) {
        return equals(card.type());
    }
}
