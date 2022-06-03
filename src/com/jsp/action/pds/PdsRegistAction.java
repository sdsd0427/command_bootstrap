package com.jsp.action.pds;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.controller.FileUploadResolver;
import com.jsp.controller.GetUploadPath;
import com.jsp.controller.XSSMultipartHttpServletRequestParser;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.exception.NotMultipartFormDataException;
import com.jsp.service.PdsService;

public class PdsRegistAction implements Action {
	
	private PdsService pdsService;
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	//1. 입력 : commons-fileupload.jar 패키지를 이용하여 FileItem 형태로 변화
	//업로드 파일 환경설정
	final private int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3MB
	final private int MAX_FILE_SIZE = 1024 * 1024 * 40; //40MB
	final private int MAX_REQUEST_SIZE = 1024 *1024 * 200; //200MB
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "/pds/regist_success";
		
		XSSMultipartHttpServletRequestParser multi = null;
		List<AttachVO> attachList = null;
		
		try {
			//파싱
			multi = new XSSMultipartHttpServletRequestParser(request, MEMORY_THRESHOLD, MAX_FILE_SIZE, MAX_REQUEST_SIZE);
			
			//파일처리
			//실제 저장 경로를 설정.
			String uploadPath = GetUploadPath.getUploadPath("pds.upload");
			
			//파일 저장 후 List<file>을 리턴
			List<File> fileList = FileUploadResolver.fileUpload(multi.getFileItems("uploadFile"), uploadPath);
			
			//List<file> -> List<AttachVO>
			if(fileList != null) {
				attachList = new ArrayList<AttachVO>();
				for(File file : fileList) {
					//DB에 저장할 attach에 file 내용 추가
					AttachVO attach = new AttachVO();
					attach.setFileName(file.getName());
					attach.setUploadPath(uploadPath);
					attach.setFileType(file.getName().substring(file.getName().lastIndexOf(".") + 1));
					
					attachList.add(attach);
				}
			}
		} catch (NotMultipartFormDataException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		//DB처리
		PdsVO pds = new PdsVO();
		pds.setTitle(multi.getXSSParameter("title"));
		pds.setContent(multi.getParameter("content"));
		pds.setWriter(multi.getXSSParameter("writer"));
		pds.setAttachList(attachList);
		
		try {
			pdsService.regist(pds);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		return url;
	}

}