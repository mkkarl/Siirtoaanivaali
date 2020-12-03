/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siirtoaanivaali.logiikka;

/**
 *
 * @author mari
 */
public class EhdokasAanet implements Comparable<EhdokasAanet> {

    private int ehdokasNumero;
    private double aanet;

    public EhdokasAanet(int ehdokasNumero, double aanet) {
        this.ehdokasNumero = ehdokasNumero;
        this.aanet = aanet;
    }

    public int getEhdokasNumero() {
        return this.ehdokasNumero;
    }

    public double getAanet() {
        return this.aanet;
    }

    @Override
    public int compareTo(EhdokasAanet ehdokasAanet) {
        if (aanet < ehdokasAanet.getAanet()) {
            return -1;
        } else if (aanet == ehdokasAanet.getAanet()) {
            return 0;
        }

        return 1;
    }

}
