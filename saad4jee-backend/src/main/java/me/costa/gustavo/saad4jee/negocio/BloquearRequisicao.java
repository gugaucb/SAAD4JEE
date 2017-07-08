package me.costa.gustavo.saad4jee.negocio;

import me.costa.gustavo.saad4jee.exceptions.RobotDetectException;
import me.costa.gustavo.saad4jee.interfaces.ICommand;
import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

public class BloquearRequisicao implements ICommand {

	@Override
	public void executar(HttpRequestWrap httpRequest) {
		throw new RobotDetectException("Robo identificado.");
	}

}
