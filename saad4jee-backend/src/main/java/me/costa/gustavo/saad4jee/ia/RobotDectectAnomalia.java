package me.costa.gustavo.saad4jee.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.costa.gustavo.saad4jee.entity.RobotDetectDataSet;
import me.costa.gustavo.saad4jee.entity.RobotDetectDicionario;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancia;
import me.costa.gustavo.saad4jee.entity.RobotDetectInstancias;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.LOF;

@Singleton
public class RobotDectectAnomalia {
	private int histBins = 20;
	private int current = 0;
	private double max = Double.MIN_VALUE;
	private double min = Double.MAX_VALUE;
	private LOF lof;
	private boolean treinado = false;
	@Inject
	private RobotDetectDataSet datasetClass;

	@Inject
	RobotDetectDicionario dicionario;
	
	private final Logger LOGGER = Logger.getLogger( RobotDectectAnomalia.class.getName() ); 
	
	public void zerarIA(){
		treinado = false;
		lof = new LOF();
		datasetClass.zerarIA();
	}

	/*private List<double[]> criarHistograma(Instancias instancias) {
		// Create delta_t histograms
		List<double[]> dataHist = new ArrayList<double[]>();
		for (Instancia sample : instancias.getListInstancias()) {
			geraDataHist(instancias, dataHist, sample);
		}

		// Normalize histograms
		for (double[] histogram : dataHist) {
			int sum = 0;
			for (double v : histogram) {
				sum += v;
			}
			for (int i = 0; i < histogram.length; i++) {
				histogram[i] /= 1.0 * sum;
			}
		}
		System.out.println("Total histogranms:" + dataHist.size());
		return dataHist;
	}*/

	/*private void geraDataHist(Instancias rawData, List<double[]> dataHist, Instancia sample) {
		histBins = dicionario.localizaUltimaPosicao();
		double[] histogram = new double[histBins];
		for (Double value : sample.getCaracteristicas()) {
			if (value != null) {
				int bin = toBin(normalize(value.doubleValue(), min, max), histBins);
				histogram[bin]++;
				current++;
				if (current == calculaWinSize(rawData)) {
					current = 0;
					dataHist.add(histogram);
					histogram = new double[histBins];
				}
			}
		}
		dataHist.add(histogram);
	}*/

	/*private Instances createDBOnTheFly(List<double[]> dataHist) {
		
		 * Create DB on-the-fly
		 
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < histBins; i++) {
			attributes.add(new Attribute("Hist-" + i));
		}
		Instances dataset = new Instances("My dataset", attributes, dataHist.size());
		for (double[] histogram : dataHist) {
			dataset.add(new DenseInstance(1.0, histogram));
		}
		System.out.println("Dataset created: " + dataset.size());
		return dataset;
	}*/

	private Instances criarInstancias(RobotDetectInstancias instancias) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Integer ultimaPosicao = dicionario.localizaUltimaPosicao();
		for (int i = 0; i < RobotDetectInstancia.QUANT_POSICAO_INIT; i++) {
			attributes.add(new Attribute("Attr-" + i));	
		}
		
		Instances dataset = new Instances("My dataset", attributes, ultimaPosicao);
		for (RobotDetectInstancia instancia : instancias.getInstancias()) {
			if(instancia.getCaracteristicas().size()>0){
				LOGGER.log(Level.INFO, "Instancia analisada se eh anomalia.");
				instancia.imprimir();
				dataset.add(new DenseInstance(1.0, Stream.of(instancia.getCaracteristicas().toArray(new Double[RobotDetectInstancia.QUANT_POSICAO_INIT])).mapToDouble(Double::doubleValue).toArray()));
			}
		}
		return dataset;
		
	}

	private int calculaWinSize(RobotDetectInstancias instancias) {
		return Math.round(instancias.size() * 0.30F);
	}

	private LOF getModelo() {
		return lof;
	}

	public boolean isAnomalia(RobotDetectInstancia instancia) {
		LOF modelo = getModelo();
		if (!treinado) {
			return false;
		}

		if (datasetClass.getInstancias() == null || datasetClass.getInstancias().isEmpty()) {
			return false;
		}

		if(modelo==null){
			return false;
		}
		try {
			List<RobotDetectInstancia> listaInstancia = new ArrayList<RobotDetectInstancia>();
			listaInstancia.add(instancia);
			RobotDetectInstancias instancias = new RobotDetectInstancias();
			instancias.setInstancias(listaInstancia);
			Instances dataset = criarInstancias(instancias);
			
			Instances predict = Filter.useFilter(dataset, modelo);
			for (Instance inst : predict) {
				double densidade = inst.value(inst.numAttributes() - 1);
				LOGGER.log(Level.INFO, "Densidade calculada: " + densidade);
				if (densidade < 1 || densidade >= 2) {
					return true;
				}
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Falha ao verificar se eh uma anomalia", e);
			return false;
		}

		return false;
	}

	public LOF buildModel() {
		try {
			if (datasetClass.getInstancias() == null || datasetClass.getInstancias().isEmpty()) {
				return null;
			}
			//Instances dataset = createDBOnTheFly(criarHistograma(datasetClass.getInstancias()));
			Instances dataset = criarInstancias(datasetClass.getInstancias());
			// split data to train and test
			Instances trainData = dataset;//.testCV(2, 0);
			//Instances testData = dataset.testCV(2, 1);
			System.out.println("Train: " + trainData.size()); //+ "\nTest:" + testData.size());

			// load the train data to a k-nn algorithm
			lof = new LOF();
			System.out.println("InputFormat");
			lof.setInputFormat(trainData);
			System.out.println("Fim InputFormat");
			lof.setOptions(new String[] { "-min", "10", "-max", "40", "-num-slots", "2" });
			for (Instance inst : trainData) {
				lof.input(inst);
			}
			System.out.println("input Concluido");

			if (!lof.isNewBatch()) {
				treinado = false;
				lof.preExecution();
				System.out.println("LOF loading...");
				lof.batchFinished();
				System.out.println("LOF loaded");
				lof.postExecution();
			}

			/*Instances testDataLofScore = Filter.useFilter(testData, lof);

			for (Instance inst : testDataLofScore) {
				System.out.println(inst.value(inst.numAttributes() - 1));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		treinado = true;
		return lof;
	}

	/**
	 * Normalizes value to interval [0, 1]
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	static double normalize(double value, double min, double max) {
		return value / (max - min);
	}

	/**
	 * Returns a bin in range [0, bins). Assumes value is normalized to interval
	 * [0, 1]
	 * 
	 * @param normalizedValue
	 * @param bins
	 * @return bin no
	 */
	static int toBin(double normalizedValue, int bins) {
		if (normalizedValue == 1.0)
			return bins - 1;
		return (int) (normalizedValue * bins);
	}

}
