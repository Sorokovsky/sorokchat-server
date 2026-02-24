package com.sorokovsky.sorokchat.utils;

public class Nickname {
    public static String NICKNAME_SYMBOL = "@";

    public static boolean isNickname(String nickname) {
        return nickname.startsWith(NICKNAME_SYMBOL);
    }

    public static String asNickname(String nickname) {
        return NICKNAME_SYMBOL.concat(nickname);
    }

    public static String asTerm(String nickname) {
        return nickname.replaceFirst(NICKNAME_SYMBOL, "");
    }
}
