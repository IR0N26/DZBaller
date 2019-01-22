
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import xobot.client.callback.listeners.PaintListener;
import xobot.script.ActiveScript;
import xobot.script.Manifest;
import xobot.script.methods.Bank;
import xobot.script.methods.GameObjects;
import xobot.script.methods.NPCs;
import xobot.script.methods.Packets;
import xobot.script.methods.Players;
import xobot.script.methods.Walking;
import xobot.script.methods.Widgets;
import xobot.script.methods.tabs.Inventory;
import xobot.script.methods.tabs.Skills;
import xobot.script.util.Time;
import xobot.script.util.Timer;
import xobot.script.wrappers.Tile;
import xobot.script.wrappers.interactive.GameObject;
import xobot.script.wrappers.interactive.Item;
import xobot.script.wrappers.interactive.NPC;

@Manifest(authors = { "IR0N" }, name = "IR0N's DZBaller", description = "Makes cannonballs at ::DZ. Start with only the mould in your inventory at ::DZ.")
    public class DZBaller extends ActiveScript implements PaintListener {

        private Timer t = null;
        private int startXP = 0;


        private final Timer timer = new Timer();

        public boolean onStart() {
        startXP = Skills.SMITHING.getCurrentExp();
        t = new Timer(System.currentTimeMillis());
        return true;
        }

    @Override
        public int loop() {
        int BAR = 2353;
        if (Inventory.Contains(BAR) && Time.sleep(() -> Players.getMyPlayer().getAnimation() != -1, 1000)) {
            return 1000;
        }
        if (Inventory.Contains(BAR)) {
            int FURNACE = 3044;
            GameObject furnace = GameObjects.getNearest(FURNACE);
            int MOULD = 4;
            Item mould = Inventory.getItem(MOULD);
            Walking.walkTo(new Tile(2538, 3895));
            Time.sleep(50);
            mould.interact("use");
            Time.sleep(1000);
            furnace.interact("use-with");
            System.out.println("Making Cannonballs");
        } else {
            Walking.walkTo(new Tile(2540, 3893));
            Time.sleep(50);
            NPC bank = NPCs.getNearest(494);
            bank.interact("Bank");
            Time.sleep(Bank::isOpen, 12000);
            System.out.println("Opening bank");
            if (Time.sleep(() -> Widgets.getOpenInterface() == 5292, 1200)) {
                Item i = Bank.getItem(BAR);
                if (i != null) {
                    Bank.withdraw(BAR, 28);
                    Time.sleep(Inventory::isFull, 8000);
                    System.out.println("Withdrawing bars");
                }
            }

        }
        return 100;
    }

    private final Color color1 = new Color(102, 102, 102, 218);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(0, 255, 255);
    private final Color color4 = new Color(255, 255, 255);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 22);
    private final Font font2 = new Font("Arial", 0, 16);

    public void repaint(Graphics g1) {
        int xp = Skills.SMITHING.getCurrentExp() - startXP;
        int xph = (int) ((xp) * 3600000D / (t.getElapsed()));

        Graphics2D g = (Graphics2D)g1;
        g.setColor(color1);
        g.fillRect(2, 0, 175, 100);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(2, 0, 175, 100);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("DZ Baller", 20, 25);
        g.setFont(font2);
        g.setColor(color4);
        g.drawString("XP: " + xp, 25, 65);
        g.drawString("XP(h): " + xph, 25, 80);
        g.drawString(t.toElapsedString(), 25, 50); //Time Elapsed
    }
}