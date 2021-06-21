package org.eternity.lecture.step02;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class GradeLecture extends Lecture {
    private List<Grade> grades;

    public GradeLecture(String name, int pass, List<Grade> grades, List<Integer> scores) {
        super(name, pass, scores);
        this.grades = grades;
    }

    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    private String gradesStatistics() {
        return grades.stream()
                .map(grade -> format(grade))
                .collect(joining(" "));
    }

    public String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .count();
    }

    public static void main(String[] args) {
        Lecture lecture = new GradeLecture("객체지향 프로그래밍",
                70,
                Arrays.asList(new Grade("A", 100, 95),
                        new Grade("B", 94, 80),
                        new Grade("C", 79, 70),
                        new Grade("D", 69, 50),
                        new Grade("F", 49, 0)),
                Arrays.asList(81, 95, 75, 50, 45));

        String evaluation = lecture.evaluate();
        System.out.println(evaluation);
    }
}
