package fr.cda.projet;
import java.util.*;
import fr.cda.util.*;

/**
 * Classe de definition du site de vente
 */
public class Site {
    private ArrayList<Produit> stock;
    private ArrayList<Commande> commandes;
    private ArrayList<Commande> commandesNonLivres;
    private ArrayList<Commande> commandesLivres;

    /**
     * Constructeur
     */
    public Site() {
        stock = new ArrayList<Produit>();
        commandes = new ArrayList<Commande>();
        commandesNonLivres = new ArrayList<Commande>();
        commandesLivres = new ArrayList<Commande>();

        // lecture du fichier data/Produits.txt
        //  pour chaque ligne on cree un Produit que l'on ajoute a stock
        initialiserStock("data/Produits.txt");

        //  lecture du fichier data/Commandes.txt
        //  pour chaque ligne on cree une commande ou on ajoute une reference
        //  d'un produit a une commande existante.
        initialiserCommande("data/Commandes.txt");

        // initialiser le method
        managingStock();
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere
     * tous les produits du stock
     *
     * @return the string
     */
    public String listerTousProduits() {
        String res="";
        for (int i = 0; i < stock.size(); i++) {
            Produit prod = stock.get(i);
            res = res + prod.toString() + "\n";
        }
        return res;
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere\
     * toutes les commandes
     *
     * @return the string
     */
    public String listerToutesCommandes() {
        String res="";
        for (Commande comd : commandes) {
            res = res + comd.toString(false) + "\n";
        }
        return res;
    }

    /**
     * Methode qui retourne sous la forme d'une chaine de caractere
     * une commande
     *
     * @param numero the numero
     * @return the string
     */
    public String listerCommande(int numero){
        String res="";
        boolean trouve = false;
        for (Commande commande : commandes) {
            int num = commande.getNumero();
            if (num == numero) {
                res = res + commande.toString(false);
                trouve = true;
            }
        }
        if(!trouve) {
            res = "   la commande n'existe pas   ";
        }
        return res;
    }

    /**
     * Afficher les commandes non livrer
     *
     * @return the string
     */

    public String listecommandesLivre(){
        String res= """
                Les commmandes liveres  : 
                =============================
                
                """;
        for (Commande comd : commandesLivres) {
            res = res + comd.toString(false) + "\n";
        }
        return res;
    }

    public String listecommandesNonLivre(){
        String res= """
                Les commmandes non liveres  : 
                =============================
                
                """;
        for (Commande comd : commandesNonLivres) {
            res = res + comd.toString(true) + "\n";
        }
        return res;
    }

    /**
     *
     * @param numero
     * @return
     */
    public Commande getCommandeByNumero(int numero)
    {
        for (int i = 0; i < commandes.size(); i++)
            if (commandes.get(i).getNumero() == numero)
                return (commandes.get(i));
        return (null);
    }

    /**
     * Calculer ventes for les commandes livrer
     *
     * @return the string
     */
    public String calculerVentes(){
        String res ="";
        String res1 ="";
        String cmdRref = "";
        int cmdQuantity = 0;
        double total = 0;
        for (Produit produit : stock) {
            String stockref = produit.getReference();
            for (int i = 0; i < commandesLivres.size(); i++) {
                res = "Commande  :    " + commandesLivres.get(i).getNumero()+ "  " + '\n';
                String[] refs = commandesLivres.get(i).getReferences().toArray(new String[0]);
                for (String ref : refs) {
                    String[] refsChamps = ref.split("=");
                    cmdRref = refsChamps[0];
                    cmdQuantity = Integer.parseInt(refsChamps[1]);
                    if(stockref.equals(cmdRref)){
                        res1 = res1 + '\n'+ "            "+ produit.getNom() + "    " + cmdQuantity + "    " + (cmdQuantity * produit.getPrix()) + '\n';
                        total = total + (cmdQuantity * produit.getPrix());
                    }
                }
                res = res + res1 +  '\n' + "=====================" + '\n' + " SOMME   :   " + total  + "   euros";
            }
        }
        return res;
    }

    /**
     * Chargement du fichier de stock
     */
    private void initialiserStock(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        for(String ligne :lignes) {
            //System.out.println(ligne);
            String[] champs = ligne.split(";",4);
            String reference = champs[0];
            String nom = champs[1];
            double prix = Double.parseDouble(champs[2]);
            int quantite =  Integer.parseInt(champs[3]);
            Produit p = new Produit(reference, nom, prix, quantite);
            stock.add(p);
        }
    }

    /**
     * Chargement du fichier de commande
     * Et ajouter la nouvelle commande si la commande existe déjà alors seulement en ajoutant refrence
     */

    private void initialiserCommande(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        for(String ligne :lignes) {
            //System.out.println(ligne);
            String[] champs = ligne.split(";",4);
            int numeroCmd = Integer.parseInt(champs[0]);
            String date = champs[1];
            String nom = champs[2];
            String reference = champs[3];

            Commande c;
            int index = rechercheParIndex(numeroCmd);
            if(index != -1){
                commandes.get(index).addRef(reference);
            } else {
                c = new Commande(numeroCmd, date, nom);
                c.addRef(reference);
                commandes.add(c);
            }

//            Commande c = rechercheCommande(numeroCmd);
//            if (c == null){
//                c = new Commande(numeroCmd, date, nom);
//                c.addRef(reference);
//
//                commandes.add(c);
//            } else {
//                c.addRef(reference);
//            }
        }
    }

    /**
     * vérifier si la commande est déjà présente dans le Arraylist des commandes return commande
     */
//    private Commande rechercheCommande(int num){
//        for (Commande c : commandes) {
//            if(c.getNumero() == num){
//                return c;
//            }
//        }
//        return null;
//    }

    /**
     * vérifier si la commande est déjà présente dans le Arraylist des commandes return index de commande
     */
    private int rechercheParIndex(int num){
        for (int i =0; i < commandes.size();i++)
            if(commandes.get(i).getNumero() == num)
                return (i);
        return (-1);
    }

    /**
     *  Rechercher les commandes non livrer
     */
    private void managingStock(){
        String cmdRref = "";
        int cmdQuantity = 0;
        String res = "";
        for (Commande commande : commandes) {
            String[] refs = commande.getReferences().toArray(new String[0]);
            for (String ref : refs) {
                String[] refsChamps = ref.split("=");
                cmdRref = refsChamps[0];
                cmdQuantity = Integer.parseInt(refsChamps[1]);
                if(notHaveEnoughQuantity(cmdRref, cmdQuantity)){
                    res = "   il manque    " + (cmdQuantity - currentQuantityInStock(cmdRref, cmdQuantity)) +"    "+ cmdRref;
                    commande.addReasons(res);  // ajouter les reasons pour non livrer
                }
                if(haveEnoughQuantity(cmdRref, cmdQuantity)){
                }
            }

            // Ajouter les commandes non livrer dans arraylist
            //
            if(notHaveEnoughQuantity(cmdRref, cmdQuantity)){
                commandesNonLivres.add(commande);
            }
            //Ajouter les commandes livrer dans arraylist
            //
            if(haveEnoughQuantity(cmdRref, cmdQuantity)){
                commandesLivres.add(commande);

            }
        }
    }

    /**
     * vérifier si la quantité est inférieure en stock puis retourner la quantité
     * @param ref
     * @param quan
     * @return stock quantity
     */
    private int currentQuantityInStock(String ref, int quan){
        int stkquan = 0;
        for(Produit p : stock){
            String key = p.getReference();
            int val = p.getQuantite();
            if (key.equals(ref) && val < quan) {
                stkquan = val;
            }
        }
        return stkquan;
    }

    /**
     * vérifier si la quantité est plus en stock
     * @param ref
     * @param quan
     * @return boolean
     */
    private boolean haveEnoughQuantity(String ref, int quan){
        boolean stockRef = false;
        for(Produit p : stock){
            String key = p.getReference();
            int val = p.getQuantite();
            if (key.equals(ref) && val > quan) {
                stockRef = true;
                break;
            }
        }
        return stockRef;
    }

    /**
     * vérifier si la quantité est inférieure en stock
     * @param ref
     * @param quan
     * @return boolean
     */
    private boolean notHaveEnoughQuantity(String ref, int quan){
        boolean lessQuan = false;
        for(Produit p : stock){
            String key = p.getReference();
            int val = p.getQuantite();
            if (key.equals(ref) && val < quan) {
                lessQuan = true;
                break;
            }
        }
        return lessQuan;
    }
}