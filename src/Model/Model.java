package Model;


import java.util.ArrayList;

public class Model {

    public ArrayList<Shape> p;

    public Model() {
        this.p = new ArrayList<Shape>();
    }

    public int getPointCount()
    {
        return p.size();
    }

    public void addPoint(Shape shape) {
        p.add(shape);
    }

    public void removePoint(Shape shape) {
        this.p.remove(shape);
    }

    public Shape getShape(int i) {
        return this.p.get(i);
    }

    public void deleteArray() {
        p.clear();
    }

    public int searchShape(int x, int y) {
        int index = -1;
        for (int i = 0; i < this.p.size(); i++)
        {
            if (this.p.get(i).getX() == x && this.p.get(i).getY() == y) index = i;
        }
        return index;
    }
}
