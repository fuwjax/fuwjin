package org.fuwjin.omen;

/**
 * Created by fuwjax on 2/27/15.
 */
public class Card {
    private CardType type;

    public boolean isUnit(){
        return CardType.Units.contains(type);
    }

    public CardType type() {
        return type;
    }
}
