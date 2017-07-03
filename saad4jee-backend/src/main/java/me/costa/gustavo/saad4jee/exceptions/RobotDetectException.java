package me.costa.gustavo.saad4jee.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class RobotDetectException extends WebApplicationException {
	public RobotDetectException(String mensagem) {
		super(Response.status(Status.FORBIDDEN).entity(mensagem).build());
	}
}
