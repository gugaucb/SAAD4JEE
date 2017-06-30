package me.costa.gustavo.saad4jee.negocio;

import java.time.Duration;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.annotations.ExcecaoEvent;
import me.costa.gustavo.saad4jee.annotations.MetodoEvent;
import me.costa.gustavo.saad4jee.entity.DataSet;
import me.costa.gustavo.saad4jee.entity.Instancia;
import me.costa.gustavo.saad4jee.ia.Anomalia;
import me.costa.gustavo.saad4jee.util.TrapSenderVersion2;


@Named
public class MonitoringBean implements Bean {
	@Inject
	DataSet dataSet;
	
	@Inject 
	Anomalia anomalia;
	
	@Inject
	TrapSenderVersion2 trapSenderVersion2;
	
	
	
	public void recebeEstimuloExcecao(@Observes @ExcecaoEvent Throwable t){
		System.out.println("EstimuloExcecao");
		dataSet.recebeEstimuloExcecao(t.toString());
		if(anomalia.isAnomalia()){
			System.out.println("Anomalia");
			//trapSenderVersion2.sendTrap_Version2(t, metodo);
		}
	}
	
	public void salvarInstancia(@Observes Instancia instancia){
		dataSet.salvarInstancia(instancia);
		if(anomalia.isAnomalia()){
			System.out.println("Anomalia");
			//trapSenderVersion2.sendTrap_Version2(t, metodo);
		}
	}
	
	public void recebeEstimuloMetodo(@Observes @MetodoEvent String metodo_duration){
		String[] temp = metodo_duration.split(";");
		recebeEstimuloMetodo(temp[0], Duration.ofMillis(Long.parseLong(temp[1])));
		if(anomalia.isAnomalia()){
			System.out.println("Anomalia");
			//trapSenderVersion2.sendTrap_Version2(t, metodo);
		}
	}
	
	public void recebeEstimuloMetodo(String metodo, Duration duration){
		dataSet.recebeEstimuloMetodo(metodo, duration);
	}
	
}
