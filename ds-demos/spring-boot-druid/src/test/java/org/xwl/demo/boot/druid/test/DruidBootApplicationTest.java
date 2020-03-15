package org.xwl.demo.boot.druid.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xwl.demo.boot.tp.druid.DruidSpringBootApplication;

/**
 * @author xwl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DruidSpringBootApplication.class})
public class DruidBootApplicationTest {

	@Test
	public void contextLoads() {
	}

}
