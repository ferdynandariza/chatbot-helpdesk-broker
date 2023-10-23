package com.iglo.chatbothelpdesk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootTest
@EnableAsync
class HelpdeskBrokerApplicationTests {

	@Test
	void contextLoads(){

	}

	@Test
	void testPage(){
		Assertions.assertFalse(check(1, 0));
		Assertions.assertFalse(check(7, 0));
		Assertions.assertFalse(check(6, 0));
		Assertions.assertTrue(check(8, 0));
		Assertions.assertFalse(check(8, 1));
		Assertions.assertFalse(check(13, 1));
		Assertions.assertTrue(check(14, 1));
		Assertions.assertFalse(check(14, 2));
		Assertions.assertFalse(check(15, 2));
	}

	private boolean check(Integer size, Integer page){
		int limit = 7;
		boolean hasNext = false;
		if (size <= 7){
			limit = size;
			System.out.printf("size, page = %s, %s\t-> limit = %s\n", size, page, limit);
			return hasNext;
		}
		if (page == 0){
			hasNext = true;
			limit = 7;
		} else if (Math.ceil((double)(size - 1) / 6) > page + 1) {
			hasNext = true;
			limit = 6;
		} else {
			hasNext = false;
			limit = size - (page * 6) - 1;
		}
		System.out.printf("size, page = %s, %s\t-> limit = %s\n", size, page, limit);
		return hasNext;
	}

}
