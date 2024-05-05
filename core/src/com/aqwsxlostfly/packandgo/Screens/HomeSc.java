package com.aqwsxlostfly.packandgo.Screens;


import static com.aqwsxlostfly.packandgo.render.Renderer.setGameScreen;

import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.SessionState;
import com.aqwsxlostfly.packandgo.SessionStateToSend;
import com.aqwsxlostfly.packandgo.Tools.FlyingObject;
import com.aqwsxlostfly.packandgo.Tools.ScreenConsumer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class HomeSc implements Screen {

    Main main;

    Stage stage;
    Skin skin;

    Texture backgroundTexture;
    Label gameNameLabel;
    TextButton createRoomBtn;
    TextButton joinRoomBtn;
    TextField setPasswordField;
    TextField setIdField;
    TextButton connectBtn;
    TextButton joinGameBtn;
    Dialog lobbyDialog;

    private ArrayList<FlyingObject> flyingObjects;
    private Texture chairTexture, bathTexture, wardrobeTexture, tableTexture, lampTexture;


    public HomeSc(Main main) {
        this.main = main;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gameNameLabel = new Label("PACK AND GO", skin);

        createRoomBtn = new TextButton("Create Lobby", skin);
        joinRoomBtn = new TextButton("Join Lobby", skin);

        setPasswordField = new TextField("", skin);
        setIdField = new TextField("", skin);

        connectBtn = new TextButton("Connect", skin);
        joinGameBtn = new TextButton("Join Game", skin);

        backgroundTexture = new Texture(Gdx.files.internal("backgroung.png"));

        chairTexture = new Texture(Gdx.files.internal("chair.png"));
        bathTexture = new Texture(Gdx.files.internal("bath.png"));
        wardrobeTexture = new Texture(Gdx.files.internal("wardrobe.png"));
        tableTexture = new Texture(Gdx.files.internal("table.png"));
        lampTexture = new Texture(Gdx.files.internal("sofa.png"));

        flyingObjects = new ArrayList<FlyingObject>();

        for (int i = 0; i < 12; i++) {
            Texture texture = getRandomTexture();
            FlyingObject object = new FlyingObject(new TextureRegion(texture));
            flyingObjects.add(object);
            stage.addActor(object);
        }

        layoutUi();
        addListeners();
    }

    private Texture getRandomTexture() {
        int rand = MathUtils.random(0, 4);
        switch (rand) {
            case 0:
                return chairTexture;
            case 1:
                return bathTexture;
            case 2:
                return wardrobeTexture;
            case 3:
                return tableTexture;
            case 4:
            default:
                return lampTexture;
        }
    }

    private void layoutUi() {
        Table table = new Table();
        table.setFillParent(true);

        gameNameLabel.setFontScale(8.0f);

        gameNameLabel.addAction(Actions.forever(
                Actions.sequence(
                        Actions.alpha(0, 2.0f),
                        Actions.alpha(1, 5.0f)
                )
        ));

        table.add(gameNameLabel).pad(70).padBottom(50);
        table.row();

        Table buttonsTable = new Table();
        createRoomBtn.getLabel().setFontScale(2.5f);


        joinRoomBtn.getLabel().setFontScale(2.5f);
        buttonsTable.add(createRoomBtn).pad(40).width(600).height(200).uniform().expand().fill();
        buttonsTable.add(joinRoomBtn).pad(40).width(600).height(200).uniform().expand().fill();
        table.add(buttonsTable).padBottom(80);
        table.row();

        setPasswordField.setMessageText("Set Password");
        setIdField.setMessageText("Set Room ID");


        table.add(setPasswordField).padBottom(50).expand().fill();
        table.row();
        table.add(setIdField).padBottom(100).expand().fill();
        table.row();
        table.add(connectBtn).padBottom(100).expand().fill();
        table.row();
        table.add(joinGameBtn).expand().fill();

        setPasswordField.setVisible(false);
        setIdField.setVisible(false);
        connectBtn.setVisible(false);
        joinGameBtn.setVisible(false);

        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField))
                    Gdx.input.setOnscreenKeyboardVisible(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER)
                    Gdx.input.setOnscreenKeyboardVisible(false);
                return super.keyDown(event, keycode);
            }
        });


    }


    private void addListeners() {


        createRoomBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showLobbyDialog(true);
            }
        });

        joinRoomBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showLobbyDialog(false);
            }
        });

        connectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connectToServer(setPasswordField.getText(), setIdField.getText(), true);
            }
        });

        joinGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connectToServer(setPasswordField.getText(), setIdField.getText(), false);
            }
        });
    }

    private void showLobbyDialog(boolean isCreating) {
        lobbyDialog = new Dialog("Lobby", skin) {
            @Override
            protected void result(Object object) {
                hide();
            }
        };


        lobbyDialog.getTitleLabel().setFontScale(2.0f);
        lobbyDialog.getTitleTable().padTop(25).padLeft(30);


        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(2.0f);

        TextField passwordFieldDialog = new TextField("", skin);
        TextField idFieldDialog = new TextField("", skin);
        TextButton actionButtonDialog = new TextButton(isCreating ? "Connect" : "Join Game", skin);

        passwordFieldDialog.setMessageText("Set Password");
        idFieldDialog.setMessageText("Set Room ID");

        setPasswordField.setStyle(textFieldStyle);
        setIdField.setStyle(textFieldStyle);

        lobbyDialog.getContentTable().add(idFieldDialog).pad(50).padTop(80).width(650).height(180).uniform().expand().fill();
        lobbyDialog.getContentTable().row();
        lobbyDialog.getContentTable().add(passwordFieldDialog).pad(50).width(650).height(180).uniform().expand().fill();
        lobbyDialog.getButtonTable().padTop(20).padBottom(30);

        lobbyDialog.button(actionButtonDialog, true);

        actionButtonDialog.addListener(new ClickListener() { // Устанавливаем слушателя для кнопки в диалоге
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connectToServer(passwordFieldDialog.getText(), idFieldDialog.getText(), isCreating);
                lobbyDialog.hide();
            }
        });

        lobbyDialog.show(stage);
        lobbyDialog.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));


        TextButton closeButton = new TextButton("X", skin);
        lobbyDialog.getTitleTable().add(closeButton).height(lobbyDialog.getTitleLabel().getHeight());

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lobbyDialog.hide();
            }
        });

    }


    private static final String CONNECTING = "CONNECTING";
    private static final String CONNECTING_TEXT = "CONNECTING...";
    private static final String CONNECTION_ERROR = "Connection Error";
    private static final String FAILED_TO_CONNECT = "Failed to connect to the server. Check your internet.";
    private static final String CREATE_ROOM = "CREATE ROOM";
    private static final String JOIN_ROOM = "JOIN ROOM";
    private static final String SESSION_EXISTS = "Creation Error";
    private static final String SESSION_EXIST_TEXT = "Session already exist.";
    private static final String DOES_NOT_EXIST = "Joining Error";
    private static final String DOES_NOT_EXIST_TEXT = "Session does not exist.";
    private static final String INCORRECT_PASSWORD = "Joining Error";
    private static final String INCORRECT_PASSWORD_TEXT = "Incorrect session password.";

    private void connectToServer(String password, String roomId, boolean isCreating) {
        String action = isCreating ? "createRoom" : "joinRoom";
        SessionState sessionState = new SessionStateToSend(action, roomId, password);
        try {
            Main.messageSender.sendMessage(sessionState);
            startConnectionTimer(isCreating, roomId, password);
        } catch (Exception e) {
            showErrorDialog(CONNECTION_ERROR, FAILED_TO_CONNECT, 5);
            Gdx.app.log(isCreating ? "ERROR CREATE" : "ERROR JOIN", action + "State " + main.gameSession.getSessionMsg());
        }
    }

    private void startConnectionTimer(boolean isCreating, String roomId, String password) {
        Timer timer = new Timer();

        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                showErrorDialog(CONNECTING, CONNECTING_TEXT, 3);
            }
        }, 0, 500);

        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                checkConnectionStatus(isCreating, roomId, password);
            }
        }, 3, 500);
    }

    private void checkConnectionStatus(boolean isCreating, String roomId, String password) {
        if (main.gameSession.getSessionMsg() != null) {
            switch (main.gameSession.getSessionMsg()) {
                case "connected_ok":
                    Gdx.app.log(isCreating ? CREATE_ROOM : JOIN_ROOM, "Creating a room with ID: " + roomId + " and password: " + password +
                            " response " + main.gameSession.getSessionMsg());
                    setGameScreen(new PlayScreen());
                    break;
                case "session_exists":
                    showErrorDialog(SESSION_EXISTS, SESSION_EXIST_TEXT, 5);
                    break;
                case "does_not_exist":
                    showErrorDialog(DOES_NOT_EXIST, DOES_NOT_EXIST_TEXT, 5);
                    break;
                case "incorrect_password":
                    showErrorDialog(INCORRECT_PASSWORD, INCORRECT_PASSWORD_TEXT, 5);
                    break;
            }
        }
    }

    private void showErrorDialog(String title, String message, float autoCloseAfterSeconds) {
        Dialog errorDialog = new Dialog(title, skin) {
            @Override
            protected void result(Object object) {
                hide();
            }
        };

        errorDialog.getTitleLabel().setFontScale(2.0f);
        errorDialog.getTitleTable().padTop(40).padLeft(25).padBottom(30);

        errorDialog.getContentTable().add(new Label(message, skin)).pad(40);
        errorDialog.button("OK", true);
        errorDialog.show(stage);

        if (autoCloseAfterSeconds > 0) {
            errorDialog.addAction(Actions.sequence(
                    Actions.delay(autoCloseAfterSeconds),
                    Actions.run(errorDialog::hide)
            ));
        }
    }


    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(1 / 60f);
        stage.draw();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
        chairTexture.dispose();
        bathTexture.dispose();
        wardrobeTexture.dispose();
        tableTexture.dispose();
        lampTexture.dispose();

    }

    @Override
    public void screenToChange(ScreenConsumer screenConsumer) {
        Screen.super.screenToChange(screenConsumer);
    }

    @Override
    public Player getPlayer(Player player) {
        return Screen.super.getPlayer(player);
    }

    @Override
    public void updatePlayerArray(String id, float x, float y) {

    }
}