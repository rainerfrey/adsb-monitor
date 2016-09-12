package de.mrfrey.adsbmonitor.connector

import de.mrfrey.adsbmonitor.ui.UiBackendApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = UiBackendApplication.class)
@WebAppConfiguration
class UiBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
