package jpabook.start.elementcollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElementCollectionExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void testSave(EntityManager em) {
        Member2 member = new Member2("kmhan", "한경만");

        Address2 address = new Address2("경기도", "미사대로", "123-123");
        Set<String> favoriteFoods = new HashSet<>();
        favoriteFoods.add("라면");
        favoriteFoods.add("삼겹살");

        List<Address2> addressList = Arrays.asList(address);

        member.setAddress(address);
        member.setFavoriteFoods(favoriteFoods);
        member.setAddressHistory(addressList);

        em.persist(member);

        em.flush();
        em.clear();

        Member2 findMember = em.find(Member2.class, "kmhan");
        Address2 findAddress = findMember.getAddress();

        System.out.println(findAddress.getCity());

        Set<String> findFavoriteFoods = findMember.getFavoriteFoods();
        System.out.println(findFavoriteFoods.size());
    }
}
