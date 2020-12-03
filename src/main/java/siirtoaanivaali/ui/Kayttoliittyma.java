/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siirtoaanivaali.ui;

import java.util.Scanner;
import siirtoaanivaali.logiikka.Logiikka;

/**
 *
 * @author mari
 */
public class Kayttoliittyma {

    private Scanner lukija;
    private Logiikka logiikka;

    public Kayttoliittyma() {
        this.lukija = new Scanner(System.in);
    }

    public void kaynnista() {
        System.out.println("Anna tiedostonimi:");
        String tiedosto = lukija.nextLine();

        this.logiikka = new Logiikka();

        logiikka.aantenLasku(tiedosto);
        logiikka.tulostaLuetutTiedot();

    }

}
