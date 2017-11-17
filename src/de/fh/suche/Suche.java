package de.fh.suche;


import java.util.ArrayList;

/**
 * Created by daniel on 22.09.16.
 * Generische Klasse für unsere Suchalgorithmen
 */
public abstract class Suche {

    private Knoten zielKnoten;

     //In der Openlist befinden sich die zu expandierenden Knoten
    protected ArrayList<Knoten> openList;

    //In der Closelist befinden sich die bereits expandierenten Knoten um loops verhindern
    protected ArrayList<Knoten> closedList;

    //1 = Tiefensuche
    public final static int TIEFENSUCHE = 1;
    //2 = Breitsuche
    public final static int BREITENSUCHE = 2;
    //3 = Bestensuche
    public final static int BESTENSUCHE = 3;
    //4 = A*;
    public final static int ASTERN = 4;
    //5 = Uniform cost search
    public final static int DIJKSTRA = 5;


    protected Suche(){
        openList = new ArrayList<Knoten>();
        closedList = new ArrayList<Knoten>();
    }

    /**
     * Ist die Suche fündig geworden, gibt die start-Methode den gefundenen Zielknoten zurück,
     * über den man sich dann wiederum die entsprechenden Actions (vom Start zum bis zum Ziel),
     * über eine entsprechende Methode, holen kann
     *
     * @return Ziel Knoten
     * */
    abstract public Knoten start();

    /**
     * Die Stelle wo und ob (beachte Closelist!) ein neuer Expansionskandidat eigefügt wird,
     * bestimmt die Form der Suche
     *
     * @param expansionsKandidat
     */
    abstract protected void KnotenEinfuegen (Knoten expansionsKandidat);

    /**
     * Wie ein Knoten bewertet wird, bestimmt die Form der Suche
     *
     * @param expansionsKandidat
     */
    abstract protected void KnotenBewerten (Knoten expansionsKandidat);

    /**
     * Es wird *immer* der erste Knoten aus der Openlist entnommen
     * Die Sortierung der Openlist bestimmt die Suche bzw. Ihr :-)
     *
     * @return Erster Knoten aus der Openlist
     */
    protected Knoten popOpenlist (){
        Knoten interessantesterKnoten;
        if (!openList.isEmpty()) {
            interessantesterKnoten = openList.get(0);
            openList.remove(0);
            return interessantesterKnoten;

        }
        return null;
    }

    /**
     * Wird ein Knoten aus der Openlist "gepopt" landet
     * dieser sofort in der Closelist, damit dieser nicht noch einmal
     * expandiert wird (wir wollen keine loops im Baum!)
     *
     * @param expandierterKnoten
     */
    protected void pushCloselist (Knoten expandierterKnoten){
        if(expandierterKnoten == null)
            throw new NullPointerException("Ungültiger Expandierter Knoten");

            closedList.add(expandierterKnoten);
    }
}
