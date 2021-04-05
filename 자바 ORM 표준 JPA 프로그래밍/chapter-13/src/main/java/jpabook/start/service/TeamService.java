package jpabook.start.service;

import jpabook.start.jpo.Team;
import jpabook.start.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public void register(Team team) {
        teamRepository.save(team);
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team find(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("team id not exists: " + id));
    }
}
