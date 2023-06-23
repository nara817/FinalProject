package com.gdu.pupo.batch;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.gdu.pupo.domain.RegularDetailImgDTO;
import com.gdu.pupo.domain.RegularMainImgDTO;
import com.gdu.pupo.mapper.AdminMapper;
import com.gdu.pupo.mapper.RegularMapper;
import com.gdu.pupo.util.MyFileUtil;

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
	
	 public void execute() {
	        // 어제 업로드 된 첨부 파일들의 정보 (DB에서 가져오기)
	        List<RegularDetailImgDTO> regularDetailImgList = amdminMapper.getRegularDetailImgListInYesterday();
	        List<RegularMainImgDTO> regularMainImgList = amdminMapper.getRegularMainImgListInYesterday();

	     // List<RegularDetailImgDTO>와 List<RegularMainImgDTO> -> List<Path>로 변환하기
	        List<Path> pathList = new ArrayList<Path>();
	        
	        if (regularDetailImgList != null && !regularDetailImgList.isEmpty()) {
	            for (RegularDetailImgDTO regulardetailImgDTO : regularDetailImgList) {
	                String filePath = myFileUtil.getPath(); // 파일 경로 가져오기
	                pathList.add(new File(filePath, regulardetailImgDTO.getRegFilesystemName()).toPath());
	            }
	        }
	        if (regularMainImgList != null && !regularMainImgList.isEmpty()) {
	            for (RegularMainImgDTO regularmainImgDTO : regularMainImgList) {
	                String filePath = myFileUtil.getPath(); // 파일 경로 가져오기
	                pathList.add(new File(filePath, regularmainImgDTO.getRegFilesystemName()).toPath());
	            }
	        }

	        // 어제 업로드 된 경로
	        String yesterdayPath = myFileUtil.getYesterdayPath();

	        // 어제 업로드 된 파일 목록(HDD에서 확인) 중에서 DB에는 정보가 없는 파일 목록
	        File dir = new File(yesterdayPath);
	        File[] wrongFiles = dir.listFiles(new FilenameFilter() {
	            @Override
	            public boolean accept(File dir, String name) {
	                // DB에 있는 목록: pathList - 이미 Path
	                // HDD에 있는 파일: File dir, String name - File.toPath() 처리해서 Path로 변경
	                return pathList.contains(new File(dir, name).toPath()) == false;
	            }
	        });

	        // File[] wrongFiles 모두 삭제
	        if (wrongFiles != null && wrongFiles.length != 0) {
	            for (File wrongFile : wrongFiles) {
	                wrongFile.delete();
	            }
	        }
	    }
	}