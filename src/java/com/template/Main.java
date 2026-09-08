package com.template;

import com.template.controller.MainController;
import com.template.validator.ILivrosValidator;
import com.template.validator.LivrosValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        ILivrosValidator livrosValidator = new LivrosValidator();

        // Configura o FXMLLoader com uma ControllerFactory para que o
        // MainController receba o LivrosValidator via construtor.
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                return new MainController(livrosValidator);
            }
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao instanciar o controller: " + controllerClass, e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Hello");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
