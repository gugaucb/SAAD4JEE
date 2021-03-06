package me.costa.gustavo.saad4jee.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Transient;

import me.costa.gustavo.saad4jee.daos.DicionarioDAO;
import me.costa.gustavo.saad4jee.interfaces.IDicionario;


@Singleton
@Entity
public class Dicionario extends BaseEntity<Serializable> implements Serializable, IDicionario {
	@Transient
	private static final long serialVersionUID = 1326222565183890233L;
	@Inject
	@Transient
	DicionarioDAO dicionarioDao;

	@Id
	@GeneratedValue
	@Column(name = "dicionario_id")
	private Long id;
	
	@ElementCollection
	@MapKeyColumn(name = "metodos")
	@CollectionTable(name = "lista", joinColumns = @JoinColumn(name = "dicionario_id"))
	private	Map<String, Integer> listaMetodos = new HashMap<String, Integer>();

	
	@PostConstruct
	public void postConstrutor(){
		
		
	}
	
	/* (non-Javadoc)
	 * @see me.costa.gustavo.saad4jee.entity.IDicionario#getPosicao(java.lang.String)
	 */
	@Override
	public Integer getPosicao(String metodo) {
		Integer posicao = getListaMetodos().get(metodo);
		Integer proximo;
		if (posicao == null) {
			if (!getListaMetodos().isEmpty()) {
				proximo = localizaUltimaPosicao();
				getListaMetodos().put(metodo, ++proximo);
			} else {
				proximo = 0;
				getListaMetodos().put(metodo, proximo);

			}
			
			return proximo;
		}
		return posicao;
	}

	/* (non-Javadoc)
	 * @see me.costa.gustavo.saad4jee.entity.IDicionario#localizaUltimaPosicao()
	 */
	@Override
	public Integer localizaUltimaPosicao() {
		return getListaMetodos().entrySet().stream()
				.max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
	}

	public void imprimir() {
		Map<String, Integer> listaMetodos2 = getListaMetodos();
		for (Entry<String, Integer> metodo : listaMetodos2.entrySet()) {
			System.out.println(metodo.getKey() + " - " + metodo.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see me.costa.gustavo.saad4jee.entity.IDicionario#getId()
	 */
	@Override
	public Serializable getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see me.costa.gustavo.saad4jee.entity.IDicionario#getListaMetodos()
	 */
	@Override
	public Map<String, Integer> getListaMetodos() {
		return listaMetodos;
	}

	/* (non-Javadoc)
	 * @see me.costa.gustavo.saad4jee.entity.IDicionario#setListaMetodos(java.util.Map)
	 */
	@Override
	public void setListaMetodos(Map<String, Integer> listaMetodos) {
		this.listaMetodos = listaMetodos;
	}

}
