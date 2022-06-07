package com.jsp.action.pds;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.AttachVO;
import com.jsp.service.PdsService;

public class PdsRemoveAction implements Action {
	
	private PdsService pdsService;
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url ="/pds/remove_success";
		
		int pno = Integer.parseInt(request.getParameter("pno"));
		try {
			//파일 삭제
			//pno에 대한 attachList확보
			List<AttachVO> attachList = pdsService.getPds(pno).getAttachList();
			if(attachList != null) {
				for(AttachVO attach : attachList) {
					String storedFilePath = attach.getUploadPath() + File.separator + attach.getFileName();
					
					File file = new File(storedFilePath);
					
					if(file.exists()) {
						file.delete();
					}
				}
			}
			
			//DB내용 삭제
			//attach는 종속삭제 설정
			pdsService.remove(pno);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return url;
	}

}
