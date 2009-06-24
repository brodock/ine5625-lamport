package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

/**
 * Responsavel por instanciar o Barbeiro
 * @author Leandro Mendes
 * @author Gabriel Mazetto
 */
public class IniciaBarbeiro {

    public IniciaBarbeiro(String args[]) {
        Mensagem("Iniciando barbeiro CORBA");

        InicializaCorba(args);
    }

    public static void main(String args[]) {
        IniciaBarbeiro inicia = new IniciaBarbeiro(args);
    }

    private static void Mensagem(String texto) {
        System.out.println("[Barbeiro] " + texto);
    }

    private void InicializaCorba(String[] args) {

        // Inicializa o ORB
        ORB orb = ORB.init(args, null);
        try {

            // Ativa o POA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();

            // Obtém a referência (endereço) do servidor de nomes (NameService)
            // Essa tarefa é realizada pelo ORB (orb.resolve_initial_references)
            NamingContextExt nc = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));

            // Instancia um objeto da classe BarbeiroImpl
            BarbeiroImpl barbeiroimpl = new BarbeiroImpl();

            // Transforma o objeto java BarbeiroImpl (barbeiro_corba) num objeto CORBA genérico
            org.omg.CORBA.Object barbeiro_corba = poa.servant_to_reference(barbeiroimpl);

            // Envia o objeto para o servidor de nomes e faz um rebind
            nc.rebind(nc.to_name("Barbearia.barbeiro"), barbeiro_corba);

            Mensagem("aguardando requisicoes ...");

            // Fun! \o/
            orb.run();
        } catch (Exception exception) {
            System.err.println("ERRO: " + exception);
            exception.printStackTrace(System.out);
        }
        Mensagem("Encerrando o Barbeiro.");
    }
}
