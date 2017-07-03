package me.costa.gustavo.saad4jee.interceptors;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import me.costa.gustavo.saad4jee.annotations.RobotDetect;
import me.costa.gustavo.saad4jee.annotations.SalvarRobotInstanciaEvent;
import me.costa.gustavo.saad4jee.entity.RobotDetectDataSet;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.exceptions.RobotDetectException;
import me.costa.gustavo.saad4jee.ia.RobotDectectAnomalia;

/**
 * Inteceptor aplicado aos metodos que possuem a annotation Monitoring. O
 * objetivo é capturar as excecoes e tempo de execução do metodo para
 * identificar possiveis erros.
 * 
 * @author gugaucb
 *
 */

@RobotDetect
@Interceptor
@Named
@Priority(Interceptor.Priority.APPLICATION)
public class RobotDetectIntercept {
	private final Logger LOGGER = Logger.getLogger( RobotDetectIntercept.class.getName() ); 
	
	@Inject
	HttpServletRequest httpRequest;

	@Inject
	@SalvarRobotInstanciaEvent
	private Event<RobotDetectInstancia> salvarInstanciaMessageEvent;

	@Inject
	RobotDetectDataSet dataSet;

	@Inject
	RobotDectectAnomalia robotDectectAnomalia;

	@AroundInvoke
	public Object around(InvocationContext jp) throws Throwable {
		LOGGER.log(Level.INFO, "Intercept");
		if (httpRequest != null) {
			LOGGER.log(Level.INFO, "HttpRequest nao eh nulo");
			String remoteAddr = httpRequest.getRemoteAddr();
			LOGGER.log(Level.INFO, "RemoteAddr: "+remoteAddr);
			if(remoteAddr!=null){
				RobotDetectInstancia instancia = dataSet.recebeEstimuloRequisicao(remoteAddr);
				if(instancia!=null){
					salvarInstanciaMessageEvent.fire(instancia);
				}
				if (robotDectectAnomalia.isAnomalia(instancia)) {
					throw new RobotDetectException("Robo identificado.");
				}
			}
		}
		try{
			return jp.proceed();
		}finally{
			LOGGER.log(Level.INFO, "Metodo executado");
		}
	}

}
