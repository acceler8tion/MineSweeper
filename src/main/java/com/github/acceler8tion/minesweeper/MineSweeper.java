package com.github.acceler8tion.minesweeper;

import jdk.internal.org.jline.utils.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class MineSweeper<T> {

    private final int x;
    private final int y;
    private final int bomb;
    private final int size;
    private final Cell[][] table;
    private final MineSweeperMetaData<T> metaData;
    private final AtomicInteger leftTile;
    private final AtomicInteger leftBomb;
    private boolean die = false;

    private static final int[][] around = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private static final int EMPTY = 0;
    private static final int BOMB = 9;

    private MineSweeper(int x, int y, int bomb, MineSweeperMetaData<T> metaData){
        this.x = x;
        this.y = y;
        this.bomb = bomb;
        this.size = x * y;
        this.table = new Cell[y][x];
        this.metaData = metaData;
        this.leftTile = new AtomicInteger(x*y-bomb);
        this.leftBomb = new AtomicInteger(bomb);
    }

    public static <U> MineSweeper<U> create(int x, int y, int bomb, U owner) {
        HashMap<String, MineSweeperMetaData.Contribution<U>> conMap = new HashMap<>();
        float rate = ((float) bomb / (x * y)) * 100;
        MineSweeperMetaData<U> md = MineSweeperMetaData.create(null, owner, rate, conMap);

        return new MineSweeper<>(x, y, bomb, md);
    }

    //Utils
    private boolean safe(int x, int y) {
        return 0 <= x && x < this.x && 0 <= y && y < this.y;
    }
    private int[] split(int cod) {
        return new int[]{cod%x, cod/x};
    }

    private void accessAroundTiles(int x, int y, BiConsumer<Integer, Integer> accessor) {
        if(safe(x, y)) accessor.accept(x, y);
    }

    //Mains
    public void setMine(int x, int y){

        List<Integer> arr = new ArrayList<>();
        List<Integer> bombs;
        x = x-1;
        y = y-1;

        for(int i = 0; i < this.size; i++) arr.add(i);
        arr.remove(this.x * y + x);
        Collections.shuffle(arr);

        bombs = arr.subList(0, bomb);
        for(int c : bombs){
            int[] r = split(c);
            this.table[r[1]][r[0]].tile = BOMB;
            accessAroundTiles(r[0], r[1], (x2,y2) -> {
                if(!this.table[y2][x2].isBomb()) this.table[y2][x2].tile += 1;
            });
        }
    }

    public void open(int x, int y, boolean ignore, Runnable updateWhen){
        if(!safe(x, y)) return;

        Cell current = this.table[y][x];
        if(current.opened){
            if(0 < current.tile && current.tile < BOMB){
                AtomicInteger flags = new AtomicInteger();
                accessAroundTiles(x, y, (nx, ny) -> {
                    Cell current2 = this.table[ny][nx];
                    if(current2.flag && !current2.opened) flags.getAndIncrement();
                });
                if(flags.get() == current.tile){
                    this.table[y][x].opened = true;
                    updateWhen.run();
                    accessAroundTiles(x, y, (cx, cy) -> open(cx, cy, true, updateWhen));
                }
            }
        } else {
            if(current.isBomb()){
                if(current.flag) return;
                this.die = true;
            } else if(current.isEmpty()){
                leftTile.decrementAndGet();
                this.table[y][x].opened = true;
                updateWhen.run();
                accessAroundTiles(x, y, (cx, cy) -> open(cx, cy, true, updateWhen));
            } else {
                leftTile.decrementAndGet();
                this.table[y][x].opened = true;
                updateWhen.run();
            }
        }
    }

    public void setUpFlag(int x, int y){
        if(safe(x, y)) {
            Cell cur = this.table[y][x];
            if(!cur.isOpen()){
                boolean f = cur.flag;
                if(f){
                    this.table[y][x].flag = false;
                    leftBomb.incrementAndGet();
                } else {
                    this.table[y][x].flag = true;
                    leftBomb.decrementAndGet();
                }

            }
        }
    }

    public String display(MineSweeperDisplay mode, int fx, int fy, MineSweeperEmojiPack emp){
        StringBuilder dis = new StringBuilder();
        for (int i = 0; i < this.y; i++) {
            dis.append("\n");
            for (int j = 0; j < this.x; j++) {
                Cell current = this.table[i][j];
                switch (mode){
                    case ONGOING:
                        if(current.isOpen()) dis.append(emp.get(current.tile));
                        else {
                            if(current.flag) dis.append(emp.FLAG);
                            else dis.append(emp.UNOPEN);
                        }
                        break;
                    case DIED:
                        if(!current.isBomb() && current.isFlag()) dis.append(emp.WRONG);
                        else if(current.tile == BOMB && i == fy && j == fx) dis.append(emp.BOOM);
                        else {
                            if(current.isFlag()) dis.append(emp.FLAG);
                            else dis.append(emp.get(current.tile));
                        }
                        break;
                    case COMPLETED:
                        dis.append(current.isBomb() ? emp.FLAG : emp.get(current.tile));
                        break;
                }
            }
        }
        return dis.toString();
    }

    private static class Cell {

        protected int tile;
        protected boolean flag;
        protected boolean opened;

        Cell(int tile, boolean flag, boolean opened) {
            this.tile = tile;
            this.flag = flag;
            this.opened = opened;
        }

        protected boolean isOpen() {
            return opened;
        }

        protected boolean isFlag() {
            return flag;
        }

        protected boolean isBomb() {
            return tile == BOMB;
        }

        protected boolean isEmpty() {
            return tile == EMPTY;
        }
    }

}
