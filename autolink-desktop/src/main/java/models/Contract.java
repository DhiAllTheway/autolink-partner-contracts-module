package models;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

/**
 * A Contract between two entreprises.
 * Provides JavaFX properties for easy binding to TableView columns.
 */
public class Contract {

    /*------------------------------------------------------------------
     *  JavaFX properties
     *----------------------------------------------------------------*/
    private final IntegerProperty           id         = new SimpleIntegerProperty();
    private final ObjectProperty<Entreprise>entreprise = new SimpleObjectProperty<>();
    private final StringProperty            content    = new SimpleStringProperty();
    private final ObjectProperty<Entreprise>receiver   = new SimpleObjectProperty<>();

    /*------------------------------------------------------------------
     *  Constructors
     *----------------------------------------------------------------*/
    public Contract() {}

    public Contract(int id, Entreprise entreprise, String content, Entreprise receiver) {
        setId(id);
        setEntreprise(entreprise);
        setContent(content);
        setReceiver(receiver);
    }

    /*------------------------------------------------------------------
     *  Standard getters / setters
     *----------------------------------------------------------------*/
    public int          getId()         { return id.get(); }
    public void         setId(int id)   { this.id.set(id); }
    public Entreprise   getEntreprise() { return entreprise.get(); }
    public void         setEntreprise(Entreprise e) { this.entreprise.set(e); }
    public String       getContent()    { return content.get(); }
    public void         setContent(String c) { this.content.set(c); }
    public Entreprise   getReceiver()   { return receiver.get(); }
    public void         setReceiver(Entreprise r) { this.receiver.set(r); }

    /*------------------------------------------------------------------
     *  Property accessors (needed by TableView bindings)
     *----------------------------------------------------------------*/
    public IntegerProperty           idProperty()         { return id; }
    public ObjectProperty<Entreprise>entrepriseProperty() { return entreprise; }
    public StringProperty            contentProperty()    { return content; }
    public ObjectProperty<Entreprise>receiverProperty()   { return receiver; }

    /*------------------------------------------------------------------
     *  Convenient toString (ComboBox display etc.)
     *----------------------------------------------------------------*/
    @Override public String toString() {
        return "Contract #" + getId();
    }
}
