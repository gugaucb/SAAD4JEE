package me.costa.gustavo.saad4jee.interfaces;

import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

public interface ICommand {
	public void executar(HttpRequestWrap httpRequest);
}
