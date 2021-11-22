package it.unibo.oop.lab.reactivegui02;

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

import org.w3c.dom.css.Counter;

/**
 * Exercise on a reactive GUI.
 */
public final class Test extends JFrame{
    final private JLabel display = new JLabel();
    final private JButton upBtn = new JButton("up");
    final private JButton downBtn = new JButton("down");
    final private JButton stopBtn = new JButton("stop");
    
    private Test() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize( (int)(screenSize.getWidth()*0.5), (int)(screenSize.getHeight()*0.5));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel canvas = new JPanel();
        canvas.add(display);
        canvas.add(this.upBtn);
        canvas.add(this.downBtn);
        canvas.add(this.stopBtn);
        this.getContentPane().add(canvas);
        this.setVisible(true);
        final Action counter = new Action();
        new Thread(counter).start();
        this.display.setText(String.valueOf(counter.getCounter()));
        this.upBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                btnPress(e);
                counter.changeValue(1);
            }
        });
        this.stopBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                btnPress(e);
                counter.forcestop();
            }
        });
        this.downBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                btnPress(e);
                counter.changeValue(-1);
            }
        });
        
        
    }
    
    private void btnPress(ActionEvent e) {
        var called = e.getSource();
        if(called == upBtn) {
            upBtn.setEnabled(false);
            downBtn.setEnabled(true);
        }
        else if(called == downBtn) {
            upBtn.setEnabled(true);
            downBtn.setEnabled(false);
        }
        else if(called == stopBtn) {
            upBtn.setEnabled(false);
            downBtn.setEnabled(false);
        }
    }
    
    private void newValue(final long value) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    display.setText(String.valueOf(value));
                    
                }
                
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Realizzare una classe ConcurrentGUI con costruttore privo di argomenti,
     * tale che quando istanziata crei un JFrame con l'aspetto mostrato nella
     * figura allegata (e contatore inizialmente posto a zero).
     * 
     * Il contatore venga aggiornato incrementandolo ogni 100 millisecondi
     * circa, e il suo nuovo valore venga mostrato ogni volta (l'interfaccia sia
     * quindi reattiva).
     * 
     * Alla pressione del pulsante "down", il conteggio venga da lì in poi
     * aggiornato decrementandolo; alla pressione del pulsante "up", il
     * conteggio venga da lì in poi aggiornato incrementandolo; e così via, in
     * modo alternato.
     * 
     * Alla pressione del pulsante "stop", il conteggio si blocchi, e i tre
     * pulsanti vengano disabilitati. Per far partire l'applicazioni si tolga il
     * commento nel main qui sotto.
     * 
     * Suggerimenti: - si mantenga la struttura dell'esercizio precedente - per
     * pilotare la direzione su/giù si aggiunga un flag booleano all'agente --
     * deve essere volatile? - la disabilitazione dei pulsanti sia realizzata
     * col metodo setEnabled
     */

    /**
     * 
     * @param args
     *            possible args to pass (not used)
     * 
     */
    private class Action implements Runnable{
        private long counter;
        private boolean stop;
        private int value;
        
        @Override
        public void run() {
            try {
                while(!this.stop) {
                    this.counter+=value;
                    newValue(this.counter);
                    Thread.sleep(100);    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        public long getCounter() {
            return this.counter;
        }
        public void changeValue(final int value){
            this.value = value;
        }
        public void forcestop() {
            this.stop = true;
        }
        
    }
    
    public static void main(final String... args) {
        new Test();
    }
}
