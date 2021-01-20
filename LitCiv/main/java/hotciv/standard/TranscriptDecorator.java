package hotciv.standard;

import hotciv.framework.*;

public class TranscriptDecorator implements Game {
    private Game delegate;

    public TranscriptDecorator(Game game) {
        delegate = game;
    }

    public Tile getTileAt(Position p) {
        return delegate.getTileAt(p);
    }

    public Unit getUnitAt(Position p) {
        return delegate.getUnitAt(p);
    }

    public City getCityAt(Position p) {
        return delegate.getCityAt(p);
    }

    public Player getPlayerInTurn() {
        return delegate.getPlayerInTurn();
    }

    public Player getWinner() {
        return delegate.getWinner();
    }

    public int getAge() {
        return delegate.getAge();
    }

    public boolean moveUnit(Position from, Position to) {
        System.out.println(getPlayerInTurn()+" moves "+getUnitAt(from).getTypeString()+ " from ("+from.getRow()+","+from.getColumn()+")"+" to "+"("+to.getRow()+","+to.getColumn()+")");
        return delegate.moveUnit(from, to);
    }

    public void endOfTurn() {
        System.out.println(getPlayerInTurn()+" ends turn");
        delegate.endOfTurn();
    }

    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        System.out.println(getPlayerInTurn()+" changes work force focus in city at ("+p.getRow()+","+p.getColumn()+")"+" to "+ balance);
        delegate.changeWorkForceFocusInCityAt(p, balance);
    }

    public void changeProductionInCityAt(Position p, String unitType) {
        System.out.println(getPlayerInTurn()+" changes production in city at ("+p.getRow()+","+p.getColumn()+")"+" to "+ unitType);
        delegate.changeProductionInCityAt(p, unitType);
    }

    public void performUnitActionAt(Position p) {
        delegate.performUnitActionAt(p);
    }

    public void addObserver(GameObserver observer) {
        delegate.addObserver(observer);
    }

    public void setTileFocus(Position position) {
        delegate.setTileFocus(position);
    }

    @Override
    public String getID() {
        return null;
    }
}
