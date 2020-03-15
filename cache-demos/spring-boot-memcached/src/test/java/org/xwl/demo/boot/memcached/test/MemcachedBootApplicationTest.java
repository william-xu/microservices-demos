package org.xwl.demo.boot.memcached.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xwl.demo.boot.tp.memcached.BootMemcachedApplication;

/**
 * @author xwl
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootMemcachedApplication.class})
public class MemcachedBootApplicationTest {

	@Test
	public void contextLoads() {
	}

}
