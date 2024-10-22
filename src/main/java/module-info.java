module hu.szakdolgozat {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;
    requires javafx.graphics;
    exports hu.szakdolgozat.view;
    exports hu.szakdolgozat.model;
    opens hu.szakdolgozat.model to com.fasterxml.jackson.databind;

}
