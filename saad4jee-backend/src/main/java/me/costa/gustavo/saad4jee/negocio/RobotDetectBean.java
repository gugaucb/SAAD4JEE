package me.costa.gustavo.saad4jee.negocio;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import me.costa.gustavo.saad4jee.annotations.SalvarRobotInstanciaEvent;
import me.costa.gustavo.saad4jee.entity.RobotDetectDataSet;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.ia.RobotDectectAnomalia;
import me.costa.gustavo.saad4jee.util.TrapSenderVersion2;

@Named
public class RobotDetectBean implements Bean {
	@Inject
	private RobotDetectDataSet dataSet;

	@Inject
	private RobotDectectAnomalia anomalia;

	@Inject
	private TrapSenderVersion2 trapSenderVersion2;

	public void salvarInstancia(@Observes @SalvarRobotInstanciaEvent RobotDetectInstancia instancia) {
		if (instancia != null && instancia.getCaracteristicas().size() > 0) {
			dataSet.salvarInstancia(instancia);
			if (anomalia.isAnomalia(instancia)) {
				System.out.println("Anomalia");
				// trapSenderVersion2.sendTrap_Version2(t, metodo);
			}
		}
	}

}
