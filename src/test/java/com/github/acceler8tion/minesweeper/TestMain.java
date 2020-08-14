package com.github.acceler8tion.minesweeper;

public class TestMain {

    public static void main(String[] args) {
        try {
            String id = "855735";
            User user = new User(id, "acceler8tion");
            MineSweeper<User> ms = MineSweeper.create(10, 10, 11, user);
            MineSweeperEmojiPack emp = new MineSweeperEmojiPack("e", "1", "2", "3", "4", "5", "6", "7", "8",
                                                                    "B", "F", "O", "X", "x");
            ms.getMetaData().putPlayers(id, user);
            MineSweeperMetaData.Contribution<User> con = ms.getMetaData().getPlayerById(id);
            ms.setMine(1, 1);
            System.out.println(ms.display(MineSweeperDisplay.ONGOING, 1, 1, emp));
            ms.open(0, 0, false, () -> {
                con.increase(1);
            });
            System.out.println(ms.display(MineSweeperDisplay.ONGOING, 1, 1, emp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class User {
        private final String id;
        private final String name;

        User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
