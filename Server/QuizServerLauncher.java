import java.rmi.RMISecurityManager;
import java.rmi.registry.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class QuizServerLauncher {
	private void launch() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			LocateRegistry.createRegistry(1099);
			QuizServer server = new QuizServer();
			String registryHost = "//localhost/";
			String serviceName = "quiz";
			Naming.rebind(registryHost + serviceName, server);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		QuizServerLauncher qsl = new QuizServerLauncher();
		qsl.launch();
	}
}

