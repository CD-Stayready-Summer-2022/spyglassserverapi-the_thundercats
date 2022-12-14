package the_thundercats.spyglassserverapi.domain.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import the_thundercats.spyglassserverapi.domain.models.Contribution;
import the_thundercats.spyglassserverapi.domain.models.Goal;

import java.util.List;


public interface ContributionRepo extends JpaRepository<Contribution, Long> {
    List<Contribution> findByGoal(Goal goal);
}
