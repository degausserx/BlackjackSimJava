package net.codejack.bjstats2.simulation;

import android.util.Log;

import net.codejack.bjstats2.settings.ExecutionSettings;
import net.codejack.bjstats2.settings.HouseStrategy;
import net.codejack.bjstats2.settings.PlayerStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Degausser on 7/3/2017.
 */

public class Simulation {

    private HashMap<String, HashMap<Integer, String>> playerStrategy;
    private LinkedList<Integer> deck;
    private ArrayList<Integer> deckSource;

    private int player1 = 0;
    private int player2 = 0;
    private int dealer = 0;

    private boolean draw17;
    private boolean holecard;
    private boolean doubleonsoft;
    private boolean splitacesone;
    private boolean splitacesnobjs;
    private boolean resplitaces;
    private boolean surrender;
    private boolean surrendervsace;
    private boolean surrenderearly;
    private int decks;
    private int maxsplits;

    private int games = 0;
    private int hands = 0;
    private int dealer_bust = 0;
    private int dealer_lose = 0;
    private int dealer_win = 0;
    private int units_won = 0;
    private int units_lost = 0;
    private int blackjacks = 0;
    private int dealer_draw = 0;

    public Simulation(PlayerStrategy p, HouseStrategy h, ExecutionSettings e) {
        playerStrategy = p.getMap(); // done

        String player1 = e.getPlayer1();
        String player2 = e.getPlayer2();
        String dealer = e.getDealer();

        if (player1.equals("A")) player1 = "11";
        if (player2.equals("A")) player2 = "11";
        if (dealer.equals("A")) dealer = "11";

        if (e.getPlayer1().length() > 0) this.player1 = Integer.parseInt(player1); // done
        if (e.getPlayer2().length() > 0) this.player2 = Integer.parseInt(player2); // done
        if (e.getDealer().length() > 0) this.dealer = Integer.parseInt(dealer); // done

        draw17 = h.getDraw17(); // done
        holecard = h.getHolecard(); // done
        doubleonsoft = h.getDoubleonsoft(); // done
        splitacesone = h.getSplitacesone(); // done
        splitacesnobjs = h.getSplitacesnobjs(); // done
        resplitaces = h.getResplitaces(); // done
        surrender = h.getSurrender(); // done
        surrendervsace = h.getSurrendervsace(); // done
        surrenderearly = h.getSurrenderearly(); // done
        decks = h.getDecks(); // done
        maxsplits = h.getMaxsplits(); // done

        deckSource = new ArrayList<>();
        deck = new LinkedList<>();

        int i = 0, cards = decks * 52, n;
        while (i < cards) {
            n = i % 13 + 2;
            if (n > 11) n = 10;
            deckSource.add(n);
            i++;
        }

        // remove set cards from the source, because searching through a linked list by value can take time
        if (this.player1 != 0) deckSource.remove(deckSource.indexOf(this.player1));
        if (this.player2 != 0) deckSource.remove(deckSource.indexOf(this.player2));
        if (this.dealer != 0) deckSource.remove(deckSource.indexOf(this.dealer));
    }

    public int getGames() {
        return games;
    }

    public int getHands() {
        return hands;
    }

    public int getDealer_bust() {
        return dealer_bust;
    }

    public int getDealer_lose() {
        return dealer_lose;
    }

    public int getDealer_win() {
        return dealer_win;
    }

    public double getUnits_won() {
        return units_won / 2.0;
    }

    public double getUnits_lost() {
        return units_lost / 2.0;
    }

    public int getBlackjacks() { return blackjacks; }

    public int getDealer_draw() { return dealer_draw; }

    public boolean getBlackjackOnSplit() { return !splitacesnobjs; }

    // simulation
    public void deal() {
        int dealer_hand, dealer_finish;
        if (dealer != 0) dealer_hand = dealer;
        else dealer_hand = getCard(true);

        dealer_finish = getDealerHand(dealer_hand);

        playerSimulate(0, dealer_hand, dealer_finish, 0, true);
    }

    private int getCard(boolean start) {
        if ((start && deck.size() < 25) || deck.size() < 1) {
            shuffleCards();
        }
        //int random = (new Random()).nextInt(deck.size());
        int random = ThreadLocalRandom.current().nextInt(0, deck.size());
        int r = deck.get(random);
        deck.remove(random);
        return r;
    }

    private void shuffleCards() {
        deck = new LinkedList<>();
        deck.addAll(deckSource);
    }

    private int playerSimulate(int player, int dealer, int dealer_finish, int split, boolean entry) {
        int new_card, hit = 0, cards = (entry) ? 0 : 1;
        boolean blackjack = false, doublebet = false, pair, soft = false,
                allow_split = true, looping = true;

        // start main loop
        while (looping) {
            // mark false in anticipation of holding one card and relooping because of a split hand
            pair = false;
            looping = false;

            // get first 2 default cards
            new_card = (split < 1 && player1 > 0) ? player1 : getCard(false);
            cards++;
            if (cards == 1) {
                player = (split < 1 && player2 > 0) ? player2 : getCard(false);
                cards++;
            }

            if (player == 11 || new_card == 11) {
                soft = true;
            }
            if (player == new_card) {
                pair = true;
                if (player == 11 && split > 0) {
                    if (!resplitaces) allow_split = false;
                }
                if (split >= maxsplits && maxsplits != 0) allow_split = false;
            }
            new_card += player;
            if (new_card == 21) {
                if (split == 0 || !splitacesnobjs) blackjack = true;
                player = new_card;
                break;
            }
            if (player == 11 && split > 0 && splitacesone) {
                if (new_card != 22 || (new_card == 22 && !allow_split)) {
                    player = (new_card == 22) ? 12 : new_card;
                    break;
                }
            }
            player = (new_card == 22) ? 12 : new_card;

            // if no blackjack..
            if (!blackjack) {
                String playerString, strategy;
                int num;
                if (holecard && dealer_finish == 35 && (!surrender || (surrender && !surrenderearly))) break;
                while (player < 21) {

                    // clean a few things, build the playerString String to help make a call for the strategy
                    if (cards > 2) pair = false;
                    if (pair && allow_split) {
                        if (soft) playerString = "AA";
                        else if (player == 20) playerString = "TT";
                        else {
                            num = player / 2;
                            playerString = "" + num + num;
                        }
                    }
                    else if (soft) {
                        playerString = (player == 12) ? "AAA" : "A" + (player - 11);
                    }
                    else playerString = "" + player;

                    // make a call for the strategy. 'dealer' is an int
                    strategy = (!playerString.equals("AAA")) ? playerStrategy.get(playerString).get(dealer) : "H";

                    // if the move is "surrender" or "surrender, otherwise stand"
                    if (strategy.equals("A") || strategy.equals("B")) {
                        if (cards == 2 && surrender && split == 0 && (surrendervsace || dealer != 11)) {
                            player = -5;
                            break;
                        }
                        if (strategy.equals("B")) break;
                        hit = 1;
                    }

                    // if the dealer has blackjack, after the chance to surrender, auto lose
                    if (cards == 2 && dealer_finish == 35 && holecard) break;

                    // if the move is "no card"
                    if (strategy.equals("X")) break;

                    // splitting 2 cards recursively
                    if (strategy.equals("S")) {
                        if (soft) player = 11;
                        else player = player / 2;
                        split = playerSimulate(player, dealer, dealer_finish, ++split, false);
                        cards--;
                        looping = true;
                        break;
                    }

                    // double the bet
                    if (strategy.equals("D") || strategy.equals("E")) {
                        if ((soft && !doubleonsoft) || cards > 2 || (split > 0 && player == 12 && soft)) {
                            if (strategy.equals("E")) break;
                            else hit = 1;
                        }
                        else hit = 2;
                    }

                    // if the move is taking a card
                    if (strategy.equals("H") || hit > 0) {
                        new_card = getCard(false);
                        if (new_card == 11) {
                            if (player > 10) new_card = 1;
                            else soft = true;
                        }
                        player += new_card;

                        if (player > 21 && soft) {
                            soft = false;
                            player -= 10;
                        }
                        if (cards == 2 && hit == 2 && (!soft || (soft && doubleonsoft))) {
                            doublebet = true;
                        }

                        cards++;
                        hit = 0;
                    }
                    if (doublebet) break;
                }
            }
        }

        dealer = dealer_finish;

        // apply stats
        if (blackjack) {
            player = 35;
            blackjacks++;
        }

        if (dealer > 21 && dealer != 35) {
            dealer = 22;
            if (entry) dealer_bust++;
        }
        if (player > 21 && player != 35) player = 22;

        if (entry) games++;
        hands++;

        if (((player < dealer) && (dealer != 22)) || player == 22 || player == -5) {
            // lose
            dealer_win++;
            if (doublebet) units_lost += 4;
            else if (player == -5) units_lost += 1;
            else units_lost += 2;
        }
        else if (player == dealer) {
            // draw
            dealer_draw++;
        }

        else {
            // win
            dealer_lose++;
            if (player == 35) units_won += 3;
            else if (doublebet) units_won += 4;
            else units_won += 2;
        }

        return split;
    }

    private int getDealerHand(int dealer) {
        boolean soft = false, start = true;
        int card;
        if (dealer == 11) soft = true;

        // at least one additional card
        do {
            card = getCard(false);
            if (card == 11) {
                if (dealer < 11) {
                    dealer += 11;
                    soft = true;
                }
                else dealer++;
            }
            else {
                dealer += card;
                if (soft && dealer > 21) {
                    dealer -= 10;
                    soft = false;
                }
            }
            if (start && dealer == 21) return 35;
            start = false;
        } while (dealer < 17 || (draw17 && dealer == 17 && soft));

        return dealer;
    }

}