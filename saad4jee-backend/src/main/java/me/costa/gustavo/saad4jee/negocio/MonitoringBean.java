package me.costa.gustavo.saad4jee.negocio;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.annotations.SalvarMonitorInstanciaEvent;
import me.costa.gustavo.saad4jee.entity.DataSet;
import me.costa.gustavo.saad4jee.entity.Instancia;
import me.costa.gustavo.saad4jee.ia.Anomalia;
import me.costa.gustavo.saad4jee.util.TrapSenderVersion2;


@Named
public class MonitoringBean implements Bean {
	@Inject
	private DataSet dataSet;
	
	@Inject 
	private Anomalia anomalia;
	
	@Inject
	private TrapSenderVersion2 trapSenderVersion2;
	
	
	
	
	public void salvarInstancia(@Observes @SalvarMonitorInstanciaEvent Instancia instancia){
		dataSet.salvarInstancia(instancia);
		if(anomalia.isAnomalia()){
			System.out.println("Anomalia");
			//trapSenderVersion2.sendTrap_Version2(t, metodo);
		}
	}
	
	
}
