package me.costa.gustavo.saad4jee.interceptors;

import java.lang.reflect.Method;
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
import me.costa.gustavo.saad4jee.enums.Comandos;
import me.costa.gustavo.saad4jee.ia.RobotDectectAnomalia;
import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

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
	private final Logger LOGGER = Logger.getLogger(RobotDetectIntercept.class.getName());

	@Inject
	HttpServletRequest httpRequest;

	@Inject
	@SalvarRobotInstanciaEvent
	private Event<RobotDetectInstancia> salvarInstanciaMessageEvent;

	@Inject
	RobotDetectDataSet dataSet;

	@Inject
	RobotDectectAnomalia robotDectectAnomalia;

	private Comandos[] comandos;

	@AroundInvoke
	public Object around(InvocationContext jp) throws Throwable {
		recuperarParametrosAnotacao(jp);
		LOGGER.log(Level.INFO, "Intercept");
		if (httpRequest != null) {
			LOGGER.log(Level.INFO, "HttpRequest nao eh nulo");
			String remoteAddr = httpRequest.getRemoteAddr();
			LOGGER.log(Level.INFO, "RemoteAddr: " + remoteAddr);
			if (remoteAddr != null) {
				RobotDetectInstancia instancia = dataSet.recebeEstimuloRequisicao(remoteAddr);
				if (instancia != null) {
					salvarInstanciaMessageEvent.fire(instancia);
					if (robotDectectAnomalia.isAnomalia(instancia)) {
						for (Comandos comandos2 : comandos) {
							comandos2.executar(new HttpRequestWrap(httpRequest));
						}
						
					}
				}

			}
		}
		try {
			return jp.proceed();
		} finally {
			LOGGER.log(Level.INFO, "Metodo executado");
		}
	}

	private void recuperarParametrosAnotacao(InvocationContext jp) {
		Method method = jp.getMethod();
		RobotDetect myAnnotation = method.getDeclaredAnnotation(RobotDetect.class);
		comandos = myAnnotation.comandos();
	}

}
