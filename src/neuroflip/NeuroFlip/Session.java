/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package neuroflip.NeuroFlip;

/**
 *
 * @author Ramim
 */
public class Session {
    private static String loggedInUsername;

    public static void setUsername(String Username) {
        loggedInUsername = Username;
    }

    public static String getUsername() {
        return loggedInUsername;
    }
}
