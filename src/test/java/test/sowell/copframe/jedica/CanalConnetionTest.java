package test.sowell.copframe.jedica;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import cn.sowell.copframe.jedica.CanalConnection;
import cn.sowell.copframe.jedica.PrintEntriesHandler;

public class CanalConnetionTest {
	
	@Test
	public void connectTest() {
		CanalConnection connection = new CanalConnection();
		connection.setConfigFile(new ClassPathResource("jedica-example.properties"));
		PrintEntriesHandler printHandler = new PrintEntriesHandler();
		connection.setHandlerList(Arrays.asList(printHandler));
		try {
			connection.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
