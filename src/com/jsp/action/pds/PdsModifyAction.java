package com.jsp.action.pds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.jsp.action.Action;
import com.jsp.controller.FileUploadResolver;
import com.jsp.controller.GetUploadPath;
import com.jsp.controller.XSSMultipartHttpServletRequestParser;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;

public class PdsModifyAction implements Action {
	
	private PdsService pdsService;
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "/pds/modify_success";
		
		try {
			//파일 삭제, 저장
			PdsVO pds = modifyAttaches(request, response);
			
			//DB 처리
			pdsService.modify(pds);

			request.setAttribute("pds", pds);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return url;
	}
	
	//업로드 파일 환경설정
	final private int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3MB
	final private int MAX_FILE_SIZE = 1024 * 1024 * 40; //40MB
	final private int MAX_REQUEST_SIZE = 1024 *1024 * 200; //200MB
	
	private PdsVO modifyAttaches(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PdsVO pds = null;
		
		XSSMultipartHttpServletRequestParser multi = new XSSMultipartHttpServletRequestParser(request, MEMORY_THRESHOLD, MAX_FILE_SIZE, MAX_REQUEST_SIZE);
		
		//파일 삭제 및 DB삭제
		String[] deleteFiles = multi.getParameterValues("deleteFile");
		if(deleteFiles != null && deleteFiles.length > 0) {
			for(String anoStr :deleteFiles) {
				int ano = Integer.parseInt(anoStr);
				AttachVO attach = pdsService.getAttachByAno(ano);
				String filePath = attach.getUploadPath() + File.separator + attach.getFileName();
				File targetFile = new File(filePath);
				
				if(targetFile.exists()) {
					targetFile.delete();
				}
				pdsService.removeAttachByAno(ano);
			}
		}
		
		//추가된 파일 저장
		FileItem[] fileItems = multi.getFileItems("uploadFile");
		List<AttachVO> attachList = null;
		if(fileItems != null && fileItems.length > 0) {
			String uploadPath = GetUploadPath.getUploadPath("pds.upload");
			List<File> fileList = FileUploadResolver.fileUpload(fileItems, uploadPath);
			
			attachList = new ArrayList<AttachVO>();
			if(fileList.size() > 0) for(File file : fileList) {
				AttachVO attach = new AttachVO();
				attach.setFileName(file.getName());
				attach.setUploadPath(uploadPath);
				attach.setFileType(file.getName().substring(file.getName().lastIndexOf(".") + 1));
				attachList.add(attach);
			}
			
		}
		pds = new PdsVO();
		pds.setPno(Integer.parseInt(multi.getParameter("pno")));
		pds.setTitle(multi.getXSSParameter("title"));
		pds.setContent(multi.getParameter("content"));
		pds.setWriter(multi.getXSSParameter("writer"));
		pds.setAttachList(attachList);
		
		return pds;
	}

}
