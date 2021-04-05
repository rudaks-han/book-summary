package jpabook.start.service.proxy;

import jpabook.start.Application;
import jpabook.start.exception.jpo.item.Book;
import jpabook.start.exception.jpo.item.Item;
import jpabook.start.exception.jpo.member.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProxyTest {

    @Autowired
    private EntityManager em;

    @Test
    public void 영속성컨택스트와_프록시() {
        Member member = new Member(1L, "한경만");
        em.persist(member);
        em.flush();
        em.clear();

        Member refMember = em.getReference(Member.class, 1L);
        Member findMember = em.find(Member.class, 1L);

        System.out.println("refMember type = " + refMember.getClass());
        System.out.println("findMember type = " + findMember.getClass());

        Assert.assertTrue(refMember == findMember);
    }

    @Test
    public void 영속성컨택스트와_프록시2() {
        Member member = new Member(1L, "한경만");
        em.persist(member);
        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, 1L);
        Member refMember = em.getReference(Member.class, 1L);

        System.out.println("refMember type = " + refMember.getClass());
        System.out.println("findMember type = " + findMember.getClass());

        Assert.assertTrue(refMember == findMember);
    }

    @Test
    public void 프록시_타입비교() {
        Member member = new Member(1L, "한경만");
        em.persist(member);
        em.flush();
        em.clear();

        Member refMember = em.getReference(Member.class, 1L);

        System.out.println("refMember type = " + refMember.getClass());

        Assert.assertFalse(Member.class == refMember.getClass());
        Assert.assertTrue(refMember instanceof Member);
    }

    @Test
    public void 프록시_동등성비교() {
        Member member = new Member(1L, "한경만");
        em.persist(member);
        em.flush();
        em.clear();

        Member newMember = new Member(1L, "한경만");
        Member refMember = em.getReference(Member.class, 1L);

        Assert.assertTrue(newMember.equals(refMember));
    }

    @Test
    public void 부모타입으로_프록시조회() {
        Book saveBook = new Book();
        saveBook.setName("jpabook");
        saveBook.setAuthor("rudaks");
        em.persist(saveBook);
        em.flush();
        em.clear();

        // 테스트시작
        Item proxyItem = em.getReference(Item.class, saveBook.getId());
        System.out.println("proxyItem = " + proxyItem.getClass());

        if (proxyItem instanceof Book) {
            System.out.println("proxyItem instanceof Book");
            Book book = (Book) proxyItem;
            System.out.println("책 저자 = " + book.getAuthor());
        }

        // 결과 검증
        Assert.assertFalse(proxyItem.getClass() == Book.class);
        Assert.assertFalse(proxyItem instanceof Book);
        Assert.assertTrue(proxyItem instanceof Item);
    }

}
