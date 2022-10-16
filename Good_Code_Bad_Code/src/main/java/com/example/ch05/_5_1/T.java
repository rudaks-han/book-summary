package com.example.ch05._5_1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class T {
    Set<String> pns = new HashSet<>();
    int s = 0;

    private boolean f(String n) {
        return pns.contains(n);
    }

    private int getS() {
        return this.s;
    }

    private int s(List<T> ts, String n) {
        for (T t : ts) {
            if (t.f(n)) {
                return t.getS();
            }
        }

        return -1;
    }
}
