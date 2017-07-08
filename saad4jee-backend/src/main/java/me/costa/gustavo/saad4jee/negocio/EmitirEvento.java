package me.costa.gustavo.saad4jee.negocio;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.annotations.RoboDetectEvent;
import me.costa.gustavo.saad4jee.interfaces.ICommand;
import me.costa.gustavo.saad4jee.wrappers.HttpRequestWrap;

@Named
@RequestScoped
public class EmitirEvento implements ICommand {
	private final Logger LOGGER = Logger.getLogger(EmitirEvento.class.getName());

	@Override
	public void executar(HttpRequestWrap httpRequest) {
		BeanManager beanManager = CDI.current().getBeanManager();
		beanManager.fireEvent("Robo identificado EmitirEvento. IP: "+httpRequest.getHttpRequest().getRemoteAddr(), new AnnotationLiteral<RoboDetectEvent>() {
		});
		LOGGER.log(Level.INFO, "Robo identificado EmitirEvento. IP: "+httpRequest.getHttpRequest().getRemoteAddr());
	}

}
