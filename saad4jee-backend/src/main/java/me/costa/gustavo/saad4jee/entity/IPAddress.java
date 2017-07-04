package me.costa.gustavo.saad4jee.entity;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Entity;

@Entity
public class IPAddress {
	private String ip;
	private LocalDateTime inicio;
	private LocalDateTime fim;
	private double requisicoesPorMinuto = 1.0;
	
	public IPAddress(){
		
	}
	
	public IPAddress(String ip){
		this.ip = ip;
	}
	
	public IPAddress(String ip, LocalDateTime now) {
		this.ip = ip;
		this.inicio = now;
	}

	public double calcularTempoEntreRequisicoes(LocalDateTime dataHora){
		if(inicio==null){
			inicio = dataHora;
			return -1.0;
		}else{
			fim = dataHora;
			long millis = Duration.between(inicio, fim).toMillis();
			iniciarNovaContagem();
			return millis;	
		}
	}

	private void iniciarNovaContagem() {
		this.inicio = fim;
		this.fim = null;
	}
	
	public void zerarRequisicoes(){
		requisicoesPorMinuto=1.0;
	}
	
	public double somarRequisicoes(){
		return requisicoesPorMinuto++;
	}

	public Double getRequisicoes() {
		return requisicoesPorMinuto;
	}
	
	@Override
	public String toString() {
		StringBuilder tostring = new StringBuilder();
		
		tostring.append("ip: "+ip+"\n");
		if(inicio!=null)
			tostring.append("inicio: "+inicio.toString()+"\n");
		if(fim!=null)
			tostring.append("fim: "+fim.toString()+"\n");
		tostring.append("requisicoesPorMinuto: "+requisicoesPorMinuto+"\n");
		return tostring.toString();
	}

	public boolean isNovaRequisicao() {
		return this.inicio==null?true:false;
	}

	public void iniciarContagemTempoEntreRequisicoes(LocalDateTime now) {
		this.inicio = now;
	}
	
}
