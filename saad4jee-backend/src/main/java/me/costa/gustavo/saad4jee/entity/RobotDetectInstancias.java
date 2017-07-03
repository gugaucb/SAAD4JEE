package me.costa.gustavo.saad4jee.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Named
@RequestScoped
public class RobotDetectInstancias extends BaseEntity<Serializable> implements Serializable{
	@Transient
	private static final long serialVersionUID = 5834299037501917146L;
	@Id
	@GeneratedValue
	private Long id;
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy="instancias")
	private List<RobotDetectInstancia> listaInstancia = new ArrayList<RobotDetectInstancia>();
	
	/**
	 * 
	 * @param listaCaracteristicas - Foto das caracteristas monitoradas em um dado momento
	 */
	public void add(RobotDetectInstancia listaCaracteristicas) {
		getInstancias().add(listaCaracteristicas);
	}
	/**
	 * 
	 * @return A ultima instancia inserida.
	 */
	public RobotDetectInstancias getUltimaInstanciaEmInstancias(){
		if(getInstancias().isEmpty()){
			return null;
		}
		return subList(getInstancias().subList(getInstancias().size()-1, getInstancias().size()));
	}
	
	public RobotDetectInstancia getUltimaInstancia(){
		if(getInstancias().isEmpty()){
			return null;
		}
		return getInstancias().get(getInstancias().size()-1);
	}
	
	private RobotDetectInstancias subList(List<RobotDetectInstancia> subInstancias){
		RobotDetectInstancias instanciasTemp = new RobotDetectInstancias();
		instanciasTemp.addAll(subInstancias);
		return instanciasTemp;
		
	}
	
	private void addAll(List<RobotDetectInstancia> subInstancias) {
		this.getInstancias().addAll(subInstancias);
		
	}
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isEmpty(){
		return getInstancias().isEmpty();
	}
	public List<RobotDetectInstancia> getListInstancias() {
		return getInstancias();
	}
	public int size() {
		return getInstancias().size();
	}
	public List<RobotDetectInstancia> getInstancias() {
		return listaInstancia;
	}
	public void setInstancias(List<RobotDetectInstancia> instancias) {
		this.listaInstancia = instancias;
	}
}
