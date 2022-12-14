package the_thundercats.spyglassserverapi.domain.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the_thundercats.spyglassserverapi.domain.core.exceptions.ResourceNotFoundException;
import the_thundercats.spyglassserverapi.domain.core.exceptions.ResourceUpdateException;
import the_thundercats.spyglassserverapi.domain.models.Contribution;
import the_thundercats.spyglassserverapi.domain.models.Goal;
import the_thundercats.spyglassserverapi.domain.repos.ContributionRepo;
import the_thundercats.spyglassserverapi.domain.services.ContributionService;
import the_thundercats.spyglassserverapi.domain.services.RecurringGoalService;
import java.util.List;

@Service
public class ContributionServiceImpl implements ContributionService {
    private ContributionRepo contributionRepo;
    private RecurringGoalService recurringGoalService;
    @Autowired
    public ContributionServiceImpl(ContributionRepo contributionRepo, RecurringGoalService recurringGoalService) {
        this.contributionRepo = contributionRepo;
        this.recurringGoalService = recurringGoalService;
    }

    @Override
    public Contribution create(Long goalId, Contribution contribution) throws ResourceNotFoundException {
        Goal goal = recurringGoalService.getById(goalId);
        goal.addToCurrentDollarAmount(contribution.getContributionAmount());
        recurringGoalService.update(goalId, goal);
        contribution.setGoal(goal);
        return contributionRepo.save(contribution);
    }

    @Override
    public Contribution getById(Long id) throws ResourceNotFoundException {
        return contributionRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No contribution with id: " + id));
    }

    @Override
    public List<Contribution> getAllFromGoal(Goal goal) throws ResourceNotFoundException {
        return contributionRepo.findByGoal(goal);
    }

    @Override
    public Contribution update(Long id, Contribution contributionDetails) throws ResourceNotFoundException, ResourceUpdateException {
        Contribution contribution = getById(id);
        contribution.setContributionAmount(contributionDetails.getContributionAmount());
        contribution.setContributionDate(contributionDetails.getContributionDate());
        return contributionRepo.save(contribution);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Contribution message = getById(id);
        Goal goal = message.getGoal();
        goal.removeFromCurrentDollarAmount(message.getContributionAmount());
        recurringGoalService.update(goal.getId(), goal);
        contributionRepo.delete(message);
    }
}
