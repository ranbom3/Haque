package com.ranbom.haque;

import com.ranbom.haque.dao.QuserMapper;
import com.ranbom.haque.entity.Quser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HaqueApplicationTests {

	@Autowired
	QuserMapper quserMapper;

	@Test
	void contextLoads() {
		Quser quser = quserMapper.loadUserByUsername("djh");
		System.out.println(quser.toString());

	}

}
