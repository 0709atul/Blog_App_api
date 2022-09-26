package com.prectise.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.blog.repositories.UserRepository;

@SpringBootTest
class BolgAppApisApplicationTests {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void repoTest() {
		String className = this.userRepository.getClass().getName();
		String classPackageName = this.userRepository.getClass().getPackageName();
		System.out.println(className);
		System.out.println(classPackageName);
	}
}
