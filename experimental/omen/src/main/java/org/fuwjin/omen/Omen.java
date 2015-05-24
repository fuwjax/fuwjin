package org.fuwjin.omen;

import org.fuwjin.game.Pile;
import org.fuwjin.game.Pool;

/**
 * Created by fuwjax on 2/27/15.
 */
public class Omen {
    public static void main(String... args) throws Exception {
        Pile<Card> deck = loadUnits();
        Pile<Card> discard = Pile.emptyPile();
        Pool<Coin> coins = new Pool<>(25, Coin.INSTANCE);
        deck.shuffle();
        Player player1 = new Player(deck.draw(4), coins.draw(4, Pool.remaining()), loadFeats());
    }

    private static Pile<Card> loadFeats() {
    }

    private static Pile<Card> loadUnits() {
        return new Pile<>();
    }
}
