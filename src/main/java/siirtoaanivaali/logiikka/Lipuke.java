/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siirtoaanivaali.logiikka;

import java.util.List;

/**
 *
 * @author mari
 */
public class Lipuke {

    private int[] ehdokasJarjestys;
    private int ehdokasIndeksi;
    private double aanimaara;

    public Lipuke(int ehdokasLkm, String rivi) {
        this.ehdokasJarjestys = new int[ehdokasLkm];

        String[] palat = rivi.split(" ");

        int x = Math.min(palat.length, ehdokasLkm + 1);

        for (int i = 1; i < x; i++) {
            ehdokasJarjestys[i - 1] = Integer.valueOf(palat[i]);
        }

        ehdokasIndeksi = 0;
        aanimaara = 1.0;
    }

    public int getEhdokas() {
        return ehdokasJarjestys[ehdokasIndeksi];
    }

    public double getAanimaara() {
        return aanimaara;
    }

    public void siirraAanetSeuraavalle(double siirtokerroin, List<Integer> valitut, List<Integer> eliminoidut) {
        aanimaara *= siirtokerroin;

        while (true) {
            ehdokasIndeksi++;

            if (ehdokasJarjestys[ehdokasIndeksi] == 0
                    || (!valitut.contains(ehdokasJarjestys[ehdokasIndeksi])
                    && !eliminoidut.contains(ehdokasJarjestys[ehdokasIndeksi]))) {
                break;
            }
        }
    }

    @Override
    public String toString() {
        String teksti = ehdokasJarjestys[0] + "";
        for (int i = 1; i < ehdokasJarjestys.length; i++) {
            teksti += " " + ehdokasJarjestys[i];
        }

        return teksti;
//        return ehdokasJarjestys.toString();
    }
}
