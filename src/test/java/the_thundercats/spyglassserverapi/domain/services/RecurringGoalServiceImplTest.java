package the_thundercats.spyglassserverapi.domain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import the_thundercats.spyglassserverapi.domain.Frequency;
import the_thundercats.spyglassserverapi.domain.core.exceptions.ResourceNotFoundException;
import the_thundercats.spyglassserverapi.domain.dtos.UserDTO;
import the_thundercats.spyglassserverapi.domain.models.RecurringGoal;
import the_thundercats.spyglassserverapi.domain.models.User;
import the_thundercats.spyglassserverapi.domain.repos.RecurringGoalRepo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RecurringGoalServiceImplTest {

    @Autowired
    private RecurringGoalService recurringGoalService;

    @MockBean
    private UserService userService;

    @MockBean
    private RecurringGoalRepo recurringGoalRepo;

    private RecurringGoal mockGoal01;
    private RecurringGoal savedGoal01;
    private RecurringGoal savedGoal02;

    private UserDTO savedUser01;

    private User savedUser02;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockGoal01 = new RecurringGoal("travel", "this is for travel", "this would be a path", new Date(), 0.00, 100.00, 0.00, Frequency.WEEKLY);
        mockGoal01.setUser(savedUser01);

        savedGoal01 = new RecurringGoal("travel", "this is for travel", "this would be a path", new Date(), 0.00, 100.00, 0.00, Frequency.WEEKLY);
        savedGoal01.setId(1L);
        savedGoal01.setUser(savedUser01);

        savedGoal02 = new RecurringGoal("car", "this is for car", "this would be a path", new Date(), 0.00, 10000.00, 100.00, Frequency.MONTHLY);
        savedGoal02.setId(1L);

        mockUser = new User("Sabrina", "Rose", "bwina@gmail.com");

        savedUser01 = new UserDTO(mockUser);
        savedUser01.setId("abc");

        savedUser02 = new User("Sabrina", "Rose", "bwina@gmail.com");
        savedUser02.setId("def");
    }

    @Test
    @DisplayName("Create recurring goal - success")
    public void createTest01() throws ResourceNotFoundException {
        BDDMockito.doReturn(savedUser01).when(userService).getUserById("abc");
        BDDMockito.doReturn(savedGoal01).when(recurringGoalRepo).save(mockGoal01);
        RecurringGoal goal = recurringGoalService.create("abc", mockGoal01);
        Assertions.assertEquals(savedGoal01, goal);
    }

    @Test
    @DisplayName("Get by id - success")
    public void getByIdTest01() throws ResourceNotFoundException {
        BDDMockito.doReturn(Optional.of((savedGoal01))).when(recurringGoalRepo).findById(1L);
        RecurringGoal goal = recurringGoalService.getById(1L);
        Assertions.assertEquals(savedGoal01, goal);
    }

    @Test
    @DisplayName("Get by id - fail")
    public void getByIdTest02() {
        BDDMockito.doReturn(Optional.empty()).when(recurringGoalRepo).findById(1L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            recurringGoalService.getById(1L);
        });
    }

    @Test
    @DisplayName("Get all from user - success")
    public void getAllFromUserTest01() throws ResourceNotFoundException {
        List<RecurringGoal> goals = new ArrayList<>();
        goals.add(savedGoal01);
        BDDMockito.doReturn(goals).when(recurringGoalRepo).findByUser(savedUser02);
        List<RecurringGoal> actual = recurringGoalService.getAllFromUser(savedUser02);
        Assertions.assertIterableEquals(goals, actual);
    }

    @Test
    @DisplayName("Update - success")
    public void updateTest01() throws ResourceNotFoundException {
        RecurringGoal updated = new RecurringGoal("update", "this is for an update", "this would be a path", new Date(), 230.00, 100.00, 0.00, Frequency.WEEKLY);
        updated.setId(1L);
        BDDMockito.doReturn(Optional.of(savedGoal01)).when(recurringGoalRepo).findById(1L);
        BDDMockito.doReturn(updated).when(recurringGoalRepo).save(savedGoal01);
        RecurringGoal actual = recurringGoalService.update(1L, updated);
        Assertions.assertEquals(updated, actual);
    }
}
