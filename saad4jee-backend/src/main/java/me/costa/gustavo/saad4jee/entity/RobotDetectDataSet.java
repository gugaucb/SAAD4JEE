package me.costa.gustavo.saad4jee.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.costa.gustavo.saad4jee.daos.RobotDetectInstanciasDAO;

/**
 * 
 * 
 * https://github.com/PacktPublishing/Machine-Learning-in-Java/blob/master/MLJ-Chapter7/src/Anomaly.java
 */

@Singleton
public class RobotDetectDataSet {
	private final Logger LOGGER = Logger.getLogger( RobotDetectDataSet.class.getName() ); 
	
	@Inject
	RobotDetectInstanciasDAO instanciasDao;

	@Inject
	private RobotDetectDicionario dicionario;
	
	private RobotDetectInstancias instancias = new RobotDetectInstancias();

	private LocalTime agora = LocalTime.now();
	private Map<String, IPAddress> listaDeIPs = new HashMap<String, IPAddress>();

	public void zerarIA() {
		instancias = new RobotDetectInstancias();
	}

	public RobotDetectInstancia recebeEstimuloRequisicao(String ip) {
		LOGGER.log(Level.INFO, "Recebendo Estimulo Requisicao");
		RobotDetectInstancia instancia = new RobotDetectInstancia();
		instancia.setDicionario(dicionario);
		IPAddress ipAddress = listaDeIPs.get(ip);
		LOGGER.log(Level.INFO, "IP remoto recuperado: "+ipAddress);
		if (ipAddress == null) {
			LOGGER.log(Level.INFO, "IPAddress nulo");
			ipAddress =new IPAddress(ip, LocalDateTime.now()); 
			LOGGER.log(Level.INFO, "IPAddress instanciado: \n"+ipAddress);
			listaDeIPs.put(ip, ipAddress);
			return null;
		} else {
			/*if(ipAddress.isNovaRequisicao()){
				ipAddress.iniciarContagemTempoEntreRequisicoes(LocalDateTime.now());
			}*/
			double tempoEntreRequisicoes = ipAddress.calcularTempoEntreRequisicoes(LocalDateTime.now());
			LOGGER.log(Level.INFO, "tempoEntreRequisicoes: "+tempoEntreRequisicoes);
			if (tempoEntreRequisicoes >= 0) {
				LOGGER.log(Level.INFO, "tempoEntreRequisicoes > 0");
				instancia.add("tempoMedioEntreRequisicoes", Double.valueOf(tempoEntreRequisicoes));
				
			}
			ipAddress.somarRequisicoes();
			LOGGER.log(Level.INFO, "Acrescentado Requisicao IPAddress: "+ipAddress);
			listaDeIPs.put(ip, ipAddress);
			LOGGER.log(Level.INFO, "Qnt elementos de listaDeIPs: "+listaDeIPs.size());
		}
		return instancia;
	}

	public RobotDetectInstancia incluirRequisicoesPorMinuto(RobotDetectInstancia instancia, IPAddress ipAddress) {
		Double totalRequisicoes = ipAddress.getRequisicoes();
		if (totalRequisicoes > 0) {
			long minutosTranscorridos = Duration.between(agora, LocalTime.now()).toMinutes();
			if (minutosTranscorridos > 0) {
				instancia.add("requisicaoPorMinuto", totalRequisicoes / minutosTranscorridos);
			}
		}
		return instancia;
	}

	public void salvarInstancia(RobotDetectInstancia instancia) {
		instancias.add(instancia);
		imprimeInstancias(instancia);
		dicionario.imprimir();
	}

	public void zeraQuantContadorPorMinuto() {
		listaDeIPs.forEach((k, v) -> {
			registrarRequisicoesPorMinuto(v);
			v.zerarRequisicoes();
		});
	}

	private void registrarRequisicoesPorMinuto(IPAddress ipAddress) {
		RobotDetectInstancia instancia = new RobotDetectInstancia();
		instancia.setDicionario(dicionario);
		salvarInstancia(incluirRequisicoesPorMinuto(instancia, ipAddress));
	}

	public static void imprimeInstancias(RobotDetectInstancia instancia) {
		instancia.imprimir();
	}

	public RobotDetectInstancias getInstancias() {
		return instancias;
	}

	public void setInstancias(RobotDetectInstancias instancias) {
		this.instancias = instancias;

	}
}
