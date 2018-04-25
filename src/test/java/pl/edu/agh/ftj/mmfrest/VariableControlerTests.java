package pl.edu.agh.ftj.mmfrest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VariableControlerTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private final String BASE_URL = "/variables";

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testGetVariablesEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("[]", true))
        .andDo(print())
        .andReturn();
    }

    @Test
    public void testGetNonExistingVariable() throws Exception {
        String url = new StringBuilder()
                .append(BASE_URL)
                .append("/x_not_there")
                .toString();
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}
