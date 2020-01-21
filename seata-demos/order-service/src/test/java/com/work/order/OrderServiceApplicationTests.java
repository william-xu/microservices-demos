package com.work.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.xwl.msd.order.OrderServiceApplication;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class OrderServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void placeOrder(){		
		try {
			int status = getAction(20).andReturn().getResponse().getStatus();
			
			log.info("\nreturn status:: " + status + "\n");
			TestCase.assertEquals(200, status);				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	@Rule
	public ExpectedException nestedServletExceptionRule = ExpectedException.none();
	
	@Test
	public void rollback() throws Exception {		
		nestedServletExceptionRule.expectMessage("status 500 reading StorageFeignClient#deduct(String,Integer)");
		getAction("product-2",5).andReturn().getResponse().getStatus();	
	}
	
	
	private ResultActions getAction(int count) throws Exception {
		return mockMvc
				.perform(get("/order/placeOrder")
						.param("userId", "2")
						.param("commodityCode", "product-2")
						.param("count", String.valueOf(count)));
	}

	private ResultActions getAction(String commodityCode, int count) throws Exception {
		return mockMvc
				.perform(get("/order/placeOrder")
						.param("userId", "2")
						.param("commodityCode", commodityCode)
						.param("count", String.valueOf(count)));
	}
	
	

}
