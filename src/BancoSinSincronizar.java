import java.util.concurrent.locks.*;


public class BancoSinSincronizar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Banco b = new Banco();
		for(int i=0; i<100; i++){
			EjecucionTransferencia r = new EjecucionTransferencia(b, i, 2000);
			Thread t= new Thread(r);
			t.start();
		}

	}

}

class Banco{
	private  final double[] cuentas;
	
	//private  Lock cierreBanco=new ReentrantLock();
	
	//private Condition saldoSuficiente;
	
	public Banco(){
		cuentas= new double[100];
		for (int i=0; i<cuentas.length; i++){
			cuentas[i]=2000;
		}
		//saldoSuficiente=cierreBanco.newCondition();
	}
	
	public synchronized void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) throws InterruptedException{
		//cierreBanco.lock();
		//try{
		while(cuentas[cuentaOrigen]<cantidad){
			//System.out.println("cantidad insuficiente: " + cuentaOrigen + " Saldo: " + cuentas[cuentaOrigen] + " ..." + cantidad);
			//return;
			//saldoSuficiente.await();
			
			wait();
			//System.out.println("HILO DORMIDO");
		} 
		
		System.out.println(Thread.currentThread());
		
		System.out.println("SALDO EN CUENTA ORIGEN: " + cuentas[cuentaOrigen]);
		cuentas[cuentaOrigen]-=cantidad; 
		System.out.printf("%10.2f de %d para %d" , cantidad, cuentaOrigen, cuentaDestino);
		
		
		cuentas[cuentaDestino]+=cantidad;
		System.out.println("SALDO EN CUENTA DESTINO: " + cuentas[cuentaDestino]);
		System.out.printf("Saldo total: %10.2f%n", getSaldoTotal());
		//saldoSuficiente.signalAll();
		/*} finally {
			cierreBanco.unlock();
		}*/
		notifyAll();
	}
	
	public double getSaldoTotal(){
		double sumaCuentas=0;
		for(double a: cuentas){
			sumaCuentas+=a;
		}
		return sumaCuentas;
	}
}

class EjecucionTransferencia implements Runnable{
	
	private Banco banco;
	private int deLaCuenta;
	private double cantidadMax;
	
	public EjecucionTransferencia(Banco b, int de, double max){
		banco=b;
		deLaCuenta=de;
		cantidadMax=max;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		while(true){
			int paraLaCuenta=(int)(100*Math.random());
			double cantidad=cantidadMax*Math.random();
			banco.transferencia(deLaCuenta, paraLaCuenta, cantidad);
			
				Thread.sleep((int)(Math.random()*10));
			
			} 
		}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
