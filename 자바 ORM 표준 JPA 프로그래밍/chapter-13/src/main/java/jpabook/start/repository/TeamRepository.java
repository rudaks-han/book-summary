package jpabook.start.repository;

import jpabook.start.jpo.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t join fetch t.members")
    @Override
    List<Team> findAll();
}
