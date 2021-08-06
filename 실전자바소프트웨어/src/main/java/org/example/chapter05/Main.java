package org.example.chapter05;

public class Main {

    public static void main(final String...args) {

        Facts env = new Facts();
        env.addFact("name", "Bob");
        env.addFact("jobTitle", "CEO");

        BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(env);

        final Rule ruleSendEmailToSalesWhenCEO =
            RuleBuilder
                .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
                .then(facts -> {
                    String name = facts.getFact("name");
                    System.out.println("Relevant customer!!!: " + name);
                });

        businessRuleEngine.addRule(ruleSendEmailToSalesWhenCEO);
        businessRuleEngine.run();

    }
}
