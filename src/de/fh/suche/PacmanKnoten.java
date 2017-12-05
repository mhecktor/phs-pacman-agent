package de.fh.suche;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanTileType;
import de.fh.util.Vector2;
/**
 * Created by daniel on 23.09.16.
 * Dies ist die Spezialisierung des generischen "Knotens"
 * für unsere Pacman-Welt.
 */
public class PacmanKnoten extends Knoten{

    //Die Felderbelegung der (virtuellen) aktuellen Welt
    //allerdings ohne den Pacman (Pacmanposition wird in pos gespeichert)
    private PacmanTileType[][] view;
    //Wie die view, nur als String für Debugzwecke gespeichert
    private String sView = "";
    //Die Position des Pacmans, während der Suche
    private Vector2 pos = new Vector2(0,0);

    public LinkedBlockingQueue<PacmanAction> getPacmanActionList() {
        return pacmanActionList;
    }

    //Ein Knoten erzeugt sich aus dem Vorgänger und Bewegungsrichtung
    private LinkedBlockingQueue<PacmanAction> pacmanActionList;

    /**
     * Konstruktor für den Wurzel- und Zielknoten
     * @param view Anischt auf die Pacman-Welt
     * @param pos Positions des Pacmans
     */
    public PacmanKnoten(PacmanTileType[][] view, Vector2 pos){
    	//Alle expandierten Knoten haben Vorgänger und (Pacman-)Bewertung
        super(new PacmanBewertung());

        //Konstruktor für den Zielzustand und das Wurzelelement
        //Der Wurzelknoten und Zielzustand hat keinen Vorgänger
        this.vorgaenger = null;
        //Der Wurzelknoten hat eine leere PacmanActionliste
        this.pacmanActionList = new LinkedBlockingQueue<PacmanAction>();
        //Die Wurzel kennt die Ausgangsposition des Pacmans
        this.pos = pos;

        //Erzeuge neues view auf Basis des gegebenen Views
        this.view = new PacmanTileType[view.length][view[0].length];
        this.view = copyArray(view);

        //Der Zielzustand kennt keinen Position
        if(this.pos != null){
            //..die Wurzel schon
            //Lösche den Agenten aus dem View
            this.view[this.pos.getX()][this.pos.getY()] = PacmanTileType.EMPTY;
        }

        //Erzeuge ein String für Debugzwecke
        for(int i = 0; i <  this.view.length; i++){
            this.sView = this.sView.concat(Arrays.toString(this.view[i]));
        }
    }

    /**
     * Konstruktor für alle expandierte Knoten
     *
     * @param vorgaenger
     * @param bewegungsRichtung
     */
    public PacmanKnoten(PacmanKnoten vorgaenger, PacmanAction bewegungsRichtung) {
        //Alle expandierten Knoten haben Vorgänger und (Pacman-)Bewertung
        super(vorgaenger, new PacmanBewertung());

        //Die Richtung, in die der Pacman ziehen soll
        //Der Wurzelknoten hat eine leere PacmanActionliste
        this.pacmanActionList =  new LinkedBlockingQueue<PacmanAction>();
        this.pacmanActionList.addAll(vorgaenger.getPacmanActionList());
        this.pacmanActionList.add(bewegungsRichtung);

        //Erzeuge neues view auf Basis des Vorgängers
        this.view = new PacmanTileType[vorgaenger.getView().length][vorgaenger.getView()[0].length];
        this.view = copyArray(vorgaenger.getView());

        //Berechne die neue Position auf Basis der Bewegungsrichtung
        this.pos = berechneNeuePosition(vorgaenger.getPos(), bewegungsRichtung);

        //Lasse dort den virtuellen Pacman das Dot fressen
        this.view[this.pos.getX()][this.pos.getY()] = PacmanTileType.EMPTY;

        //Erzeuge ein String für Debugzwecke
        for(int i = 0; i <  this.view.length; i++){
            this.sView = this.sView.concat(Arrays.toString(this.view[i]));
        }
    }

    public PacmanTileType[][] getView() {
        return view;
    }

    public void setView(PacmanTileType[][] view) {
        this.view = view;
    }

    public Vector2 getPos() {
        return pos;
    }

    public PacmanBewertung getBewertung(){
        return (PacmanBewertung) this.bewertung;
    }

    public PacmanKnoten getVorgaenger(){
        return (PacmanKnoten) this.vorgaenger;
    }




    //Kopieren von 2-dimensionalen Arrays
    private PacmanTileType[][] copyArray(PacmanTileType[][] view) {
        // Kopieren manuell
    	PacmanTileType[][] newView = new PacmanTileType[view.length][view[0].length];

        for (int spalte = 0; spalte < view.length; spalte++) {
            for (int zeile = 0; zeile < view[0].length; zeile++) {
                newView[spalte][zeile] = view[spalte][zeile];
            }
        }

        return newView;
    }

    /**
     * Wann sind zwei Konten gleich
     *
     * @param knoten
     * @return
     */
    @Override
    public boolean isGleich(Knoten knoten) {
        PacmanKnoten vergleichsKandidat = (PacmanKnoten) knoten;

        if(this.getPos().getX() == vergleichsKandidat.getPos().getX())
            if(this.getPos().getY() == vergleichsKandidat.getPos().getY())
                //Die Position der Agenten ist gleich
                if (this.sView.equals(vergleichsKandidat.sView)){
                    //Die Welt mit ihren Dots ist auch gleich, ergo Knoten sind gleich
                    return true;
                }

         return false;
    }

    /**
     * Ist der Konten der Zielzustand
     *
     * @param knoten
     * @return
     */
    @Override
    public boolean isZiel(Knoten knoten) {
        PacmanKnoten vergleichsKandidat = (PacmanKnoten) knoten;

        if (this.sView.equals(vergleichsKandidat.sView)){
            //Die Welt mit ihren Dots ist gleich, ergo Zielzustand erreicht
            //Die Position des Agenten spielt bei dieser Abfrage keine Rolle!
            return true;
        }
        return false;
    }


    //Gebe den Knoten auf der Konsole aus
    @Override
    public String toString() {
        String str = "";

        str += "\n";
        str += "[" + this.pos.getX()+"," + this.pos.getY() + "]\n";
        for (int x = 0; x < view[0].length; x++) {
            for (int y = 0; y < view.length; y++) {
                str += view[y][x] + " ";
            }
            str += "\n";
        }

        return str;
    }


    //Berechne die neue Positions des Pacmans auf Basis der Bewegungsrichtung
    Vector2 berechneNeuePosition(Vector2 vorgaengerPos, PacmanAction bewegungsrichtung){
        Vector2 nachfolgerPos = new Vector2(-1, -1);

       if(bewegungsrichtung == PacmanAction.GO_NORTH)
                nachfolgerPos.set(vorgaengerPos.getX(), vorgaengerPos.getY() - 1);
       else if(bewegungsrichtung == PacmanAction.GO_EAST)
                nachfolgerPos.set(vorgaengerPos.getX() + 1, vorgaengerPos.getY());
       else if(bewegungsrichtung == PacmanAction.GO_SOUTH)
                nachfolgerPos.set(vorgaengerPos.getX(), vorgaengerPos.getY() + 1);
       else if(bewegungsrichtung == PacmanAction.GO_WEST)
                nachfolgerPos.set(vorgaengerPos.getX() - 1, vorgaengerPos.getY());
       else
           throw new IllegalArgumentException("Unzulässige PacmanAction");

        return nachfolgerPos;
    }
}
