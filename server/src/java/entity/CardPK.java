/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author jcvsa
 */
public class CardPK implements Serializable {

    public String id;

    public CardPK(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null || !(obj instanceof CardPK))) {
            return false;
        } else {
            CardPK other = (CardPK) obj;
            if (id != null && other.id != null) {
                return id.equals(other.id);
            }
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (id != null) {
            hashCode ^= id.hashCode();
        }
        return hashCode;
    }
}
