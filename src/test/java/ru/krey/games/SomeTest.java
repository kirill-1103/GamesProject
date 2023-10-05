package ru.krey.games;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.krey.games.controller.MainController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@RequiredArgsConstructor
public class SomeTest {
    private final MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"));
    }
}
