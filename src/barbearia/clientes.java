package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

/**
 *
 * @author llm
 */
public class Clientes {

    private Integer cliente_num;

    public static void main(String[] args) {
        Clientes clientes = new Clientes(args);
        System.exit(0);
    }

    public Clientes(String[] args) {

        boolean error = true;

        // Extrai numero do cliente
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            if (param.startsWith("-cliente")) {
                this.cliente_num = Integer.parseInt(param.substring(8));
                error = false;
                args[i] = null;
            }

        }

        if (!error) {
            this.InicializaCorba(args);
        } else {
            mensagem("Utilize -clienteN para definir um cliente novo");
        }
    }

    private void InicializaCorba(String[] args) {
        try {

            // Inicializa o ORB
            ORB orb = ORB.init(args, null);

            // Ativa o POA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();

            // Obtém a referência do servidor de nomes (NameService)
            // Essa tarefa é realizada pelo ORB (resolve_initial_references)
            NamingContextExt nc = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));

            // Obtém o objeto CORBA (genérico) do servidor HelloWorld
            // É o servidor de nomes que fornece essa referência (resolve)
            org.omg.CORBA.Object o = nc.resolve(nc.to_name("ServicoEventos.corba"));


            ClienteImpl cliente_impl = new ClienteImpl(this.cliente_num, orb, poa, nc);

            org.omg.CORBA.Object cliente_corba = poa.servant_to_reference(cliente_impl);

            nc.rebind(nc.to_name("cliente1"), cliente_corba);
        } catch (Exception ex) {
            System.out.println("ERROR : " + ex);
            ex.printStackTrace(System.out);
        }
    }

    private void mensagem(String string) {
        System.out.println(string);
    }
}


