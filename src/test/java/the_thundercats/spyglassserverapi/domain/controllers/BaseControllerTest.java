package the_thundercats.spyglassserverapi.domain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseControllerTest {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
