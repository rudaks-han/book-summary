package com.example.ch05._5_2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 팀을 나타낸다. */
public class T {
    Set<String> pns = new HashSet<>(); // 팀에 속한 선수의 이름
    int s = 0; // 팀의 점수

    /**
     * @param n 플레이어의 이름
     * @return true 플레이어가 팀에 속해 있는 경우
     */
    private boolean f(String n) {
        return pns.contains(n);
    }

    /**
     * @return 팀의 점수
     */
    private int getS() {
        return this.s;
    }

    /**
     * @param ts 모든 팀의 리스트
     * @param n 플레이어의 이름
     * @return 플레이어가 속해 있는 팀의 점수
     */
    private int s(List<T> ts, String n) {
        for (T t : ts) {
            if (t.f(n)) {
                return t.getS();
            }
        }

        return -1;
    }
}
