package sample;
import HboxTower.Hbox_SniperTower;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import sample.Enemy;
import sample.Tower;


import sample.*;
import sample.Enemy;
import sample.Tower;
import  sample.NormalEnemy;
import sample.SniperTower;

// Man choi, dinh nghia trang thia bat dau cua game
public class GameStage {
    private final static int SCREEN_HEIGHT = 720;
    private final static int SCREEN_WIDTH = 1200;
    private final static int SCREEN_TITLEMAP = 30;
    private final static String GAME_TITLE = "Tower Defense";
    final static Image imageBullet = new Image("file:src/Assets/Bullet/Bullet.png", 30, 30, true, true);
    static int[][] MapTitle = new int[24][40];
    static Image[][] imageMap = new Image[24][40];
    static List<GameTile> listBullet = new ArrayList<>();
    private List<Enemy> normalEnemyAction = new ArrayList<>();
    private List<Point> ListRoad = new ArrayList<>();
    GraphicsContext mainGraphic;
    private Canvas mainCanvas;
    private Scene mainScene;
    private Stage mainStage;
    private Group root;
    ImageView iv; //vùng thao tác ảnh
    int curseurX = 0 ; // lưu vị trí hoành độ con trỏ chuột khi trỏ đến vùng chọn tháp
    int curseurY = 0 ; //tung độ
    SniperTower listTower = new SniperTower();

    Scanner input = new Scanner(new File("src/MapGame1.txt")); //ds tháp được đặt


    static  int i = 0;
    static  int j = 0;

    public GameStage() throws FileNotFoundException, InterruptedException {
        mainCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainGraphic = mainCanvas.getGraphicsContext2D();
        root = new Group();
        root.getChildren().add(mainCanvas);
        mainScene = new Scene(root);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setTitle(GAME_TITLE);

        Hbox_SniperTower hbox_sniperTower = new Hbox_SniperTower();
        root.getChildren().add(hbox_sniperTower.getHbox_Tower());
        hbox_sniperTower.setupGestureTarget(mainScene, MapTitle);


        List<Bullet> bulletAction = new ArrayList<>();

        LoadMap();

        Enemy ListEnemy = new NormalEnemy(ListRoad);

        Tower tower = new sample.SniperTower(100,400);
        Tower tower1 = new sample.SniperTower(600,300);
        //Tower tower2 = new SniperTower(100,400);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    DrawMap();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(hbox_sniperTower.isPut()) {
                    listTower.towerList.add(new SniperTower(hbox_sniperTower.getTower().x_pos, hbox_sniperTower.getTower().y_pos));
                    hbox_sniperTower.setPut(false);
                }
                if(!normalEnemyAction.isEmpty() && i == 0)  ListEnemy.adds(normalEnemyAction.remove(0));
                i = (i > 120) ? 0 : i + 1;
                ListEnemy.RenderList(mainGraphic);
                for(Tower t : listTower.towerList){
                    t.Render(mainGraphic, ListEnemy.getListEnemy());
                }

                hbox_sniperTower.Render_Hbox(mainGraphic);

//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //tower2.Render(mainGraphic, ListEnemy.getListEnemy());
                hbox_sniperTower.Render_Hbox(mainGraphic);
            }
        };
//        timer.wait(100);
        timer.start();


    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void DrawMap() throws FileNotFoundException {

        int x_pos = 0;
        int y_pos = 0;
        int width = SCREEN_WIDTH / 40;
        int height = SCREEN_HEIGHT / 24;

        for (int i = 0; i < 24; i++) {
            x_pos = 0;
            for (int j = 0; j < 40; j++) {
                mainGraphic.drawImage(imageMap[i][j], x_pos, y_pos);
                x_pos += width;
            }
            y_pos += height;
        }
    }

    public void LoadMap() throws FileNotFoundException {

        Image tilemap0 = new Image("file:src/Assets/Map/Map-" + 0 + ".png",
                30, 30, true, true);
        Image tilemap1 = new Image("file:src/Assets/Map/Map-" + 1 + ".png",
                30, 30, true, true);
        Image tilemap2 = new Image("file:src/Assets/Map/Map-" + 2 + ".png",
                30, 30, true, true);
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 40; j++) {
                int a = input.nextInt();
                MapTitle[i][j] = a;
            }
            //System.out.println();
        }
        int index  =0;
        int maxRoad = input.nextInt();
        while ( index < maxRoad)
        {
            int x = input.nextInt();
            int y = input.nextInt();
            //System.out.println(x + " " + y);
            ListRoad.add(new Point(x, y));
            index ++;
        }
        int maxEnemy = input.nextInt();
        index = 0;
        while (index < maxEnemy)
        {
            index ++;
            int enemy = input.nextInt();
            switch (enemy)
            {
                case 1 :
                    normalEnemyAction.add(new NormalEnemy(ListRoad));
                    break;
            }
        }
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 40; j++) {
                switch (MapTitle[i][j]) {
                    case 0:
                        imageMap[i][j] = tilemap0;
                        break;
                    case 1:
                        imageMap[i][j] = tilemap1;
                        break;
                    case 2:
                        imageMap[i][j] = tilemap2;
                        break;
                }
            }
        }
    }


    public List<Point> getListRoad() {
        return ListRoad;
    }
}
