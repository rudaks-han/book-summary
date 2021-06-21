package org.eternity.lecture.step02;

import java.util.Arrays;

public class Professor {
    private String name;
    private Lecture lecture;

    public Professor(String name, Lecture lecture) {
        this.name = name;
        this.lecture = lecture;
    }

    public String compileStatistics() {
        return String.format("[%s] %s - Avg: %.1f", name,
                lecture.evaluate(), lecture.average());
    }

    public static void main(String[] args) {
        Professor professor = new Professor("다익스트라",
                new Lecture("알고리즘",
                        70,
                        Arrays.asList(81, 95, 75, 50, 45)));
        String statistics = professor.compileStatistics();
        System.out.println(statistics);
    }
}
