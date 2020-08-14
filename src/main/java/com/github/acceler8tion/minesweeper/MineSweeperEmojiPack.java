package com.github.acceler8tion.minesweeper;

public class MineSweeperEmojiPack {
    
    public final String EMPTY;
    public final String ONE;
    public final String TWO;
    public final String THREE;
    public final String FOUR;
    public final String FIVE;
    public final String SIX;
    public final String SEVEN;
    public final String EIGHT;
    public final String BOMB;
    public final String FLAG;
    public final String UNOPEN;
    public final String BOOM;
    public final String WRONG;
    
    MineSweeperEmojiPack(String empty, String one, String two, String three, String four, String five, String six, 
                         String seven, String eight, String bomb, String flag, String unopen, String boom, String wrong) {
        
        this.EMPTY = empty;
        this.ONE = one;
        this.TWO = two;
        this.THREE = three;
        this.FOUR = four;
        this.FIVE = five;
        this.SIX = six;
        this.SEVEN = seven;
        this.EIGHT = eight;
        this.BOMB = bomb;
        this.FLAG = flag;
        this.UNOPEN = unopen;
        this.BOOM = boom;
        this.WRONG = wrong;
        
    }

    public String get(int num) {
        String em;
        switch (num) { //it succccccccccccccccccccccccccccccks
            case 0:
                em = EMPTY;
                break;
            case 1:
                em = ONE;
                break;
            case 2:
                em = TWO;
                break;
            case 3:
                em = THREE;
                break;
            case 4:
                em = FOUR;
                break;
            case 5:
                em = FIVE;
                break;
            case 6:
                em = SIX;
                break;
            case 7:
                em = SEVEN;
                break;
            case 8:
                em = EIGHT;
                break;
            case 9:
                em = BOMB;
                break;
            default:
                em = "";
        }
        return em;
    }
}
