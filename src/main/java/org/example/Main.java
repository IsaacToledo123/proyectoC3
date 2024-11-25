import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class Main extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("FXGL Game");
        settings.setVersion("1.0");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
