package opc.sdk.eval;

import java.util.Timer;
import java.util.TimerTask;

public class Evaluation {
	private static final int secPerMin = 60;
	private static final int milliPerSec = 1000;
	private Timer timer = null;
	private int mins = -1;

	public Evaluation() {
		// Default 90
		this.mins = 90;
		this.timer = new Timer("Evaluation", true);
	}

	public Evaluation(int mins) {
		this();
		this.mins = mins;
	}

	public void start() {
		TimerTask evalTask = new TimerTask() {
			@Override
			public void run() {
				evaluationOutputShutdown();
				System.exit(0);
			}
		};
		long delay = this.mins * Evaluation.secPerMin * Evaluation.milliPerSec;
		this.timer.schedule(evalTask, delay);
		evaluationOutputStartup();
	}

	private void evaluationOutputStartup() {
		System.err.println();
		System.err.println("----------------------------------------------------");
		System.err.println("EVALUATION VERSION");
		System.err.println("This is an evaluation version of the Bichler Technologies UA SDK.");
		System.err.println("The evaluation version stops after " + this.mins + " minutes.");
		System.err.println("--------------------------------------------------");
		System.err.println();
	}

	private void evaluationOutputShutdown() {
		System.err.println();
		System.err.println("----------------------------------------------------");
		System.err.println("EVALUATION VERSION");
		System.err.println("The evaluation version has been stopped after " + this.mins + " minutes.");
		System.err.println("--------------------------------------------------");
		System.err.println();
	}
}
