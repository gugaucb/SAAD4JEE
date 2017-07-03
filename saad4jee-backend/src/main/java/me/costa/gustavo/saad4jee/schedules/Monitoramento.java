package me.costa.gustavo.saad4jee.schedules;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import me.costa.gustavo.saad4jee.daos.DicionarioDAO;
import me.costa.gustavo.saad4jee.daos.InstanciasDAO;
import me.costa.gustavo.saad4jee.daos.RobotDetectDicionarioDAO;
import me.costa.gustavo.saad4jee.daos.RobotDetectInstanciaDAO;
import me.costa.gustavo.saad4jee.daos.RobotDetectInstanciasDAO;
import me.costa.gustavo.saad4jee.entity.DataSet;
import me.costa.gustavo.saad4jee.entity.Dicionario;
import me.costa.gustavo.saad4jee.entity.RobotDetectDataSet;
import me.costa.gustavo.saad4jee.entity.RobotDetectDicionario;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancias;
import me.costa.gustavo.saad4jee.interceptors.RobotDetectIntercept;

@Singleton
@Startup
public class Monitoramento {
	private final Logger LOGGER = Logger.getLogger(RobotDetectIntercept.class.getName());

	@Inject
	private InstanciasDAO instanciasDAO;

	@Inject
	private RobotDetectInstanciasDAO robotDetectinstanciasDAO;

	@Inject
	private RobotDetectInstanciaDAO robotDetectinstanciaDAO;

	@Inject
	private DicionarioDAO dicionarioDao;

	@Inject
	private DataSet dataSet;

	@Inject
	private Dicionario dicionario;

	@Inject
	private RobotDetectDicionarioDAO robotDetectDicionarioDao;

	@Inject
	private RobotDetectDataSet robotDetectDataSet;

	@Inject
	private RobotDetectDicionario robotDetectDicionario;

	private boolean salvoMonitoramento = false;
	private boolean salvoRequisicaoPorMinuto = false;

	@Schedule(second = "1", minute = "*/3", hour = "*", info = "Report every 1 Minutes", persistent = false)
	private void execucaoPorMinuto() {
		salvarEstimulos();
		salvarRequisicaoPorMinuto();
	}

	public void salvarRequisicaoPorMinuto() {
		try {
			robotDetectDataSet.zeraQuantContadorPorMinuto();

			RobotDetectInstancias instancias = robotDetectDataSet.getInstancias();
			if (!salvoRequisicaoPorMinuto) {
				salvoRequisicaoPorMinuto = true;
				List<RobotDetectDicionario> dicionarios = robotDetectDicionarioDao.findAll(RobotDetectDicionario.class);
				if (dicionarios != null && !dicionarios.isEmpty()) {
					robotDetectDicionario = dicionarios.get(0);
				}
				Map<String, Integer> listaMetodos = robotDetectDicionario.getListaMetodos();
				if (listaMetodos == null || listaMetodos.isEmpty()) {
					robotDetectDicionarioDao.save(robotDetectDicionario);
					System.out.println("RobotDetect.salvarEstimulos()");
				}

				if (instancias != null || !instancias.isEmpty()) {
					robotDetectinstanciasDAO.save(instancias);
				}
			} else {
				robotDetectinstanciasDAO.merge(instancias);
				robotDetectDicionarioDao.merge(robotDetectDicionario);
				System.out.println("RobotDetect.AtualizarEstimulos() - Total Instancias: " + instancias.size());
			}
			System.out.println("RobotDetect Salvo" + salvoMonitoramento);
		} catch (Exception e) {
			salvoRequisicaoPorMinuto = false;
			LOGGER.log(Level.WARNING, "HttpRequest nao eh nulo", e);
		}
	}

	private void salvarInstancias() {
		for (RobotDetectInstancia instancia : robotDetectDataSet.getInstancias().getInstancias()) {
			robotDetectinstanciaDAO.merge(robotDetectDataSet.getInstancias());
		}
	}

	public void salvarEstimulos() {
		dataSet.calcularMedia();
		dataSet.somarMinuto();
		// TODO Refatorar
		System.out.println("Salvo" + salvoMonitoramento);
		if (!salvoMonitoramento) {
			salvoMonitoramento = true;
			List<Dicionario> dicionarios = dicionarioDao.findAll(Dicionario.class);
			if (dicionarios != null && !dicionarios.isEmpty()) {
				dicionario = dicionarios.get(0);
			}
			if (dicionario.getListaMetodos() == null || dicionario.getListaMetodos().isEmpty()) {
				instanciasDAO.save(dataSet.getInstancias());
				dicionarioDao.save(dicionario);
				System.out.println("Monitoramento.salvarEstimulos()");
			}
		} else {
			instanciasDAO.merge(dataSet.getInstancias());
			dicionarioDao.merge(dicionario);
			System.out.println(
					"Monitoramento.AtualizarEstimulos() - Total Instancias: " + dataSet.getInstancias().size());
		}
		System.out.println("Salvo" + salvoMonitoramento);
	}

}
