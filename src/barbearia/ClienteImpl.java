package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import java.util.Vector;
import org.omg.PortableServer.POA;

/**
 * Implementação do Cliente CORBA
 * @author Leandro Mendes
 * @author Gabriel Mazetto
 */
public class ClienteImpl extends ClientePOA {

    private Integer identificador;
    private short cont = 0;
    private Vector oks = new Vector();
    private Vector<Mensagem> fila = new Vector<Mensagem>();
    private Vector veri_ok = new Vector();
    private boolean usando = false;
    //
    // CORBA
    //
    private ORB orb = null;
    private POA poa = null;
    private NamingContextExt namingcontext = null;

    public ClienteImpl(Integer numero, ORB orb, POA poa, NamingContextExt nc) {
        this.identificador = numero;
        this.orb = orb;
        this.poa = poa;
        this.namingcontext = nc;

    }

    /**
     * Rececpção de mensagens de OK via CORBA
     * @param id Número do cliente que enviou o OK
     */
    public void msgOK(short id) {
        oks.add(id);
    }

    /**
     * Recepção de mensagem para Concorrer, via CORBA
     * @param msg Mensagem pedindo acesso a área de exclusão mútua
     */
    public void Concorrer(Mensagem msg) {

        //
        // LAMPORT
        //

        if (usando) {
            fila.add(msg);
        } else {
            if (msg.cont < cont) { // Se o contador for menor que o atual, ele tem prioridade
                enviaOK(msg);
            } else if (msg.cont == cont) { // Se o contador for igual o atual, quem tiver o menor id tem prioridade
                if (msg.id < this.identificador) {
                    enviaOK(msg);
                } else {
                    fila.add(msg);
                }
            } else if (msg.cont > cont) {
                this.cont = msg.cont;
                fila.add(msg);
            }
        }

    }

    /**
     * Envia uma mensagem de OK para o remetente da mensagem passada por parâmetro
     * @param msg Mensagem que vai receber um OK
     */
    public void enviaOK(Mensagem msg) {
        try {
            Cliente clientex = ClienteHelper.narrow(namingcontext.resolve_str("cliente" + msg.id));
            clientex.msgOK(identificador.shortValue());
        } catch (Exception exc) {
            System.out.println("ERROR : " + exc);
            exc.printStackTrace(System.out);
        }
    }

    /**
     * Dispara mensagens para concorrer a um recurso
     */
    public void enviaConcorrer() {

        this.cont++;
        Mensagem msg = new Mensagem(identificador.shortValue(), "barbeiro", this.cont);

        // Dispara a mensagem para todos clientes (exceto o próprio)
        for (int i = 1; i < 6; i++) {
            if (i != identificador) {
                try {
                    Cliente clientex = ClienteHelper.narrow(namingcontext.resolve_str("cliente" + i));
                    clientex.Concorrer(msg);
                } catch (Exception exc) {
                    System.out.println("ERROR : " + exc);
                    exc.printStackTrace(System.out);
                }
            }
        }
    }

    public void inicia() {
        Gera gera = new Gera();
        gera.start();
    }

    /**
     *
     * @param sequencia
     */
    public void acesso(int sequencia) {
        try {
            org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
            NamingContextExt namingcontextext = NamingContextExtHelper.narrow(obj);
            String s = "barbeiro";
            Barbeiro barbeiro = BarbeiroHelper.narrow(namingcontextext.resolve_str(s));

            if (sequencia == 1) {
                barbeiro.cortarCabelo();
                mensagem("Barbeiro cortou o cabelo");
            } else if (sequencia == 2) {
                barbeiro.cortarBarba();
                mensagem("Barbeiro cortou a barba");
            } else if (sequencia == 3) {
                barbeiro.cortarBigode();
                mensagem("Barbeiro cortou o bigode");
            }

        } catch (Exception exc) {
            mensagem("ERROR : " + exc);
            exc.printStackTrace(System.out);
        }
    }

    private void mensagem(String texto) {
        System.out.println("[Cliente"+this.identificador+"] "+texto);
    }

    private class Gera extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                for (int j = 1; j < 4; j++) {
                    enviaConcorrer();
                    ////////////////
                    int cont_veri = 0;
                    boolean veri_check = false;
                    boolean veri_o = false;
                    while (veri_check == false) {
                        for (int x = 0; x < oks.size(); x++) {
                            if (cont_veri == 4) {
                                veri_check = true;
                                veri_ok.clear();
                                break;
                            }
                            if (veri_ok.size() == 0) {
                                veri_ok.add(oks.elementAt(x));
                                oks.remove(x);
                                ++cont_veri;
                            } else {
                                veri_o = false;
                                for (int z = 0; z < veri_ok.size(); z++) {
                                    if (!oks.elementAt(x).equals(veri_ok.elementAt(z))) {
                                        veri_o = true;
                                    }
                                }
                                if (veri_o == true) {
                                    veri_ok.add(oks.elementAt(x));
                                    oks.remove(x);
                                    ++cont_veri;
                                }
                            }
                        }
                        if (cont_veri == 4) {
                            veri_check = true;
                            veri_ok.clear();
                        }
                    }
                    usando = true;
                    acesso(j);
                    usando = false;
                    for (int m = 0; m < fila.size(); m++) {
                        try {
                            Cliente clientex = ClienteHelper.narrow(namingcontext.resolve_str("cliente" + fila.elementAt(m).toString()));
                            clientex.msgOK(identificador.shortValue());
                            fila.remove(m);
                        } catch (Exception exc) {
                            System.out.println("ERROR : " + exc);
                            exc.printStackTrace(System.out);
                        }
                    }
                    ////////////////
                }
            }
        }
    }
}
