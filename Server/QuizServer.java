package Quiz.Server;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import compute.Compute;
import compute.Task;

public class QuizServer extends UnicastRemoteObject implements Compute {

    public EchoServer() throws RemoteException {
        // nothing to initialise for this server
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }
}