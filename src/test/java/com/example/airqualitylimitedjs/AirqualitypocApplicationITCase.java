package com.example.airqualitylimitedjs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.profiles.active:localwithh2")
@TestPropertySource(properties = {"os.mapping.key=someRandomKey"})
class AirqualitypocApplicationITCase {

	@Test
	void contextLoads() {
	}

}
