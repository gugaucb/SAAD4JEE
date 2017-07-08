package me.costa.gustavo.saad4jee.negocio;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.costa.gustavo.saad4jee.interfaces.ICommand;
import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

public class EmitirEvento implements ICommand {
	private final Logger LOGGER = Logger.getLogger(EmitirEvento.class.getName());
	
	@Override
	public void executar(HttpRequestWrap httpRequest) {
		LOGGER.log(Level.INFO, "Robo identificado EmitirEvento. IP: "+httpRequest.getHttpRequest().getRemoteAddr());
	}

}
