package the_thundercats.spyglassserverapi.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the_thundercats.spyglassserverapi.domain.core.exceptions.ResourceCreationException;
import the_thundercats.spyglassserverapi.domain.core.exceptions.ResourceNotFoundException;
import the_thundercats.spyglassserverapi.domain.models.Contribution;
import the_thundercats.spyglassserverapi.domain.models.RecurringGoal;
import the_thundercats.spyglassserverapi.domain.services.ContributionService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/contribution")
public class ContributionController {
    private ContributionService contributionService;
    @Autowired
    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @PostMapping
    public ResponseEntity<Contribution> createContribution(@RequestBody Contribution contribution) throws ResourceCreationException {
        contribution = contributionService.create(contribution);
        return new ResponseEntity<>(contribution, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Contribution>> getAllContributionsFromGoal() throws ResourceNotFoundException {
        List<Contribution> contributions = contributionService.getAllFromGoal();
        return new ResponseEntity<>(contributions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contribution> getContributionById(@PathVariable Long id) throws ResourceNotFoundException {
        Contribution contribution = contributionService.getById(id);
        return ResponseEntity.ok(contribution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contribution> update(@PathVariable("id") Long goalId, @RequestBody Contribution details) throws ResourceNotFoundException {
        Contribution contribution = contributionService.update(goalId, details);
        return new ResponseEntity<>(contribution, HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteContribution(@PathVariable Long id) throws ResourceNotFoundException {
        contributionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
