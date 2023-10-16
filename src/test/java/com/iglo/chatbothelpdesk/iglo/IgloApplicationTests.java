package com.iglo.chatbothelpdesk.iglo;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SpringBootTest
@EnableAsync
class IgloApplicationTests {

	@Test
	void contextLoads() throws Exception{
		String url = "https://dittel.kominfo.go.id/attachments/blank.pdf";

		asyncMethod(url);
		System.out.println("waiting file...");
	}

	@Async
	public void asyncMethod(String url) throws IOException {
		byte[] bytes = loadFile(url);
		System.out.println(bytes.length);
		for (byte b : bytes) {
			System.out.print(b);
			System.out.print(", ");
		}
	}


	private static byte[] loadFile(String url) throws IOException {
//		URL fileUrl = new URL(url);
		String userHome = System.getProperty("user.home");
		String path = userHome + "\\Downloads\\putty-64bit-0.79-installer.msi";
		URL fileUrl = new File(path).toURI().toURL();

		try (InputStream in = fileUrl.openStream()) {
			return IOUtils.toByteArray(in);
		}
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
		/*if (size > 7){
			if (page == 0){
				hasNext = true;
				limit = 7;
			} else if (size / 6 > page) {
				hasNext = true;
				limit = 6;
			} else {
				hasNext = false;
				limit = size - (page * 6) - 1;
			}
		} else {
			limit = size;
		}*/
}
