/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package barbearia;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import java.util.Vector;

/**
 *
 * @author llm
 */

public class ClienteImpl extends ClientePOA {
    
    private short numero;
    private String[] args = new String[4];
    private ORB orb = null;
    private NamingContextExt namingcontext = null;
    private org.omg.CORBA.Object objx = null;
    private String sx = "";
    private Cliente clientex = null;
    private short cont = 0;
    private Vector oks = new Vector();
    private Vector log = new Vector();
    private Vector veri_ok = new Vector();
    private boolean usando = false;

    public ClienteImpl(short numero){
        this.numero = numero;
        args[0] = "-ORBInitialPort";
        args[1] = "2500";
        args[2] = "-ORBInitialHost";
        args[3] = "150.162.65.126";
        orb = ORB.init(args, ((java.util.Properties) (null)));
    }
   
    public void msgOK(short id){oks.add(id);}

    public void Concorrer(Mensagem msg){
        boolean veri = false;
        if(usando == true){
            log.add(msg.id);
        }
        else{
            if(msg.cont > cont){veri = true;}
            if(msg.cont == cont){
                if(msg.id < numero){veri = true;}
                else{log.add(msg.id);}              
            }
            if(msg.cont < cont){log.add(msg.id);}
        }
        if(msg.cont > cont){cont = msg.cont;}
        if (veri == true) {
            try {
                orb = ORB.init(args, ((java.util.Properties) (null)));
                objx = orb.resolve_initial_references("NameService");
                namingcontext = NamingContextExtHelper.narrow(objx);
                sx = "cliente" + msg.id;
                clientex = ClienteHelper.narrow(namingcontext.resolve_str(sx));
                clientex.msgOK(numero);
            } catch (Exception exc) {
                System.out.println("ERROR : " + exc);
                exc.printStackTrace(System.out);
            }
        }
    }

    public void inicia() {
        Gera gera = new Gera();
        gera.start();
    }

    public void acesso(int num) {
        try {
            org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
            NamingContextExt namingcontextext = NamingContextExtHelper.narrow(obj);
            String s = "barbeiro";
            Barbeiro barbeiro = BarbeiroHelper.narrow(namingcontextext.resolve_str(s));
            if (num == 1) {
                barbeiro.cortarCabelo();
                System.out.println("Barbeiro cortou o cabelo do Cliente" + numero);
            }
            if (num == 2) {
                barbeiro.cortarBarba();
                System.out.println("Barbeiro cortou a barba do Cliente" + numero);
            }
            if (num == 3) {
                barbeiro.cortarBigode();
                System.out.println("Barbeiro cortou o bigode do Cliente" + numero);
            }
        } catch (Exception exc) {
            System.out.println("ERROR : " + exc);
            exc.printStackTrace(System.out);
        }
    }

    public class Gera extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                for (int j = 1; j < 4; j++) {
                    cont++;
                    Mensagem msg = new Mensagem(numero, "barbeiro", cont);
                    for (int l = 1; l < 6; l++) {
                        if (l != numero) {
                            try {
                                objx = orb.resolve_initial_references("NameService");
                                namingcontext = NamingContextExtHelper.narrow(objx);
                                sx = "cliente" + l;
                                clientex = ClienteHelper.narrow(namingcontext.resolve_str(sx));
                                clientex.Concorrer(msg);
                            } catch (Exception exc) {
                                System.out.println("ERROR : " + exc);
                                exc.printStackTrace(System.out);
                            }
                        } else {continue;}
                    }
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
                    for (int m = 0; m < log.size(); m++) {
                        try {
                            orb = ORB.init(args, ((java.util.Properties) (null)));
                            objx = orb.resolve_initial_references("NameService");
                            namingcontext = NamingContextExtHelper.narrow(objx);
                            sx = "cliente" + log.elementAt(m).toString();
                            clientex = ClienteHelper.narrow(namingcontext.resolve_str(sx));
                            clientex.msgOK(numero);
                            log.remove(m);
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
