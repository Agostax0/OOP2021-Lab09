package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class AnotherConcurrentGUI extends JFrame{

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");

    /**
     * Builds a new CGUI.
     */
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        /*
         * Create the counter agent and start it. This is actually not so good:
         * thread management should be left to
         * java.util.concurrent.ExecutorService
         */
        final AgentC agent = new AgentC();
        new Thread(agent).start();
        /*
         * Register a listener that stops it
         */
        stop.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stop(agent);
            }
        });
        
        final AgentC countdown = new AgentC();
        
    }
    private class AgentC implements Runnable{
        private boolean flag;
        @Override
        public void run() {
            try {
                while(!this.flag) {
                    final Agent agent = new Agent();
                    new Thread(agent).start();
                    Thread.sleep(2000);
                    bastaCounting(agent);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        public void stop(AgentC a) {
            this.flag = true;
        }
        public void bastaCounting(Agent a) {
               a.stopCounting();
        }
        private class Agent implements Runnable {
            private volatile boolean stop;
            private volatile int counter;

            @Override
            public void run() {
                while (!this.stop) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                
                            }
                        });

                        this.counter++;
                        Thread.sleep(100);
                    } catch (InvocationTargetException | InterruptedException ex) {

                        ex.printStackTrace();
                    }
                }
            }
            public void stopCounting() {
                this.stop = true;
            }
        }
        
    }
    
    
    
}
