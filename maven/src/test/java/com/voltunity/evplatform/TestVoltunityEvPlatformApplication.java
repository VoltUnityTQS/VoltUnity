package com.voltunity.evplatform;

import org.springframework.boot.SpringApplication;

public class TestVoltunityEvPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.from(VoltunityEvPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
