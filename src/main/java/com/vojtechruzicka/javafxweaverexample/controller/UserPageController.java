package com.vojtechruzicka.javafxweaverexample.controller;

import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("user-page.fxml")
public class UserPageController extends DefaultJavaFXController {

    public static final String TITLE = "Пользовательское окно";
}
