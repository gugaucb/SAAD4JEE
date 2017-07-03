package me.costa.gustavo.saad4jee.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface IDicionario {

	Integer getPosicao(String metodo);

	Integer localizaUltimaPosicao();

	Serializable getId();

	Map<String, Integer> getListaMetodos();

	void setListaMetodos(Map<String, Integer> listaMetodos);

}