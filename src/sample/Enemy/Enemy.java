package sample;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import sample.*;
import sample.Enemy;
import sample.Tower;
import  sample.NormalEnemy;
import sample.SniperTower;

enum Direction{
    LEFT(180), RIGHT(0), UP(270), DOWN(90);
    int direction;
     Direction(int i){
        this.direction = i;
    }
    public int getDirection() {
        return direction;
    }
}
public abstract class Enemy extends GameEntity {
    protected int speed ;
    protected int Blood;
    protected int armor;
    private List<Enemy> normalEnemies = new ArrayList<>();
    static final int angle_Right = 0;
    static final int angle_Left = 180;
    static final int angle_Up =  90 ;
    static final int angle_Down = 270;
    protected Point point;
    protected List<Point> roadList = new ArrayList<>();
    protected int angle = 0;
    protected  int i ;
    protected int dri ;
    public void adds(Enemy normalEnemy)
    {
        normalEnemies.add(normalEnemy);
    }
    public void loadRoad()
    {
        Point point = new Point(0, 170);
        Point point1 = new Point(200,  170);
        Point point2 = new Point(200,  620);
        Point point3 = new Point(650,  620);
        Point point4 = new Point(650,  80);
        Point point5 = new Point(230,  170);
        roadList.add(point);
        roadList.add(point1);
        roadList.add(point2);
        roadList.add(point3);
        roadList.add(point4);
        roadList.add(point5);
    }
    public void Move() {
        /***** To do handle move of Enemy*******/
        switch (getDri())
        {
            case angle_Right:
                this.x_pos += speed;
                break;
            case angle_Left:
                this.x_pos -= speed;
                break;
            case angle_Down:
                this.y_pos += speed;
                break;
            case angle_Up :
                this.y_pos -= speed;
                break;
        }
        int delta_x = this.roadList.get(i + 1).getX() - this.x_pos;
        int delta_y = this.roadList.get(i + 1).getY() - this.y_pos;
        if(delta_x == 0 && delta_y == 0 && i < roadList.size())
        {
            System.out.println(i++);
        }
        if(this.x_pos > 1200) {
            i = 0;
            setPosition(this.roadList.get(0).getX(), this.roadList.get(0).getY());
            setDri(angle_Right);
        }
        if(this.dri != nextRoad())
        {
            this.dri = nextRoad();
//            angle += 90;
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            ImageView imageView = new ImageView(this.image);
            imageView.setRotate(imageView.getRotate() + 90);
            this.image = imageView.snapshot(snapshotParameters, null);
        }
    }
    public int nextRoad()
    {
        int delta_x =this.roadList.get(i + 1).getX() - this.roadList.get(i).getX();
        int delta_y = this.roadList.get(i + 1).getY() - this.roadList.get(i).getY();
        if(delta_x == 0 && delta_y > 0)
        {
            //System.out.println(x_pos + " " + y_pos);
            //System.out.println("DOWN");
            return angle_Down;
        }
        if(delta_x == 0 && delta_y < 0)
        {
            return angle_Up;
        }
        if(delta_x > 0 && delta_y == 0)
        {
            return angle_Right;
        }
        if(delta_x < 0 && delta_y == 0)
        {
            return angle_Left;
        }
        return  0;
    }
    public Point getPosition()
    {
        point = new Point(x_pos + 15, y_pos + 20);
        return point;
    }
    public void RenderList(GraphicsContext gc)
    {
        for (Enemy normalEnemy : normalEnemies)
        {
            normalEnemy.Render(gc);
        }
    }
    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getArmor() {
        return armor;
    }

    public int getBlood() {
        return Blood;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFirst_Blood(int first_Blood) {
        Blood = first_Blood;
    }
    public void bleed(int blood_delta)
    {
        this.Blood -= blood_delta;
    }
    public boolean is_dead()
    {
        return  (this.Blood <= 0);
    }

    @Override
    public void loadImage(String path) {
        this.image = new Image(path + ".png", 50, 50, true, true);
    }
    public int getDri() {
        return dri;
    }
    public void setDri(int dri) {
        this.dri = dri;
    }
    public Enemy get(int index)
    {
        return normalEnemies.get(index);
    }
    public int size()
    {
        return normalEnemies.size();
    }
}