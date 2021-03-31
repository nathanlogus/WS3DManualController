package ws3dmanualcontroller;

import ws3dmanualcontroller.config.ControllerListener;
import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;
import ws3dproxy.model.WorldPoint;
import ws3dproxy.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerStart extends JPanel {
    private static int initialFood = 3;
    private static int initialJewels = 3;
    private static int spawnFood = 10000;
    private static int spawnJewels = 5000;
    private static int updateStatus = 500;

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
            WorldPoint creatureStartPosition = World.getRandomTarget();
            Creature c = proxy.createCreature(creatureStartPosition.getX(), creatureStartPosition.getY(), 0);
            c.start();
            initializeJFrameController(c, w);
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

    private static void initializeJFrameController(Creature c, World w) {
        JFrame frame = new JFrame("WS3D Controller");
        frame.setLayout(new GridLayout(0, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JLabel creatureName = new JLabel("Creature Name: ");
        JLabel creatureScore = new JLabel("Creature Score: ");
        JLabel creaturePosition = new JLabel("Creature Position: X ");
        JLabel creatureEnergy = new JLabel("Creature Energy: ");
        JLabel creatureSpeed = new JLabel("Creature Speed: ");
        JLabel thingsInView = new JLabel("Things in view: ");
        JLabel nPFood = new JLabel("Non Perishable Food in Bag: ");
        JLabel pFood = new JLabel("Perishable Food in Bag: ");
        JLabel redCrystals = new JLabel ("Red Crystals in Bag: ");
        JLabel blueCrystals = new JLabel ("Blue Crystals in Bag: ");
        JLabel greenCrystals = new JLabel ("Green Crystals in Bag: ");
        JLabel yellowCrystals = new JLabel ("Yellow Crystals in Bag: ");
        JLabel magentaCrystals = new JLabel ("Magenta Crystals in Bag: ");
        JLabel whiteCrystals = new JLabel ("White Crystals in Bag: ");
        JLabel leafletOne = new JLabel("Leaflet 1: ");
        JLabel leafletTwo = new JLabel("Leaflet 2: ");
        JLabel leafletThree = new JLabel("Leaflet 3: ");

        frame.add(creatureName);
        frame.add(creatureScore);
        frame.add(creaturePosition);
        frame.add(creatureEnergy);
        frame.add(creatureSpeed);
        frame.add(thingsInView);
        frame.add(nPFood);
        frame.add(pFood);
        frame.add(redCrystals);
        frame.add(blueCrystals);
        frame.add(greenCrystals);
        frame.add(yellowCrystals);
        frame.add(magentaCrystals);
        frame.add(whiteCrystals);
        frame.add(leafletOne);
        frame.add(leafletTwo);
        frame.add(leafletThree);

        Timer timer = new Timer();
        TimerTask statusTask = new TimerTask() {
            @Override
            public void run() {
                c.updateState();
                c.updateBag();
                creatureName.setText("Creature Name: " + c.getName());
                creatureScore.setText("Creature Score: " + c.getAttributes().getScore());
                creaturePosition.setText("Creature Position: X " + Math.ceil(c.getPosition().getX()) + "/ Y " + Math.ceil(c.getPosition().getY()));
                creatureEnergy.setText("Creature Energy: " + c.getFuel());
                creatureSpeed.setText("Creature Speed: " + c.getSpeed());
                thingsInView.setText("Things in view: " + c.getThingsInVision().toString());
                if (c.getBag() != null) {
                    nPFood.setText("Non Perishable Food in Bag: " + c.getBag().getNumberNPFood());
                    pFood.setText("Perishable Food in Bag: " + c.getBag().getNumberPFood());
                    redCrystals.setText("Red Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorRED));
                    blueCrystals.setText("Blue Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorBLUE));
                    greenCrystals.setText("Green Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorGREEN));
                    yellowCrystals.setText("Yellow Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorYELLOW));
                    magentaCrystals.setText("Magenta Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorMAGENTA));
                    whiteCrystals.setText("White Crystals in Bag: " + c.getBag().getNumberCrystalPerType(Constants.colorWHITE));
                }
                if(c.getLeaflets() != null) {
                    leafletOne.setText("Leaflet 1: " + c.getLeaflets().get(0).getWhatToCollect() + " - Status: " + c.getLeaflets().get(0).getSituation());
                    leafletTwo.setText("Leaflet 2: " + c.getLeaflets().get(1).getWhatToCollect() + " - Status: " + c.getLeaflets().get(1).getSituation());
                    leafletThree.setText("Leaflet 3: " + c.getLeaflets().get(2).getWhatToCollect() + " - Status: " + c.getLeaflets().get(2).getSituation());
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        };

        timer.schedule(statusTask, 0, updateStatus);

        ControllerStart keyboardExample = new ControllerStart(c);
        frame.add(keyboardExample);
        frame.setVisible(true);
    }
}
