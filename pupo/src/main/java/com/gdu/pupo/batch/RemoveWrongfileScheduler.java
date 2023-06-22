package com.gdu.pupo.batch;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.pupo.domain.AttachDTO;
import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularMainImgDTO;
import com.gdu.pupo.mapper.AdminMapper;
import com.gdu.pupo.mapper.RegularMapper;
import com.gdu.pupo.util.MyFileUtil;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  // field의 @Autowired 처리
@EnableScheduling
@Component
// 새벽 12시에 어제 업로드된 파일들 중 DB에 등록되지 않은 파일들을 삭제하는 작업
public class RemoveWrongfileScheduler {
	
	// field
	private final MyFileUtil myFileUtil;
	private final RegularMapper regularMapper;
	private final AdminMapper amdminMapper;
	
	@Scheduled(cron="0 0 12 1/1 * ?")  // www.cronmaker.com에서 생성한 매일 새벽 12시
	public void execute() {
		List<AttachDTO> attachList = new ArrayList<>();

		// 어제 업로드된 디테일 이미지 정보 가져오기
		List<RegularDetailImgDTO> regularImgList = adminMapper.getAttachListInYesterday();
		regularImgList.addAll(regularImgList);

		// 어제 업로드된 메인 이미지 정보 가져오기`
		List<RegularMainImgDTO> regularMainImgList = adminMapper.getMainImgListInYesterday();
		regularMainImgList.addAll(regularMainImgList);

		// 이미지 파일 삭제
		deleteImageFiles(attachList);
	}

	private void deleteImageFiles(List<AttachDTO> attachList) {
		List<Path> pathList = new ArrayList<>();
		if (attachList != null && !attachList.isEmpty()) {
			for (AttachDTO attach : attachList) {
				pathList.add(Paths.get(attach.getPath(), attach.getFilesystemName()));
				if (attach.getHasThumbnail() == 1) {
					pathList.add(Paths.get(attach.getPath(), "s_" + attach.getFilesystemName()));
				}
			}
		}

		String yesterdayPath = myFileUtil.getYesterdayPath();

		File dir = new File(yesterdayPath);
		File[] wrongFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !pathList.contains(Paths.get(dir.getPath(), name));
			}
		});

		if (wrongFiles != null && wrongFiles.length != 0) {
			for (File wrongFile : wrongFiles) {
				wrongFile.delete();
			}
		}
	}
}