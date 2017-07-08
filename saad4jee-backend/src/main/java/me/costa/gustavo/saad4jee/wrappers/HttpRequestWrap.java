package me.costa.gustavo.saad4jee.wrappers;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestWrap {
	private HttpServletRequest httpRequest;
	
	public HttpRequestWrap(HttpServletRequest httpRequest){
		this.httpRequest = httpRequest;
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}
}
