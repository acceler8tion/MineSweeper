package com.github.acceler8tion.minesweeper;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MineSweeperMetaData<T> {

    private String id;
    private final T owner;
    private final float rate;
    private long start;
    private long endAt;
    private final HashMap<String, Contribution<T>> participants;

    private MineSweeperMetaData(String id, T owner, float rate, HashMap<String, Contribution<T>> participants) {
        this.id = id;
        this.owner = owner;
        this.rate = rate;
        this.participants = participants;
    }

    public static <U> MineSweeperMetaData<U> create(String id, U owner, float rate, HashMap<String, Contribution<U>> participants) {
        return new MineSweeperMetaData<>(id, owner, rate, participants);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public HashMap<String, Contribution<T>> getPlayers() {
        return participants;
    }

    public Contribution<T> getPlayerById(String id) {
        return participants.get(id);
    }

    public void putPlayers(String id, T user) {
        Contribution<T> con = Contribution.create(user);
        participants.put(id, con);
    }
    
    public void updateContribution(String id, Contribution<T> con) {
        participants.replace(id, con);
    }

    public static class Contribution<T> {

        private final T user;
        private final AtomicInteger tiles;

        private Contribution(T user) {
            this.user = user;
            this.tiles = new AtomicInteger(0);
        }

        public T getUser() {
            return user;
        }

        public AtomicInteger getTiles() {
            return tiles;
        }

        public void increase(int num) {
            tiles.addAndGet(num);
        }

        public static <U> Contribution<U> create(U user) {
            return new Contribution<>(user);
        }

    }

}
