package de.fh;

import de.fh.agent.PacmanAgent;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanActionEffect;

import de.fh.agent.Agent;
import de.fh.pacman.enums.PacmanTileType;


/**
 * DIESE KLASSE VERÄNDERN SIE BITTE NUR AN DEN GEKENNZEICHNETEN STELLEN
 * wenn die Bonusaufgabe bewertet werden soll.
 */
public class MyAgent extends PacmanAgent {

    private PacmanPercept percept;
    private PacmanActionEffect actionEffect;
    private PacmanTileType[][] view;

    public MyAgent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

    public static void main(String[] args) {
        MyAgent agent = new MyAgent("");
		Agent.start(agent, "127.0.0.1", 5000);
    }

    /**
     * In dieser Methode kann das Wissen über die Welt (der State, der Zustand)
     * entsprechend der aktuellen Wahrnehmungen anpasst, und die "interne Welt",
     * die Wissensbasis, des Agenten kontinuierlich ausgebaut werden.
     *
     * Wichtig: Diese Methode wird aufgerufen, bevor der Agent handelt, d.h.
     * bevor die chooseAction()-Methode aufgerufen wird...
     *
     * @param percept Aktuelle Wahrnehmung
     * @param actionEffect Reaktion des Servers auf vorhergewählte Aktion
     */
    @Override
        public void updateState(PacmanPercept percept, PacmanActionEffect actionEffect) {
        /**
         * Je nach Sichtbarkeit & Schwierigkeitsgrad (laut Serverkonfiguration)
         * aktuelle Wahrnehmung des Agenten.
         * Beim Pacman erhalten Sie je nach Level mehr oder weniger Mapinformationen.
         */
        this.percept = (PacmanPercept) percept;

        /**
         * Aktuelle Reaktion des Server auf die letzte übermittelte Action.
         *
         // Alle möglichen Serverrückmeldungen:
         if(actionEffect == PacmanActionEffect.GAME_INITIALIZED) {
         //Erster Aufruf
         }

         if(actionEffect == PacmanActionEffect.GAME_OVER) {
         //Das Spiel ist verloren
         }


         if(actionEffect == PacmanActionEffect.BUMPED_INTO_WALL) {
         //Letzte Bewegungsaktion führte in eine Wand
         }

         if(actionEffect == PacmanActionEffect.MOVEMENT_SUCCESSFUL) {
         //Letzte Bewegungsaktion war gültig
         }


         if(actionEffect == PacmanActionEffect.DOT_EATEN) {
         //Ein Dot wurde gefressen
         }
         */

        this.actionEffect = actionEffect;


        /*
        percept.getView() enthält die aktuelle Felderbelegung je nach Level/Sichtweite in einem Array

		Die Interpretationen der Werte können der public PacmanTileType--Klasse
		entnommen werden

        Für den Pacman sind folgende Felderbelegungen möglich

		PacmanTileType.WALL
        PacmanTileType.DOT
        PacmanTileType.EMPTY
        PacmanTileType.PACMAN
        PacmanTileType.GHOST
        PacmanTileType.GHOST_AND_DOT
        PacmanTileType.GHOST_HUNTER
        PacmanTileType.GHOST_PASSIVE
        PacmanTileType.GHOST_RANDOM
		*/





        //Beispiel:
        view = this.percept.getView();


        //Gebe den aktuellen Zustand der Welt aus
        String view_row = "";
        System.out.println("viewsize: " + view.length + "*" + view[0].length);
        for (int x = 0; x < view[0].length; x++) {
            for (int y = 0; y < view.length; y++) {
                view_row += " " + view[y][x];
            }
            System.out.println(view_row);
            view_row = "";
        }
        System.out.println("-------------------------------");


        /**
         * TODO [1]: Erweitern Sie diese updateState-Methode gemäß der Aufgabenstellung
         */


	}

    /**
     * Unsere Action-Methode (s. Russel / Norwig) wählt die nächste(n) sinnvolle(n) Aktion(en), gemäß Regeln.
     * @param
     * @return Die nächste Pacman-Action die vom Server ausgeführt werden soll
     */
    @Override
    public PacmanAction action() {


        /**
         * ChooseAction:
         * Diesen Part  erweitern Sie so, dass die nächste(n) sinnvolle(n) Aktion(en),
         * auf Basis der vorhandenen Zustandsinformationen und gegebenen Zielen, ausgeführt wird/werden.
         * Der chooseAction-Part soll den Agenten so intelligent wie möglich handeln lassen.
         *
         * Zum Beispiel:
         * Wenn die letzte Wahrnehmung ein Haufen von Dots in einem bestimmten Bereich der
         * Pacman-Welt ist, dann wird der Agent sicher eine intelligente Entscheidung treffen
         * können, welches das/die nächste zu betretende(n) Feld(er) ist/sind.
         *
         *
         * Mögliche PacmanActions sind möglich:
         * PacmanAction.GO_EAST
         * PacmanAction.GO_NORTH
         * PacmanAction.GO_SOUTH
         * PacmanAction.GO_WEST
         * PacmanAction.QUIT_GAME
         * PacmanAction.WAIT
         */


        if(actionEffect == PacmanActionEffect.GAME_INITIALIZED) {
            //Erster Aufruf
            nextAction = PacmanAction.GO_EAST;
        }

        return nextAction;
	}
}
