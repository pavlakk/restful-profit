package pl.pvkk.profit.exchange;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ExchangeServiceTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testBuyShares() throws Exception {
		//cool version
		this.mockMvc
			.perform(post("/transfer/buy?name=alior&number=5")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"));
		
		//wrong number
		this.mockMvc
			.perform(post("/transfer/buy?name=alior&number=a")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(status().isBadRequest());
		
		//wrong name
		this.mockMvc
			.perform(post("/transfer/buy?name=aasd&number=5")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(status().isBadRequest());
	}

}
