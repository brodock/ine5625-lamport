package barbearia;

import java.util.logging.Level;
import java.util.logging.Logger;
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
    private short contador_geral = 0;
    private short contador_atual = contador_geral;
    private Vector<Short> oks = new Vector<Short>();
    private Vector<Mensagem> fila = new Vector<Mensagem>();
    private Vector veri_ok = new Vector();
    private boolean usando = false;
    private boolean tentando = false;
    private Integer sequencia_barbeiro = 1;
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
            // Vamos processar a mensagem...
            if (msg.cont < contador_atual) { // Se o contador for menor que o atual, ele tem prioridade
                enviaOK(msg);
            } else if (msg.cont == contador_atual) { // Se o contador for igual o atual, quem tiver o menor id tem prioridade
                if (msg.id < this.identificador) {
                    enviaOK(msg);
                } else {
                    fila.add(msg);
                }
            } else { // Se o contador for maior que o atual, vamos por na fila
                fila.add(msg);
            }
        }

        // Se o contador da mensagem recebida for maior, incrementa nosso contador geral
        if (msg.cont > contador_geral) {
            contador_geral = msg.cont;
        }
    }

    /**
     * Envia uma mensagem de OK para o remetente da mensagem passada por parâmetro
     * @param msg Mensagem que vai receber um OK
     */
    public void enviaOK(Mensagem msg) {
        try {
            Cliente clientex = ClienteHelper.narrow(namingcontext.resolve_str("Barbearia.cliente" + msg.id));
            clientex.msgOK(identificador.shortValue());
            mensagem("Enviei mensagem de OK para Cliente " + msg.id);
        } catch (Exception exc) {
            System.out.println("ERROR : " + exc);
            exc.printStackTrace(System.out);
        }
    }

    public void inicia() {
        Concorrer concorrer = new Concorrer();
        concorrer.start();
    }

    /**
     *
     * @param sequencia
     */
    public void acesso(int sequencia) {
        try {
            Barbeiro barbeiro = BarbeiroHelper.narrow(namingcontext.resolve_str("Barbearia.barbeiro"));

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

    public void tentaAcessarBarbeiro() {

        if (tentando) {
            // Vamos verificar se já possui todos os oks
            if (oks.size() >= (Constantes.NUMERO_CLIENTES - 1)) {
                boolean verifica_ok = true;
                for (Integer i = 1; i <= Constantes.NUMERO_CLIENTES; i++) {
                    if (i != identificador) {
                        if (!oks.contains(i.shortValue())) {
                            verifica_ok = false; // Nunca deve chegar aqui...
                            mensagem("ERRO: Temos mais OKS do que deveria e o do cliente " + i + " não foi encontrado! (" + oks.toString() + ")");
                        }
                    }
                }

                if (verifica_ok) {
                    this.tentando = false;
                    this.usando = true;
                    acesso(this.sequencia_barbeiro);
                    this.usando = false;
                    this.sequencia_barbeiro++;
                    this.oks.clear();
                    this.contador_atual = this.contador_geral;
                    processaFila();
                }
            }
        } else {
            // Vamos enviar a mensagem para concorrer ao recurso
            this.tentando = true;
            enviaConcorrer();
        }
    }

    /**
     * Dispara mensagens para concorrer a um recurso
     */
    private void enviaConcorrer() {

        this.contador_geral++;
        this.contador_atual = this.contador_geral;
        Mensagem msg = new Mensagem(identificador.shortValue(), "barbeiro", this.contador_atual);

        // Dispara a mensagem para todos clientes (exceto o próprio)
        for (int i = 1; i <= Constantes.NUMERO_CLIENTES; i++) {
            if (i != identificador) {
                boolean error = true;
                while (error) {
                    try {
                        Cliente clientex = ClienteHelper.narrow(namingcontext.resolve_str("Barbearia.cliente" + i));
                        clientex.Concorrer(msg);
                        error = false;
                    } catch (Exception ex) {
                        mensagem("nao encontrei o cliente " + i + "... AGUARDANDO 5s");

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex1) {
                            Logger.getLogger(ClienteImpl.class.getName()).log(Level.SEVERE, null, ex1);
                        }

                    }
                }
            }
        }
    }

    private void mensagem(String texto) {
        System.out.println("[Cliente" + this.identificador + "] " + texto);
    }

    private void processaFila() {
        mensagem("Processando fila (" + fila.toString() + ")");

        int size = fila.size();
        for (int i = 0; i < size; i++) {
            enviaOK(fila.elementAt(0));
            fila.remove(0); // pega o primeiro elemento da fila e tras o segundo pro lugar do primeiro
        }
    }

    private class Concorrer extends Thread {

        @Override
        public void run() {
            int i = 0;
            while (i < 100 && sequencia_barbeiro <= 3) {
                tentaAcessarBarbeiro();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClienteImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            mensagem("Acabaram minhas tentativas...");
        }
    }
}
