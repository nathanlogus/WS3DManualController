package ws3dmanualcontroller.config;


import ws3dproxy.CommandExecException;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class ControllerListener implements KeyListener {
    private final Creature creature;

    public ControllerListener(Creature c) {
        System.out.println(c.getName());
        this.creature = c;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Creature Move Inwards - Button: Arrow Up
        if (e.getKeyCode() == 38 && this.creature != null) {
            try {
                this.creature.move(2, 2, 0);
            } catch (CommandExecException commandExecException) {
                System.out.println("CANT MOVE UP");
            }
        }
        // Creature Move Backwards - Button Arrow Down
        else if (e.getKeyCode() == 40 && this.creature != null) {
            try {
                this.creature.move(-2, -2, 0);
            } catch (CommandExecException commandExecException) {
                System.out.println("CANT MOVE DOWN");
            }
        }
        // Creature Rotate Left - Button Arrow Left
        else if (e.getKeyCode() == 37 && this.creature != null) {
            try {
                this.creature.move(-2, 2, 2);
            } catch (CommandExecException commandExecException) {
                System.out.println("CANT MOVE LEFT");
            }
        }
        // Creature Rotate Right - Button Arrow Right
        else if (e.getKeyCode() == 39 && this.creature != null) {
            try {
                this.creature.move(2, -2, -2);
            } catch (CommandExecException commandExecException) {
                System.out.println("CANT MOVE RIGHT");
            }
        }
        // Creature Eat a Fruit - Button Z
        else if (e.getKeyCode() == 90 && this.creature != null) {
            this.creature.updateState();
            List<Thing> thingsInVision = this.creature.getThingsInVision();
            System.out.println("Found " + thingsInVision.size() + " Things.");
            thingsInVision.stream().forEach(thing -> {
                double thingDistance = this.creature.calculateDistanceTo(thing);
                System.out.println("Thing Name: " + thing.getName());
                System.out.println("Distance to thing: " + thingDistance);
                if (thingDistance < 30) {
                    try {
                        this.creature.eatIt(thing.getName());
                    } catch (CommandExecException commandExecException) {
                        commandExecException.printStackTrace();
                    }
                }
            });
        }
        // Creature Deliver Jewels - Button: C
        else if (e.getKeyCode() == 67 && this.creature != null) {
            this.creature.updateState();
            List<Thing> thingsInVision = this.creature.getThingsInVision();
            System.out.println("Found " + thingsInVision.size() + " Things.");
            thingsInVision.stream().forEach(thing -> {
                double thingDistance = this.creature.calculateDistanceTo(thing);
                System.out.println("Thing Name: " + thing.getName());
                System.out.println("Distance to thing: " + thingDistance);
                if (thingDistance < 80) {
                    this.creature.getLeaflets().stream().forEach(leaflet -> {
                        if (leaflet.getSituation() == 1) {
                            try {
                                this.creature.deliverLeaflet(String.valueOf(leaflet.getID()));
                            } catch (CommandExecException commandExecException) {
                                commandExecException.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
        // Creature Add Thing to Bag - Button X
        else if (e.getKeyCode() == 88 && this.creature != null) {
            this.creature.updateState();
            List<Thing> thingsInVision = this.creature.getThingsInVision();
            System.out.println("Found " + thingsInVision.size() + " Things.");
            thingsInVision.stream().forEach(thing -> {
                double thingDistance = this.creature.calculateDistanceTo(thing);
                System.out.println("Thing Name: " + thing.getName());
                System.out.println("Distance to thing: " + thingDistance);
                if (thingDistance < 30) {
                    try {
                        this.creature.putInSack(thing.getName());
                    } catch (CommandExecException commandExecException) {
                        commandExecException.printStackTrace();
                    }
                }
            });
        } else {
            System.out.println("Creature is Undefined");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.creature != null) {
            try {
                this.creature.move(0, 0, 0);
            } catch (CommandExecException commandExecException) {
                System.out.println("CANT STOP CREATURE");
            }
        } else {
            System.out.println("Creature is Undefined");
        }
    }
}