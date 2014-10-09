package com.ssbb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by calvin on 2014/10/06.
 */
public class PathFinder {
    public int mapHeight;
    public int mapWidth;
    SquishyBlock game;
    Array<RectangleMapObject> obstacles;
    Node[][] nodes;
    PriorityQueue<Node> open;
    HashSet<Node> closed;

    public PathFinder(SquishyBlock game) {
        mapHeight = game.mapHeight / 64;
        mapWidth = game.mapWidth / 64;
        this.game = game;
        obstacles = game.obstacles;
        createGrid();
        open = new PriorityQueue<Node>();
        closed = new HashSet<Node>();
    }

    public int[] nextMove(Sprite hunter, Sprite prey) {
        int hunterX = (int) hunter.getX() / 64;
        int hunterY = (int) hunter.getY() / 64;
        Node child = nodes[hunterX][hunterY];
        System.out.println("This is happening I guess:" + 2);

        if (null == child.child) {
            findPath(hunter, prey);
        }
        int[] toReturn = {child.child.x, child.child.y};
        System.out.println("Returning" + toReturn);

        return toReturn;
    }

    public void findPath(Sprite hunter, Sprite prey) {
        int hunterX = (int) hunter.getBoundingRectangle().x /64;
        int hunterY = (int) hunter.getBoundingRectangle().y /64;
        int preyX = (int) prey.getBoundingRectangle().x / 64;
        int preyY = (int) prey.getBoundingRectangle().y / 64;
        System.out.println("Hunter is at" + hunterX + " , " + hunterY);
        System.out.println("Prey is at" + preyX + " , " + preyY);

        open.clear();
        closed.clear();
        int currentX = hunterX;
        int currentY = hunterY;
        int g = 0;
        Node next = nodes[currentX][currentY];
        next.x = currentX;
        next.y = currentY;
        next.parent = null;
        Node objective = nodes[preyX][preyY];
        Node current = null;
        boolean done = false;
        closed.add(next);

        do {
            System.out.println("Entering loop, looking at node at" + next.x + " " + next.y + "," + next.f);
            g++;
            for (int i = -1; i < 2; i++) {
                if (done) break;
                for (int j = -1; j < 2; j++) {
                    int x = currentX + i;
                    int y = currentY + j;
                    if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight && (x != currentX || y != currentY) && (j ==0 || i == 0)) {
                        current = nodes[x][y];
                        if (!closed.contains(current) && !open.contains(current)) {
                            if (current.pass) {
                                System.out.println("adding " + x +" , " + y);
                                int h = Math.max(Math.abs(currentX - preyX), Math.abs(currentY - preyY));
                                int f = h + g;
                                current.set(g, h, f, x, y, next);
                                if (current == objective) {
                                    done = true;
                                    break;
                                }
                                open.add(current);
                            }
                        }
                    }
                }
            }
            if (done) break;

            closed.add(next);
            next = open.remove();
            while (closed.contains(next)){
                next = open.remove();
            }
            g = next.g;
            currentX = next.x;
            currentY = next.y;
        } while (open.size() > 0 );
        System.out.println("Does this happen I wonder?");

        if (current != objective) {
            // dead end
        } else {
            retrace(current);
        }

    }

    public void retrace(Node current) {
        Node temp;
        current.child = null;
        while (current.parent != null ) {
            System.out.println(current);
            temp = current;
            current = current.parent;
            current.child = temp;
        }
    }

    public void createGrid() {
        nodes = new Node[mapWidth][mapHeight];
        for (RectangleMapObject o : obstacles) {
            fill(o);
        }
    }

    public void fill(RectangleMapObject o) {
        int blx = (int) o.getRectangle().x;
        int bly = (int) o.getRectangle().y;
        int width = (int) o.getRectangle().width;
        int height = (int) o.getRectangle().height;


        for (int j = bly / 64; j <= (bly + height) / 64; j++) {
            for (int i = blx / 64; i <= (blx + width) / 64; i++) {
                nodes[i][j] = new Node(false);
                System.out.println(nodes[i][j] + "is inpassable bly" + i + "  blx  " + j );
            }
        }

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (null == nodes[i][j]) {
                    nodes[i][j] = new Node(true);
                }
            }
        }
    }

    public void reset() {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                nodes[i][j].reset();
            }
        }
    }

    public class Node implements Comparable<Node> {
        public Node parent;
        public Node child;
        public int g;
        public int h;
        public int f;
        public int x;
        public int y;
        boolean pass;
        boolean set;

        public Node(boolean pass) {
            this.pass = pass;
            set = false;
        }

        public void reset() {
            set = false;
        }

        public void clear() {
            child = null;
        }

        public boolean hasPath() {
            return child != null;
        }

        public void set(int g, int h, int f, int x, int y, Node parent) {
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = f;
            this.x = x;
            this.y = y;
            set = true;
        }

        @Override
        public int compareTo(Node node) {
            return this.f - node.f;
        }

        public String toString(){
            return "x: " + x + " y: " + y;
        }
    }
}
