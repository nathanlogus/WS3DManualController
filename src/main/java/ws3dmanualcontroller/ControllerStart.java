package ws3dmanualcontroller;

import ws3dmanualcontroller.config.ControllerListener;
import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;
import ws3dproxy.model.WorldPoint;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerStart extends JPanel {
    private static int initialFood = 3;
    private static int initialJewels = 3;
    private static int spawnFood = 10000;
    private static int spawnJewels = 5000;

    public ControllerStart(Creature c) {
        KeyListener listener = new ControllerListener(c);
        addKeyListener(listener);
        setFocusable(true);
    }

    public static void main(String[] args) throws CommandExecException {
        WS3DProxy proxy = new WS3DProxy();
        try {
            World w = World.getInstance();
            w.reset();
            Creature c = proxy.createCreature(100, 450, 0);
            c.start();
            initializeJFrameController(c);
            createInitialFoodInWorld(w);
            createInitialJewelsInWorld(w);
            createDeliverySpot(w);
            createSchedulers(w);

        } catch (CommandExecException e) {
            System.out.println("Erro capturado");
        }
    }

    private static void createSchedulers(World w) {
        Timer timer = new Timer();
        TimerTask foodTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    createFood(w);
                } catch (CommandExecException e) {
                    e.printStackTrace();
                }
            }
        };

        TimerTask jewelTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    createJewel(w);
                } catch (CommandExecException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(foodTask, 0, spawnFood);
        timer.schedule(jewelTask, 0, spawnJewels);
    }

    private static void createFood(World w) throws CommandExecException {
        WorldPoint randomPoint = World.getRandomTarget();
        int foodType = (int) Math.round(Math.random());
        w.createFood(foodType, randomPoint.getX(), randomPoint.getY());
    }

    private static void createJewel(World w) throws CommandExecException {
        WorldPoint randomPoint = World.getRandomTarget();
        int jewelType = (int) (Math.random() * ((5 - 0) + 1));
        w.createJewel(jewelType, randomPoint.getX(), randomPoint.getY());
    }

    private static void createInitialFoodInWorld(World w) throws CommandExecException {
        for (int i = 0; i < initialFood; i++) {
            createFood(w);
        }
    }

    private static void createInitialJewelsInWorld(World w) throws CommandExecException {
        for (int i = 0; i < initialJewels; i++) {
            createJewel(w);
        }
    }

    private static void createDeliverySpot(World w) throws CommandExecException {
        WorldPoint randomPoint = World.getRandomTarget();
        w.createDeliverySpot(randomPoint.getX(), randomPoint.getY());
    }

    private static void initializeJFrameController(Creature c) {
        JFrame frame = new JFrame("WS3D Controller");
        frame.setSize(600, 400);
        JLabel creatureNameLabel = new JLabel("Creature Name: " + c.getName(), JLabel.CENTER);
        JLabel movementLabel = new JLabel("Movement: Arrow Keys, Eat Food: Z, Add Thing to Bag: X, Deliver Leaflets: C");
        creatureNameLabel.setVerticalTextPosition(JLabel.TOP);
        frame.add(creatureNameLabel);
        frame.add(movementLabel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ControllerStart keyboardExample = new ControllerStart(c);
        frame.add(keyboardExample);
    }
}
