/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

/**
 *
 * @author llm
 */
public class clientes {

    public clientes(){}

    public static void main(String args[]) {

        if (args.length == 0) {
            args = new String[4];
            args[0] = "-ORBInitialPort";
            args[1] = "2500";
            args[2] = "-ORBInitialHost";
            args[3] = "150.162.65.126";
        }

        try {
            short num = 1;
            ORB orb = ORB.init(args, ((java.util.Properties) (null)));
            ClienteImpl clienteimpl1 = new ClienteImpl(num);
            num = 2;
            ClienteImpl clienteimpl2 = new ClienteImpl(num);
            num = 3;
            ClienteImpl clienteimpl3 = new ClienteImpl(num);
            num = 4;
            ClienteImpl clienteimpl4 = new ClienteImpl(num);
            num = 5;
            ClienteImpl clienteimpl5 = new ClienteImpl(num);
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();
            org.omg.CORBA.Object obj1 = poa.servant_to_reference(clienteimpl1);
            org.omg.CORBA.Object obj2 = poa.servant_to_reference(clienteimpl2);
            org.omg.CORBA.Object obj3 = poa.servant_to_reference(clienteimpl3);
            org.omg.CORBA.Object obj4 = poa.servant_to_reference(clienteimpl4);
            org.omg.CORBA.Object obj5 = poa.servant_to_reference(clienteimpl5);
            NamingContextExt namingcontext = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
            namingcontext.rebind(namingcontext.to_name("cliente1"), obj1);
            namingcontext.rebind(namingcontext.to_name("cliente2"), obj2);
            namingcontext.rebind(namingcontext.to_name("cliente3"), obj3);
            namingcontext.rebind(namingcontext.to_name("cliente4"), obj4);
            namingcontext.rebind(namingcontext.to_name("cliente5"), obj5);
            clienteimpl1.inicia();
            clienteimpl2.inicia();
            clienteimpl3.inicia();
            clienteimpl4.inicia();
            clienteimpl5.inicia();
        } catch (Exception exc) {
            System.out.println("ERROR : " + exc);
            exc.printStackTrace(System.out);
        }

    }

}
