/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siirtoaanivaali;

import siirtoaanivaali.ui.Kayttoliittyma;

/**
 *
 * @author mari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Kayttoliittyma kayttoliittyma = new Kayttoliittyma();
        if (args == null || args.length == 0) {
            kayttoliittyma.kaynnista();
        } else {
            kayttoliittyma.kaynnista(args[0]);
        }

    }

}
