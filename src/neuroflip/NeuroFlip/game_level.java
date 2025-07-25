/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package neuroflip.NeuroFlip;

/**
 *
 * @author Ramim
 */
public class game_level {
    private static int hard = 4;
    private static int normal = 3;
    private static int easy = 2;

    public static void setHard(int value) {
        hard = value;
    }

    public static int getHard() {
        return hard;
    }

    public static void setNormal(int value) {
        normal = value;
    }

    public static int getNormal() {
        return normal;
    }

    public static void setEasy(int value) {
        easy = value;
    }

    public static int getEasy() {
        return easy;
    }
}
