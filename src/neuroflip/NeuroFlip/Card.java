/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package neuroflip.NeuroFlip;

/**
 *
 * @author Ramim
 */
public class Card {
    private int cardID;
    private String cardname;

    public Card(int cardID, String cardname) {
        this.cardID = cardID;
        this.cardname = cardname;
    }

    public int getCardID() {
        return cardID;
    }

    public String getCardname() {
        return cardname;
    }
}
