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
    private List<Integer> eliminoidut;

    public Logiikka() {
//        this.tiedostonlukija = new Scanner(tiedosto);
        this.lipukkeetRaaka = new ArrayList<>();
        this.lipukkeet = new ArrayList<>();
        this.valitut = new ArrayList<>();
        this.eliminoidut = new ArrayList<>();
    }

    public void aantenLasku(String tiedosto) {
        this.tiedosto = tiedosto;

        lueTiedosto();
        luoLipukkeet();
        laskeAanet();
        tulostaValitut();

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

        while (valitut.size() < valittavatLkm) {

            //Lasketaan ehdokkaiden äänet
            double[] aanet = new double[ehdokasLkm + 1];

            for (Lipuke lipuke : lipukkeet) {
                int ehdokas = lipuke.getEhdokas();
                aanet[ehdokas] += lipuke.getAanimaara();

            }

            // Siirretään äänikynnyksen ylittäneet ehdokkaat tarkastelulistalle
            List<EhdokasAanet> vertailu = new ArrayList<>();
            List<EhdokasAanet> poistoVertailu = new ArrayList<>();
            int[] lipukelaskuri = new int[ehdokasLkm + 1];

            for (int i = 1; i < aanet.length; i++) {
                if (aanet[i] >= aanikynnys) {
                    vertailu.add(new EhdokasAanet(i, aanet[i]));
                } else if (aanet[i] < aanikynnys && !eliminoidut.contains(i) && !valitut.contains(i)) {
                    poistoVertailu.add(new EhdokasAanet(i, aanet[i]));
                }
            }

            if (!vertailu.isEmpty()) {

                // Jos tarkasteltavia ehdokkaita on korkeintaan vapaiden paikkoijen verran, valitaan kaikki
                if (vertailu.size() <= valittavatLkm - valitut.size()) {
                    for (EhdokasAanet vertailtava : vertailu) {
                        valitut.add(vertailtava.getEhdokasNumero());
                        lipukelaskuri[vertailtava.getEhdokasNumero()]++;
                    }
                } else { // Muuten valitaan eniten ääniä saaneet
                    Collections.shuffle(vertailu); // Tämä hoitaa mahdollisen arvonnan tarpeen
                    Collections.sort(vertailu); // Järjestää pienimmästä suurimpaan äänimäärään
                    Collections.reverse(valitut); // Kääntää järjestyksen suurimmasta pienimpään äänimäärään

                    for (int i = 0; i < valittavatLkm - valitut.size(); i++) {
                        valitut.add(vertailu.get(i).getEhdokasNumero()); // entä jos viimeisen jälkeen tasapisteissä toinen? VAATII ARVONNAN JOS USEAMPIA
                        lipukelaskuri[vertailu.get(i).getEhdokasNumero()]++;
                    }

                }

                // Siirretään ylijäämä-äänet
                for (Lipuke lipuke : lipukkeet) {
                    if (valitut.contains(lipuke.getEhdokas())) {
                        lipuke.siirraAanetSeuraavalle((aanet[lipuke.getEhdokas()] - aanikynnys) / lipukelaskuri[lipuke.getEhdokas()], valitut, eliminoidut);
                    }
                }
            } else {
                // Jos ei ehdokkaita tarkasteltavana, siirretään vähiten ääniä saaneen äänet seuraavalle ja lisätään ehdokas eliminoitujen listalle
                Collections.shuffle(poistoVertailu); // Tämä hoitaa mahdollisen arvonnan tarpeen
                int eliminoitava = Collections.min(poistoVertailu).getEhdokasNumero();

                eliminoidut.add(eliminoitava);

                //Siirretään eliminoidun äänet seuraavalle
                for (Lipuke lipuke : lipukkeet) {
                    if (lipuke.getEhdokas() == eliminoitava) {
                        lipuke.siirraAanetSeuraavalle((aanet[lipuke.getEhdokas()] - aanikynnys) / lipukelaskuri[lipuke.getEhdokas()], valitut, eliminoidut);
                    }
                }
            }
        }

    }

    void tulostaValitut() {
        System.out.println("Valitut:");
        for (int i = 0; i < valitut.size(); i++) {
            System.out.println((i + 1) + " " + ehdokkaat[valitut.get(i)]);
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
