package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;


/**
 *
 * @author llm
 */
public class servidor {

    public servidor(){}

    public static void main(String args[])
    {
        if (args.length == 0) {
			args = new String[4];
	    	args[0] = "-ORBInitialPort";
	    	args[1] = "2500";
	    	args[2] = "-ORBInitialHost";
	    	args[3] = "localhost";
		}
        
        try
        {
            ORB orb = ORB.init(args, ((java.util.Properties) (null)));
            BarbeiroImpl barbeiroimpl = new BarbeiroImpl();
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();
            org.omg.CORBA.Object obj = poa.servant_to_reference(barbeiroimpl);
            Barbeiro barbeiro = BarbeiroHelper.narrow(obj);
            org.omg.CORBA.Object obj1 = orb.resolve_initial_references("NameService");
            NamingContextExt namingcontextext = NamingContextExtHelper.narrow(obj1);
            String s = "barbeiro";
            org.omg.CosNaming.NameComponent anamecomponent[] = namingcontextext.to_name(s);
            namingcontextext.rebind(anamecomponent, barbeiro);
            System.out.println("Servidor aguardando requisicoes ....");
            orb.run();
        }
        catch(Exception exception)
        {
            System.err.println("ERRO: " + exception);
            exception.printStackTrace(System.out);
        }
        System.out.println("Encerrando o Servidor.");
    }

}