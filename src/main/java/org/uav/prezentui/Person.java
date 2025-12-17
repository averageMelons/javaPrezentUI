package org.uav.prezentui;

import javafx.beans.property.*;
import java.time.*;

public class Person {
    private SimpleStringProperty date;
    private SimpleStringProperty name;
    private SimpleStringProperty curs;
    private SimpleStringProperty lab;
    private SimpleStringProperty materie;

    public Person(String __date, String __name, String __materie, String __curs, String __lab) {
        this.date = new SimpleStringProperty(__date);
        this.name = new SimpleStringProperty(__name);
        this.curs = new SimpleStringProperty(__curs);
        this.lab = new SimpleStringProperty(__lab);
        this.materie = new SimpleStringProperty(__materie);
    }
    public String getDate() {
        return date.get();
    }
    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getName() {
        return name.get();
    }

    public String getMaterie() {
        return materie.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getCurs() {
        return curs.get();
    }

    public SimpleStringProperty cursProperty() {
        return curs;
    }

    public String getLab() {
        return lab.get();
    }

    public SimpleStringProperty labProperty() {
        return lab;
    }
}
