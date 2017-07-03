package me.costa.gustavo.saad4jee.interceptors;

import java.time.Duration;
import java.time.Instant;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import me.costa.gustavo.saad4jee.annotations.Monitoring;
import me.costa.gustavo.saad4jee.annotations.SalvarMonitorInstanciaEvent;
import me.costa.gustavo.saad4jee.entity.DataSet;
import me.costa.gustavo.saad4jee.entity.Dicionario;
import me.costa.gustavo.saad4jee.entity.Instancia;


/**
 * Inteceptor aplicado aos metodos que possuem a annotation Monitoring. 
 * O objetivo é capturar as excecoes e tempo de execução do metodo para
 * identificar possiveis erros.
 * @author gugaucb
 *
 */

@Monitoring
@Interceptor
@Named
@Priority(Interceptor.Priority.APPLICATION)
public class MonitoringIntercept {
	
	@Inject
    HttpServletRequest httpRequest;
	
	
	@Inject
	@SalvarMonitorInstanciaEvent
	private Event<Instancia> salvarInstanciaMessageEvent;
	
	@Inject
	Dicionario dicionario;
	
	@AroundInvoke
	public Object around(InvocationContext jp) throws Throwable {
		Instancia instancia = new Instancia();
		instancia.setDicionario(dicionario);
		final String metodo = jp.getMethod().getName();
		final Instant start = Instant.now();
		try {
			return jp.proceed();
		}catch(Throwable t){
			instancia = DataSet.recebeEstimuloExcecaoV1(instancia, t.toString());
			throw t;
		
		} finally {
			final Instant end = Instant.now();
			instancia = DataSet.recebeEstimuloMetodoV1(instancia, metodo, Duration.between(start, end));
			salvarInstanciaMessageEvent.fire(instancia);
		}

	}

}
