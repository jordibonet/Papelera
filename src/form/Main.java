package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private JPanel panelMain;
    private JPanel panelTitle;
    private JPanel panelCenter;
    private JLabel labelTime;
    private JLabel labelTrash;
    private JButton buttonPause;
    private int seconds = 0;
    private JLabel labelPuntuacion;
    private int puntuacion = 0;
    private JLabel labelVidas;
    private int vidas = 3;
    private JLabel labelPaper;
    private final Timer timer;
    private final Timer timerPaper;

    public Main() {
        //Se declaran las dimensiones de la pantalla Main y que se detecten los eventos
        panelMain.setPreferredSize(new Dimension(800, 600));
        panelMain.setSize(800,600);
        panelMain.setLayout(null);
        panelMain.setFocusable(true);

        showPanelTitle();
        showPanelCenter();

        timer = new Timer(1000, new TimerActionListener());
        timer.start();

        timerPaper = new Timer(1, new TimerPaper());
        timerPaper.start();

        panelMain.addKeyListener(new PanelMainKeyListener());
        panelMain.requestFocusInWindow();
        buttonPause.addMouseListener(new ButtonPauseListener(timer, timerPaper));

    }

    private void paperLlegaInferiror() {
        // Si el papel pasa la parte inferior de la pantalla se resta una vida, el papel vuelve a aparacer en una posición
        // horizontal aleatoria en la altura 5 y se comprueba el metodo checkGameOver()
        if (labelPaper.getY() > panelCenter.getHeight() - labelPaper.getHeight()-20){
            vidas--;
            labelPaper.setLocation(1+ (int)(Math.random() * panelCenter.getWidth()), 5);
            labelVidas.setText("VIDAS: "+vidas);
            checkGameOver();
        } else if (paperLlegaPapelera()){
            // Si el metodo paperLlegaPapelera() es true, la puntuación se suma 10 puntos, papel vuelve a aparacer en una
            // posición horizontal aleatoria en la altura 5 y se comprueba el metodo CheckGameWin()
            puntuacion += 10;
            labelPuntuacion.setText("PUNTUACIÓN: " +puntuacion);
            labelPaper.setLocation(1+ (int)(Math.random() * panelCenter.getWidth()), 5);
            checkGameWin();
        }
    }

    private boolean paperLlegaPapelera() {

        // Se devolvera un true si la posicion Y del papel más su altura es mayor o igual a la posición Y
        // de la papeleria y si tambien la posicioón Y del papel es menor o igual a la de la papelera. Lo que comprueba
        // que de true siempre que el papel toque la posición Y de la paplera

        return labelPaper.getY() + labelPaper.getHeight() >= labelTrash.getY() && labelPaper.getY() <= labelTrash.getY();
    }

    private void checkGameOver() {
        // Metodo que comprueba si pierdes si el contador de vidas llega a 0
        if (vidas <= 0 ){
            timer.stop();
            timerPaper.stop();
            JOptionPane.showMessageDialog(panelMain, "HAS PERDIDO");
            resetGame();
        }
    }

    private void checkGameWin(){
        // Metodo que comprueba si ganas si el contador de puntuación llega a 50
        if (puntuacion >= 50) {
            timer.stop();
            timerPaper.stop();
            JOptionPane.showMessageDialog(panelMain, "HAS GANADO");
            resetGame();
        }
    }

    private void resetGame() {
        // Se reseta el juego volviendo las variables a su estado inicial y iniciando los eventos
        puntuacion = 0;
        vidas = 3;
        seconds = 0;
        labelPuntuacion.setText("PUNTUACIÓN " +puntuacion);
        labelVidas.setText("VIDAS: " +vidas);
        labelTime.setText(seconds +" segundos");
        labelPaper.setLocation(1 + (int)(Math.random() * panelCenter.getWidth()), 5);
        timer.start();
        timerPaper.start();
        panelMain.requestFocusInWindow();
    }

    private class PanelMainKeyListener extends KeyAdapter{
        // Evento que habilita poder mover la papelera con las flechas del teclado
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int x = labelTrash.getX();

            switch (e.getKeyCode()){
                case KeyEvent.VK_RIGHT -> x += 5;
                case KeyEvent.VK_LEFT -> x -= 5;
            }
            if (x>= 0 && x < panelMain.getWidth() - labelTrash.getWidth()){
                labelTrash.setLocation(x, labelTrash.getY());
            }

        }
    }
    private class ButtonPauseListener extends MouseAdapter {

        //Se crea un boton que cuando se hace clic en el se pausara el juego y cambiara el texto dependiendo si está
        //pausado en reanudado

        Timer timer, timerPaper;

        public ButtonPauseListener(Timer timer, Timer timerPaper) {
            this.timer = timer;
            this.timerPaper = timerPaper;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (buttonPause.getText().equals("Pausa")){
                timer.stop();
                timerPaper.stop();
                buttonPause.setText("Reanudar");
            } else{
                timer.start();
                timerPaper.start();
                buttonPause.setText("Pausa");
                panelMain.requestFocusInWindow();
            }
        }
    }

    private class TimerPaper implements ActionListener{
        // Timer que baja cada segundo la posición del papel 2Y y comprueba si el papel toca la papelera
        @Override
        public void actionPerformed(ActionEvent e) {
            labelPaper.setLocation(labelPaper.getX(), labelPaper.getY()+2);
            paperLlegaInferiror();
        }
    }
    private class TimerActionListener implements ActionListener{
        // Timer que actualiza el contador de segundos
        @Override
        public void actionPerformed(ActionEvent e) {
            seconds++;
            labelTime.setText(seconds +" segundos");
        }
    }

    private static class FrameWindowsListener extends WindowAdapter{
        //Se declara el frame de Windows a la hora de cerrar la pantalla para corroborar que queremos cerrar la aplicación

        JFrame frame;
        public FrameWindowsListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);

            int confirmado = JOptionPane.showConfirmDialog(null,
                    "¿Estas seguro de cerrar la ventana?",
                    "Mensaje" ,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmado == JOptionPane.YES_OPTION){
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            } else {
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }
        }
    }

    private void showPanelTitle() {
        // Se delcara el panel del Titulo (posición, tamamño y color), y se le aplica unos labels cojn un contador, un
        // boton de pausa, una puntuación y el número de vidas.
        panelTitle = new JPanel();
        panelTitle.setLocation(0,0);
        panelTitle.setSize( panelMain.getWidth(), 50);
        panelTitle.setBackground(Color.GRAY);
        panelMain.add(panelTitle);

        labelTime = new JLabel();
        labelTime.setText("0 Segundos");
        panelTitle.add(labelTime);

        buttonPause = new JButton();
        buttonPause.setText("Pausa");
        buttonPause.setFocusPainted(false);
        buttonPause.setBackground(new Color(25,18,50));
        buttonPause.setForeground(Color.white);

        panelTitle.add(buttonPause);

        labelPuntuacion = new JLabel();
        labelPuntuacion.setText("PUNTUACIÓN: "+puntuacion);
        panelTitle.add(labelPuntuacion);

        labelVidas = new JLabel();
        labelVidas.setText("VIDAS: " +vidas);
        panelTitle.add(labelVidas);
    }

    private void showPanelCenter() {
        //Se muestra el panel central donde iran la papelera y el papel, se declara el panel y se llaman a los metodos que
        // muestran a la papelera y el papel
        panelCenter = new JPanel();
        panelCenter.setLayout(null);
        panelCenter.setLocation(0, panelTitle.getHeight());
        panelCenter.setSize(panelMain.getWidth(), panelMain.getHeight() - panelTitle.getHeight());
        panelCenter.setBackground(Color.LIGHT_GRAY);
        panelMain.add(panelCenter);

        showTrash();
        showPaper();
    }

    private void showPaper(){
        //Crea el label del papeñ que aparece en una posición random del ancho del panel central en la altura 5
        labelPaper = new JLabel();
        labelPaper.setSize(30, 30);
        ImageIcon imageIcon = new ImageIcon("src/images/paper.png");
        Icon icon = new ImageIcon(
                imageIcon.getImage().getScaledInstance(labelPaper.getWidth(), labelPaper.getHeight(), Image.SCALE_DEFAULT)
        );
        labelPaper.setIcon(icon);
        labelPaper.setLocation(1+ (int)(Math.random() * panelCenter.getWidth()), 5);
        panelCenter.add(labelPaper);
    }
    private void showTrash() {
        //Se declara el label de la papelera
        labelTrash = new JLabel();
        labelTrash.setSize(90,100);
        ImageIcon imageIcon = new ImageIcon("src/images/trash.png");
        Icon icon = new ImageIcon(
                imageIcon.getImage().getScaledInstance(labelTrash.getWidth(), labelTrash.getHeight(), Image.SCALE_DEFAULT)
        );
        labelTrash.setIcon(icon);
        labelTrash.setLocation(panelCenter.getWidth()/2 - labelTrash.getWidth()/2, panelCenter.getHeight() - labelTrash.getHeight());
        panelCenter.add(labelTrash);

    }

    public static void main(String[] args) {
        //Se declara el frame main con la pantalla principal con el icono de la ventana

        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.setLocation(350, 100);
        frame.setLayout(null);

        Toolkit pantalla =Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/images/favicon-politecnics.png");
        frame.setIconImage(icono);

        frame.addWindowListener(new FrameWindowsListener(frame));
    }
}
