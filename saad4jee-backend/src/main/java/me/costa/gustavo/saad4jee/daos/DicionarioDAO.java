package me.costa.gustavo.saad4jee.daos;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.entity.Dicionario;



@Named
@RequestScoped
public class DicionarioDAO extends GenericDaoJpaSaad4Jee<Dicionario> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6451669848088491600L;

}
