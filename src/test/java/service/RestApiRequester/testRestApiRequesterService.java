package service.RestApiRequester;

import Application.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testRestApiRequesterService {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetArtistById() throws Exception {
        String artistId = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";

        mockMvc.perform(MockMvcRequestBuilders.get("/artist/{id}", artistId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
