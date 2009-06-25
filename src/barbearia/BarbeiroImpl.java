/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package barbearia;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author llm
 */

public class BarbeiroImpl extends BarbeiroPOA {

    public boolean cortarBarba(short id){
        boolean veri = false;
        System.out.println("Barbeiro cortou uma barba do cliente" + id + " !");
        try {
            Thread.sleep(Constantes.CORTAR_BARBA);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarbeiroImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return veri;
    }
    public boolean cortarCabelo(short id){
        boolean veri = false;
        System.out.println("Barbeiro cortou um cabelo do cliente" + id + " !");
                try {
            Thread.sleep(Constantes.CORTAR_CABELO);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarbeiroImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return veri;
    }
    public boolean cortarBigode(short id){
        boolean veri = false;
        System.out.println("Barbeiro cortou um bigode do cliente" + id + " !");
                try {
            Thread.sleep(Constantes.CORTAR_BIGODE);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarbeiroImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return veri;
    }

}
