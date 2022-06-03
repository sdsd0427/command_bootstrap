package com.jsp.action.pds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.command.PdsModifyCommand;
import com.jsp.controller.XSSHttpRequestParameterAdapter;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;

public class PdsModifyAction implements Action {
	
	private PdsService pdsService;
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "redirect:/pds/detail.do?from=modify&pno="+request.getParameter("pno");
		
		try {
			PdsModifyCommand modifyReq = (PdsModifyCommand)XSSHttpRequestParameterAdapter.execute(request, PdsModifyCommand.class, true);
			PdsVO pds = modifyReq.toPdsVO();
			pds.setContent(request.getParameter("content"));
			
			pdsService.modify(pds);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return url;
	}

}
