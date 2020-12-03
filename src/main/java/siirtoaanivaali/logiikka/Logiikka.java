/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siirtoaanivaali.logiikka;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mari
 */
public class Logiikka {

//    private Scanner tiedostonlukija;
    private String tiedosto;
    private int ehdokasLkm;
    private int valittavatLkm;
    private List<String> lipukkeetRaaka;
    private List<Lipuke> lipukkeet;
    private String[] ehdokkaat;
    private String vaalinNimi;
    private List<Integer> valitut;

    public Logiikka() {
//        this.tiedostonlukija = new Scanner(tiedosto);
        this.lipukkeetRaaka = new ArrayList<>();
        this.lipukkeet = new ArrayList<>();
    }

    public void aantenLasku(String tiedosto) {
        this.tiedosto = tiedosto;

        lueTiedosto();
        luoLipukkeet();

        double aanikynnys = 1.0 * lipukkeetRaaka.size() / ehdokasLkm;

        double[] aanet = new double[ehdokasLkm + 1];

        for (Lipuke lipuke : lipukkeet) {
            int ehdokas = lipuke.getEhdokas();
            aanet[ehdokas] += lipuke.getAanimaara();
        }

        for (int i = 1; i < aanet.length; i++) {
            if (aanet[i] >= aanikynnys && !valitut.contains(i)) {
                valitut.add(i);
            }
        }
    }

    void lueTiedosto() {
        try (Scanner tiedostonlukija = new Scanner(new File(tiedosto))) {

            String[] palat = tiedostonlukija.nextLine().split(" ");

            ehdokasLkm = Integer.valueOf(palat[0]);
            valittavatLkm = Integer.valueOf(palat[1]);

            while (tiedostonlukija.hasNextLine()) {
                String rivi = tiedostonlukija.nextLine();
                if (rivi.equals("0")) {
                    break;
                } else {
                    lipukkeetRaaka.add(rivi);
                }
            }

            ehdokkaat = new String[ehdokasLkm + 1]; // yhden pidempi, jotta ehdokkaan numero vastaa indeksiä

            for (int i = 1; i <= ehdokasLkm; i++) {
                ehdokkaat[i] = tiedostonlukija.nextLine();
            }

            vaalinNimi = tiedostonlukija.nextLine();
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }
    }

    void luoLipukkeet() {
        for (String lipuke : lipukkeetRaaka) {
            lipukkeet.add(new Lipuke(ehdokasLkm, lipuke));
        }
    }

    void laskeAanet() {

        double aanikynnys = 1.0 * lipukkeetRaaka.size() / ehdokasLkm;

        while (valitut.size() <= valittavatLkm) {

            //Lasketaan ehdokkaiden äänet
            double[] aanet = new double[ehdokasLkm + 1];

            for (Lipuke lipuke : lipukkeet) {
                int ehdokas = lipuke.getEhdokas();
                aanet[ehdokas] += lipuke.getAanimaara();

            }

            // Siirretään äänikynnyksen ylittäneet ehdokkaat tarkastelulistalle
            List<EhdokasAanet> vertailu = new ArrayList<>();
            int[] lipukelaskuri = new int[ehdokasLkm];

            for (int i = 1; i < aanet.length; i++) {
                vertailu.add(new EhdokasAanet(ehdokasLkm, aanet[i]));
            }

            if (!vertailu.isEmpty()) {

                // Jos tarkasteltavia ehdokkaita on korkeintaan vapaiden paikkoijen verran, valitaan kaikki
                if (vertailu.size() <= valittavatLkm - valitut.size()) {
                    for (EhdokasAanet vertailtava : vertailu) {
                        valitut.add(vertailtava.getEhdokasNumero());
                        lipukelaskuri[vertailtava.getEhdokasNumero()]++;
                    }
                } else { // Muuten valitaan eniten ääniä saaneet
                    Collections.sort(vertailu);

                    for (int i = 0; i < valittavatLkm - valitut.size(); i++) {
                        valitut.add(vertailu.get(i).getEhdokasNumero()); // entä jos viimeisen jälkeen tasapisteissä toinen?
                        lipukelaskuri[vertailu.get(i).getEhdokasNumero()]++;
                    }
                }

                // Siirretään ylijäämä-äänet
                for (Lipuke lipuke : lipukkeet) {
                    if (valitut.contains(lipuke.getEhdokas())) {
                        lipuke.siirraAanetSeuraavalle((aanet[lipuke.getEhdokas()] - aanikynnys) / lipukelaskuri[lipuke.getEhdokas()], valitut);
                    }
                }
            } else {
                // Jos ei ehdokkaita tarkasteltavana, siirretään vähiten ääniä saaneen äänet seuraavalle ja lisätään ehdokas eliminoitujen listalle
                
            }
        }

    }

    public void tulostaLuetutTiedot() {
        System.out.println("Vaalin nimi: " + vaalinNimi);
        System.out.println("Ehdokkaiden määrä: " + ehdokasLkm);
        System.out.println("Valittavien määrä: " + valittavatLkm);
        System.out.println("");

        System.out.println("Ehdokkaat:");
        for (String ehdokas : ehdokkaat) {
            System.out.println(ehdokas);
        }
        System.out.println("");

        System.out.println("Lipukkeiden lkm: " + lipukkeetRaaka.size());
        System.out.println("Äänestyslipukkeet:");
        for (String lipuke : lipukkeetRaaka) {
            System.out.println(lipuke);
        }
        System.out.println("Toinen");

        for (Lipuke lipuke : lipukkeet) {
            System.out.println(lipuke.toString());
        }
    }

}
