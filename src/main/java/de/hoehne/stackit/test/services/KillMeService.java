package de.hoehne.stackit.test.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KillMeService {

	@Async
	public void killMemory() {
		log.info("Start to kill the memory now...");
		List<byte[]> list = new ArrayList<>();
		int index = 0;
		while (true) {
			try {
				// 1MB each loop, 1 x 1024 x 1024 = 1048576
				byte[] b = new byte[1048576];
				list.add(b);

			} catch (OutOfMemoryError e) {
				list.clear();
				log.error("Out Of Memory Error. {} Restart", index++);
			}
		}

	}
}
