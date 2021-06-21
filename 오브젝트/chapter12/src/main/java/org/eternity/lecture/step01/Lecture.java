package org.eternity.lecture.step01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lecture {
    private int pass;
    private String title;
    private List<Integer> scores = new ArrayList<>();

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title;
        this.pass = pass;
        this.scores = scores;
    }

    public double average() {
        return scores.stream()
                .mapToInt(Integer::intValue)
                .average().orElse(0);
    }

    public List<Integer> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public String evaluate() {
        return String.format("Pass:%d Fail:%d", passCount(), failCount());
    }

    public long passCount() {
        return scores.stream()
                .filter(score -> score >= pass).count();
    }

    public long failCount() {
        return scores.size() - passCount();
    }

    public static void main(String[] args) {
        Lecture lecture = new Lecture("객체지향 프로그래밍",
                70,
                Arrays.asList(81, 95, 75, 50, 45));
        String evaluation = lecture.evaluate();
        System.out.println(evaluation);
    }
}
