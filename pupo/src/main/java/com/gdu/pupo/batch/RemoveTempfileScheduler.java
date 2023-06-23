package com.gdu.pupo.batch;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.pupo.mapper.AdminMapper;
import com.gdu.pupo.mapper.RegularMapper;
import com.gdu.pupo.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableScheduling
@Component
public class RemoveTempfileScheduler {

	// 임시 폴더(temp)의 모든 파일을 매일 새벽 12시에 지우는 스케쥴러
	
	private final MyFileUtil myFileUtil;
	private final RegularMapper regularMapper;
	private final AdminMapper amdminMapper;

	@Scheduled(cron="0 0 12 1/1 * ?")  // www.cronmaker.com에서 생성한 매일 새벽 12시
	public void execute() {
		
		// 임시 폴더의 File 객체
		File dir = new File(myFileUtil.getPath());
		
		// 임시 폴더가 존재하면 폴더 내의 모든 파일을 가져와서 하나씩 삭제
		if(dir.exists()) {
			for(File file : dir.listFiles()) {
				file.delete();
			}
		}
		
	}
	
}