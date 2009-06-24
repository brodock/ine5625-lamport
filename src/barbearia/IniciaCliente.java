package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

/**
 * Inicia um cliente especificado por -clienteN
 * 
 * @author Leandro Mendes
 * @author Gabriel Mazetto
 */
public class IniciaCliente {

    private Integer cliente_num;

    public static void main(String[] args) {
        IniciaCliente clientes = new IniciaCliente(args);
        System.exit(0);
    }

    public IniciaCliente(String[] args) {

        boolean error = true;

        // Extrai numero do cliente
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            if (param.startsWith("-cliente")) {
                this.cliente_num = Integer.parseInt(param.substring(8));
                error = false;
                args[i] = null;
                mensagem("instanciado!");
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

            ClienteImpl cliente_impl = new ClienteImpl(this.cliente_num, orb, poa, nc);

            org.omg.CORBA.Object cliente_corba = poa.servant_to_reference(cliente_impl);

            nc.rebind(nc.to_name("Barbearia.cliente"+this.cliente_num.toString()), cliente_corba);

            mensagem("carregado no ORB");
            mensagem("Iniciando threads do cliente");
            cliente_impl.inicia();

            orb.run();
            mensagem("finalizando...");
        } catch (Exception ex) {
            mensagem(true, ex.toString());
            ex.printStackTrace(System.out);
        }
    }

    private void mensagem(String texto) {
        if (this.cliente_num == null) {
            this.cliente_num = 0;
        }
        System.out.println("[Cliente"+this.cliente_num.toString()+"] "+texto);

    }

    private void mensagem(boolean erro, String texto) {
        this.mensagem("ERRO: "+texto);
    }
}


