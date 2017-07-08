package me.costa.gustavo.saad4jee.enums;

import javax.inject.Inject;

import me.costa.gustavo.saad4jee.interfaces.ICommand;
import me.costa.gustavo.saad4jee.negocio.BloquearRequisicao;
import me.costa.gustavo.saad4jee.negocio.EmitirEvento;
import me.costa.gustavo.saad4jee.negocio.EnviarTrap;
import me.costa.gustavo.saad4jee.negocio.ImprimirConsole;
import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

public enum Comandos {
	
	EmitirEvento(new EmitirEvento()),
	BloquearRequisicao(new BloquearRequisicao()),
	EnviarTrap(new EnviarTrap()),
	ImprimirConsole(new ImprimirConsole());
	
	public ICommand comando;
	@Inject
	Comandos(ICommand comando){
		this.comando = comando;
	}
	
	public void executar(HttpRequestWrap httpRequest){
		this.comando.executar(httpRequest);
	}
}
