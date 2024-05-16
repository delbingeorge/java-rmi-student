import java.rmi.*;

public class MyServer {
    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(5015);
            MarkCalc stub = new CalculationImplementation();
            Naming.rebind("rmi://localhost:5015/student", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
