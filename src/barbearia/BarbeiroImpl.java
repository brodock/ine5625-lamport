package barbearia;

/**
 * Implementaçãod o Barbeiro - CORBA
 * @author Leandro Mendes
 * @author Gabriel Mazetto
 */
public class BarbeiroImpl extends BarbeiroPOA {

    public boolean cortarBarba() {
        boolean veri = false;
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        System.out.println("Barbeiro cortou uma barba!");
        return veri;
    }

    public boolean cortarCabelo() {
        boolean veri = false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        System.out.println("Barbeiro cortou um cabelo!");
        return veri;
    }

    public boolean cortarBigode() {
        boolean veri = false;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        System.out.println("Barbeiro cortou um bigode!");
        return veri;
    }
}
