import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import java.util.HashMap;
import java.util.Map;

/**
 *This class creates an MP3 player
 * @author xyang383
 * @version 1.0
 */

public class MusicPlayer extends Application {

    private TableView table = new TableView();
    private ArrayList<Song> dataList = new ArrayList<Song>();
    private Song songMP3;
    private String keyword;
    private String cate;
    private  Map<String, String> map = new HashMap<String, String>();
    private  Song nowData;
    private  ArrayList<Song> filteredSong;

    /**
    *show the layout of mp3
    *@param args whatever input
    */
    public static void main(String[] args) {
        launch(args);
    }

    /**
    *build up the stage of the layout
    *@param stage a stage
    */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Music Player");
        stage.setWidth(450);
        stage.setHeight(500);
        File file = new File(System.getProperty("user.dir"));
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".mp3")) {
                dataList.add(new Song(f));
                System.out.println(f.getName());
            }
        }
        ObservableList<Song> data =
            FXCollections.observableArrayList(dataList);
        TableColumn fileNameCol = new TableColumn("File Name");
        fileNameCol.setMinWidth(100);
        fileNameCol.setCellValueFactory(new PropertyValueFactory<Song,
            StringProperty>("name"));
        table.getSelectionModel().setCellSelectionEnabled(true);
        TableColumn attributesCol = new TableColumn("Attributes");
        TableColumn artistCol = new TableColumn("Artist");
        artistCol.setMinWidth(100);
        artistCol.setCellValueFactory(new PropertyValueFactory<Song,
            StringProperty>("artist"));
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(new PropertyValueFactory<Song,
            StringProperty>("title"));
        TableColumn albumCol = new TableColumn("Album");
        albumCol.setMinWidth(100);
        albumCol.setCellValueFactory(new PropertyValueFactory<Song,
            StringProperty>("album"));
        table.setItems(data);
        attributesCol.getColumns().addAll(artistCol, titleCol, albumCol);
        table.getColumns().addAll(fileNameCol, attributesCol);
        Button btnPlay = new Button("Play");
        btnPlay.setDisable(false);
        Button btnPause = new Button("Pause");
        btnPause.setDisable(true);
        Button btnSearch = new Button("Search Songs");
        btnSearch.setDisable(false);
        Button btnShow = new Button("Show All Songs");
        btnShow.setDisable(true);
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                songMP3 = (Song) table.getSelectionModel().getSelectedItem();
                songMP3.play();
                btnPlay.setDisable(true); btnPause.setDisable(false);
            }
        });
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                songMP3.stop();
                btnPlay.setDisable(false); btnPause.setDisable(true);
            }
        });
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            private ArrayList<Song> filteredSong = new ArrayList<Song>();
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Search Song");
                dialog.setHeaderText("What song do you want to find?");
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.getButtonTypes().addAll(ButtonType.CANCEL,
                    ButtonType.OK);
                ButtonType btn1 = new ButtonType("File Name");
                ButtonType btn2 = new ButtonType("Artist");
                ButtonType btn3 = new ButtonType("Title");
                ButtonType btn4 = new ButtonType("Album");
                dialogPane.getButtonTypes().addAll(btn1, btn2, btn3, btn4);
                GridPane grid = new GridPane();
                grid.setHgap(10); grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));
                TextField textField = new TextField("Keyword");
                textField.setPromptText("keyword");
                grid.add(new Label("keyword:"), 0, 0);
                grid.add(textField, 1, 0);
                dialog.getDialogPane().setContent(grid);
                dialog.setResultConverter(dialogButton -> {
                        if (dialogButton.getText().equals("File Name")) {
                            map = new HashMap<String, String>();
                            map.put(textField.getText(), btn1.getText());
                            keyword = textField.getText();
                        } else if (dialogButton.getText().equals("Artist")) {
                            map = new HashMap<String, String>();
                            map.put(textField.getText(), btn2.getText());
                            keyword = textField.getText();
                        } else if (dialogButton.getText().equals("Title")) {
                            map = new HashMap<String, String>();
                            map.put(textField.getText(), btn3.getText());
                            keyword = textField.getText();
                        } else if (dialogButton.getText().equals("Album")) {
                            map = new HashMap<String, String>();
                            map.put(textField.getText(), btn4.getText());
                            keyword = textField.getText();
                        }
                        return keyword;
                    });
                    Optional<String> result = dialog.showAndWait();
                    for (int i = 0; i < dataList.size(); i++) {
                        nowData = dataList.get(i);
                        if (map.get(keyword).equals("File Name")) {
                            if (nowData.name.toString().contains(keyword)) {
                                filteredSong.add(nowData);
                            }
                        } else if (map.get(keyword).equals("Artist")) {
                            if (nowData.artist.toString().contains(keyword)) {
                                filteredSong.add(nowData);
                            }
                        } else if (map.get(keyword).equals("Title")) {
                            if (nowData.title.toString().contains(keyword)) {
                                filteredSong.add(nowData);
                            }
                        } else if (map.get(keyword).equals("Album")) {
                            if (nowData.album.toString().contains(keyword)) {
                                filteredSong.add(nowData);
                            }
                        }
                    }
                    System.out.println(map.get(keyword));
                    ObservableList<Song> filter =
                        FXCollections.observableArrayList(filteredSong);
                    for (int i = 0; i < filteredSong.size(); i++) {
                        System.out.println(filteredSong.get(i));
                    }
                    table.setItems(filter);
                    btnSearch.setDisable(true); btnShow.setDisable(false);
                    filteredSong = new ArrayList<Song>();
                }
            });
        btnShow.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    table.setItems(data);
                    btnSearch.setDisable(false); btnShow.setDisable(true);
                }
            });
        final VBox vbox = new VBox(); final HBox hbox = new HBox();
        vbox.setSpacing(5); vbox.setPadding(new Insets(10, 0, 0, 10));
        hbox.setSpacing(5); hbox.setPadding(new Insets(10, 0, 0, 10));
        hbox.getChildren().addAll(btnPlay, btnPause, btnSearch, btnShow);
        vbox.getChildren().addAll(table, hbox);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene); stage.show();
    }


/**
 *This class creates a song object
 * @author xyang383
 * @version 1.0
 */
    public class Song {
        private Media music;
        private File file;
        private StringProperty name = new SimpleStringProperty("");
        private MediaPlayer mp;
        private StringProperty title = new SimpleStringProperty("");
        private StringProperty artist = new SimpleStringProperty("");
        private StringProperty album = new SimpleStringProperty("");

        /**
        *help generate a file item
        *@param file a file object
        */
        public Song(File file) {
            music = new Media(file.toURI().toString());

            music.getMetadata()
                .addListener((MapChangeListener.Change<? extends String,
                    ? extends Object> c) -> {
                        if (c.wasAdded()) {
                            if ("artist".equals(c.getKey())) {
                                artist.set(c.getValueAdded().toString());
                            } else if ("title".equals(c.getKey())) {
                                title.set(c.getValueAdded().toString());
                            } else if ("album".equals(c.getKey())) {
                                album.set(c.getValueAdded().toString());
                            }
                        }
                    });
            mp = new MediaPlayer(music);


            this.file = file;
            this.name.set(file.getName());
        }

        /**
        *play the media player
        */

        public void play() {
            mp.play();
        }

        /**
        *pause the media player
        */

        public void pause() {
            mp.pause();
        }

        /**
        *stop the media player
        */

        public void stop() {
            mp.stop();
        }

        /**
        *get media player
        *@return mp a media player
        */

        public MediaPlayer getMediaPlayer() {
            return mp;
        }

        /**
        *get music
        *@return music
        */

        public Media getMedia() {
            return music;
        }

        /**
        *get name
        *@return name of a file
        */

        public StringProperty nameProperty() {
            return name;
        }

        /**
        *get artist
        *@return artist
        */

        public StringProperty artistProperty() {
            return artist;
        }

        /**
        *get title
        *@return title
        */

        public StringProperty titleProperty() {
            return title;
        }

        /**
        *get album
        *@return album name
        */

        public StringProperty albumProperty() {
            return album;
        }

        /**
        *get name
        *@return a name
        */

        public StringProperty getName() {
            return name;
        }

        /**
        *get title
        *@return title
        */

        public StringProperty getTitle() {
            return title;
        }

        /**
        *get artist
        *@return artist name
        */

        public StringProperty getArtist() {
            return artist;
        }

        /**
        *get album
        *@return album
        */

        public StringProperty getAlbum() {
            return album;
        }




    }

}


