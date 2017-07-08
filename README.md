# System Alert Anomaly Detection For JEE - SAAD4JEE

## Objetivo

O objetivo do projeto é aplicar o algoritmo LOF  (Local Outlier Factor) para identificar o acesso de robos a serviços REST. O Algoritmo LOF calcula a pontuação "outlier" com base no tempo entre requisições realizada pelo usuário ou robô aos serviços REST. 

## LOF  (Local Outlier Factor)

O algoritmo calcula a pontuação por meio da função de distância de vizinhos mais próximos são conectáveis. Ou seja, com base nas características informadas ao algoritmo (neste caso o tempo entre requisições) esse reduz as características em dos valores que representam o eixo x e y do plano cartesiano. 

![Grafico](https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/LOF.svg/400px-LOF.svg.png)

A partir dos pontos plotados em um gráfico bidimensional é aplicado o algoritmo LinearNNSearch para identificar a densidade de pontos próximos. Desta forma, quanto mais pontos plotados perto do ponto calculado com o tempo entre requisição informado maior a chance do tempo entre requisições representar um comportamento de um usuário válido. Por outro lado, quanto menos ponto próximo maior a chance de ser um comportamento fora do padrão podendo ser um robô. Densidades retornadas pelo algoritmo entre 1 a 2 representa que o tempo entre a requisição informada está dentro do padrão de tempo entre requisiçõe disponibilizado durante o treinamento do algoritmo.

## Como utilizar

### Maven
Após clonar o projeto e importá-lo na IDE, deve ser acrescentado a dependência abaixo no pom.xml do projeto.

``` 
    <dependency>
			<groupId>me.costa.gustavo</groupId>
			<artifactId>saad4jee-backend</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
``` 

### Implementação
No projeto deve ser criado uma classe que implemente a classe javax.ws.rs.container.ContainerRequestFilter e sobreescrever o metodo ContainerRequestFilter.filter

``` 
  @RobotDetect(comandos={Comandos.EmitirEvento, Comandos.EnviarTrap})
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.log(Level.INFO, "filter acionado");
	} 
  ```

## Referências
[1] Markus M. Breunig, Hans-Peter Kriegel, Raymond T. Ng, Jorg Sander (2000). LOF: Identifying Density-Based Local Outliers. ACM SIGMOD Record. 29(2):93-104.

