//package ro.etr.victorbet.processingapp.service;
//
//import ro.etr.victorbet.processingapp.infrastructure.RandomTextClient;
//import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
//
//public class ProcessRequestRunnable implements Runnable {
//
//	private int index;
//	private ProcessRequestParams params;
//
//	public ProcessRequestRunnable(int index, ProcessRequestParams params) {
//		this.index = index;
//		this.params = params;
//		
//	}
//	
//	@Override
//	public void run() {
//		RandomTextResponse response = new RandomTextClient().requestData(index, params);		
//	}
//
//}
