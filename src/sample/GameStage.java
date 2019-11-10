package sample;
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
    Scanner input = new Scanner(new File("src/MapGame1.txt"));

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

        //tạo khung chọn tháp
        HBox hBoxTower = new HBox();
        hBoxTower.setPrefWidth(SCREEN_WIDTH);
        hBoxTower.setPrefHeight(SCREEN_TITLEMAP*4);
        hBoxTower.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 1;"
                + "-fx-border-style: solid;");
        hBoxTower.setTranslateX(0);
        hBoxTower.setTranslateY(SCREEN_HEIGHT);

        insertImage(new Image("file:src/Assets/Tower/SniperTower.png", 120, 120,
                true, true), hBoxTower);
        root.getChildren().add(hBoxTower);

        List<Bullet> bulletList = new LinkedList<>();

        Bullet bullet = new Bullet(imageBullet);
        Bullet bullet1 = new Bullet(imageBullet);
        Bullet bullet2 = new Bullet(imageBullet);
        Bullet bullet3 = new Bullet(imageBullet);
        Bullet bullet5 = new Bullet(imageBullet);

        Bullet bullet4 = new Bullet(imageBullet);


        bulletList.add(bullet);
        bulletList.add(bullet1);
        bulletList.add(bullet2);
        bulletList.add(bullet3);
        bulletList.add(bullet4);
        bulletList.add(bullet5);

        List<Bullet> bulletAction = new ArrayList<>();

        LoadMap();

        Enemy ListEnemy = new NormalEnemy(ListRoad);

        Tower tower = new SniperTower(100,400);
        listTower.setupGestureTarget(mainScene, imageMap);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    DrawMap();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                if(!bulletList.isEmpty() && i % 20 == 0) bulletAction.add(new Bullet(imageBullet));
                if(!normalEnemyAction.isEmpty() && i == 0)  ListEnemy.adds(normalEnemyAction.remove(0));
                i = (i > 120) ? 0 : i + 1;
//                ((sample.SniperTower) SnT).setTargetShoot(((NormalEnemy)ListEnemy).get(0).getPosition());
                ListEnemy.RenderList(mainGraphic);

                for(int i = 0; i < bulletAction.size() - 1 ; i ++)

                {
                    if(bulletAction.get(i).isShoot())
                    {
                        if(bulletAction.isEmpty())  bulletAction.remove(bulletAction.get(i));
                        continue;
                    }

                    bulletAction.get(i).setDestination( ListEnemy.get(i % ListEnemy.size()).getPosition());
                    bulletAction.get(i).Render(mainGraphic);
                }
                for(Tower t : listTower.towerList){
                    t.Render(mainGraphic);
                }
                tower.Render(mainGraphic);
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
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
            System.out.println();
        }
        int index  =0;
        int maxRoad = input.nextInt();
        while ( index < maxRoad)
        {
            int x = input.nextInt();
            int y = input.nextInt();
            System.out.println(x + " " + y);
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
    void insertImage(Image i, HBox hb){

        iv = new ImageView();
        iv.setImage(i);
        listTower.setupGestureSource(iv);
        hb.getChildren().add(iv);
    }


    public List<Point> getListRoad() {
        return ListRoad;
    }
}
