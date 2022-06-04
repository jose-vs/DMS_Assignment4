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
public class UserPK implements Serializable {

    public String username;

    public UserPK(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserPK)) {
            return false;
        } else {
            boolean isEqual = false;
            UserPK other = (UserPK) obj;

            if ((username != null) && (other.username != null)) {
                isEqual = username.equals(other.username);
            }

            return isEqual;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (username != null) {
            hashCode ^= username.hashCode();
        }
        return hashCode;
    }

}
