<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.*;

/**
 *
 * @author jl393
 */
public class Diseño {

    public static final Color FONDO_PRINCIPAL = new Color(248, 249, 250); // #f8f9fa
    public static final Color FONDO_PANEL = new Color(255, 255, 255);     // #ffffff
    public static final Color COLOR_PRIMARIO = new Color(0, 123, 255);     // #007bff (azul)
    public static final Color COLOR_SECUNDARIO = new Color(40, 167, 69);   // #28a745 (verde)
    public static final Color COLOR_TERCIA = new Color(255, 193, 7);       // #ffc107 (amarillo)
    public static final Color COLOR_TEXTO = new Color(33, 37, 41);         // #212529
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(108, 117, 125); // #6c757d
    public static final Color COLOR_BORDE = new Color(206, 212, 218);      // #ced4da

    // FUENTES
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_PEQUEÑA = new Font("Segoe UI", Font.PLAIN, 12);
    //colores de los textfield
    public static final Color FONDO_CAMPO = new Color(240, 245, 255);     // Azul muy claro
    public static final Color BORDE_CAMPO = new Color(180, 200, 255);     // Azul claro
    public static final Color BORDE_FOCO = new Color(0, 123, 255);        // Azul primario
    public static final Color TEXTO_CAMPO = new Color(33, 37, 41);        // Negro suave

    public void aplicarEstiloPanel(JPanel panel) {
        panel.setBackground(FONDO_PANEL);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField field = (JTextField) comp;
                configurarCampoTexto(field);
            } else if (comp instanceof JPasswordField) {
                JPasswordField field = (JPasswordField) comp;
                configurarCampoTexto(field);
            } else if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                combo.setFont(FUENTE_NORMAL);
                combo.setForeground(TEXTO_CAMPO);
                combo.setBackground(FONDO_CAMPO);
                combo.setBorder(crearBorde(BORDE_CAMPO, 1, 5));
                combo.setOpaque(true);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setFont(FUENTE_NORMAL);
                label.setForeground(COLOR_TEXTO);
            }
        }
    }

    private static void configurarCampoTexto(JComponent field) {
        field.setFont(FUENTE_NORMAL);
        field.setForeground(TEXTO_CAMPO);
        field.setBackground(FONDO_CAMPO);
        field.setBorder(crearBorde(BORDE_CAMPO, 1, 5)); // Borde redondeado suave
        field.setOpaque(true);

        // === Efecto de foco (al hacer clic) ===
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(crearBorde(BORDE_FOCO, 2, 5)); // Borde más grueso y azul
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(crearBorde(BORDE_CAMPO, 1, 5)); // Vuelve al original
            }
        });
    }

    public static Border crearBorde(Color color, int grosor, int radio) {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(grosor));
                g2.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(8, 10, 8, 10); // Padding interno
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    public static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }

        @Override
        public boolean isBorderOpaque() {

            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(COLOR_BORDE);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    public static Border bordeSave() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(5),
                BorderFactory.createCompoundBorder(
                        new MatteBorder(1, 1, 2, 1, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8))
        );

    }

    

    public void aplicarEstiloBoton(javax.swing.JLabel label, String texto) {
        // Establecer texto con subrayado usando HTML
        label.setText("<html><u>" + texto + "</u></html>");
        label.setFont(FUENTE_SUBTITULO);
        label.setForeground(COLOR_PRIMARIO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Tamaño fijo
        label.setPreferredSize(new Dimension(146, 136));
        label.setMinimumSize(new Dimension(146, 136));
        label.setMaximumSize(new Dimension(146, 136));

        // 🔹 Habilitar fondo y establecer color original
        label.setOpaque(true);
        Color FONDO_ORIGINAL = new Color(240, 245, 255); // Azul muy claro
        Color FONDO_HOVER = new Color(0, 123, 255);      // Azul brillante
        Color BORDE_HOVER = new Color(0, 86, 179);
        label.setBackground(FONDO_ORIGINAL);

        // 🔹 Aplicar borde con espacio interno (opcional, para mejor aspecto)
        Border margen = BorderFactory.createEmptyBorder(10, 10, 10, 10); // padding
        label.setBorder(margen);

        // Efecto hover
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(Color.WHITE);           // Texto blanco al hacer hover
                label.setBackground(FONDO_HOVER);           // Fondo azul brillante
                // Borde más definido alrededor
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE_HOVER, 2, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(COLOR_PRIMARIO);        // Vuelve al color original del texto
                label.setBackground(FONDO_ORIGINAL);        // Vuelve al fondo original
                label.setBorder(margen);                    // Vuelve al borde original (sin línea)
            }
        });
    }

}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.*;

/**
 *
 * @author jl393
 */
public class Diseño {

    public static final Color FONDO_PRINCIPAL = new Color(248, 249, 250); // #f8f9fa
    public static final Color FONDO_PANEL = new Color(255, 255, 255);     // #ffffff
    public static final Color COLOR_PRIMARIO = new Color(0, 123, 255);     // #007bff (azul)
    public static final Color COLOR_SECUNDARIO = new Color(40, 167, 69);   // #28a745 (verde)
    public static final Color COLOR_TERCIA = new Color(255, 193, 7);       // #ffc107 (amarillo)
    public static final Color COLOR_TEXTO = new Color(33, 37, 41);         // #212529
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(108, 117, 125); // #6c757d
    public static final Color COLOR_BORDE = new Color(206, 212, 218);      // #ced4da

    // FUENTES
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_PEQUEÑA = new Font("Segoe UI", Font.PLAIN, 12);
    //colores de los textfield
    public static final Color FONDO_CAMPO = new Color(240, 245, 255);     // Azul muy claro
    public static final Color BORDE_CAMPO = new Color(180, 200, 255);     // Azul claro
    public static final Color BORDE_FOCO = new Color(0, 123, 255);        // Azul primario
    public static final Color TEXTO_CAMPO = new Color(33, 37, 41);        // Negro suave

    public void aplicarEstiloPanel(JPanel panel) {
        panel.setBackground(FONDO_PANEL);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField field = (JTextField) comp;
                configurarCampoTexto(field);
            } else if (comp instanceof JPasswordField) {
                JPasswordField field = (JPasswordField) comp;
                configurarCampoTexto(field);
            } else if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                combo.setFont(FUENTE_NORMAL);
                combo.setForeground(TEXTO_CAMPO);
                combo.setBackground(FONDO_CAMPO);
                combo.setBorder(crearBorde(BORDE_CAMPO, 1, 5));
                combo.setOpaque(true);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setFont(FUENTE_NORMAL);
                label.setForeground(COLOR_TEXTO);
            }
        }
    }

    private static void configurarCampoTexto(JComponent field) {
        field.setFont(FUENTE_NORMAL);
        field.setForeground(TEXTO_CAMPO);
        field.setBackground(FONDO_CAMPO);
        field.setBorder(crearBorde(BORDE_CAMPO, 1, 5)); // Borde redondeado suave
        field.setOpaque(true);

        // === Efecto de foco (al hacer clic) ===
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(crearBorde(BORDE_FOCO, 2, 5)); // Borde más grueso y azul
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(crearBorde(BORDE_CAMPO, 1, 5)); // Vuelve al original
            }
        });
    }

    public static Border crearBorde(Color color, int grosor, int radio) {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(grosor));
                g2.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(8, 10, 8, 10); // Padding interno
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    public static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }

        @Override
        public boolean isBorderOpaque() {

            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(COLOR_BORDE);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    public static Border bordeSave() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(5),
                BorderFactory.createCompoundBorder(
                        new MatteBorder(1, 1, 2, 1, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8))
        );

    }

    

    public void aplicarEstiloBoton(javax.swing.JLabel label, String texto) {
        // Establecer texto con subrayado usando HTML
        label.setText("<html><u>" + texto + "</u></html>");
        label.setFont(FUENTE_SUBTITULO);
        label.setForeground(COLOR_PRIMARIO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Tamaño fijo
        label.setPreferredSize(new Dimension(146, 136));
        label.setMinimumSize(new Dimension(146, 136));
        label.setMaximumSize(new Dimension(146, 136));

        // 🔹 Habilitar fondo y establecer color original
        label.setOpaque(true);
        Color FONDO_ORIGINAL = new Color(240, 245, 255); // Azul muy claro
        Color FONDO_HOVER = new Color(0, 123, 255);      // Azul brillante
        Color BORDE_HOVER = new Color(0, 86, 179);
        label.setBackground(FONDO_ORIGINAL);

        // 🔹 Aplicar borde con espacio interno (opcional, para mejor aspecto)
        Border margen = BorderFactory.createEmptyBorder(10, 10, 10, 10); // padding
        label.setBorder(margen);

        // Efecto hover
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(Color.WHITE);           // Texto blanco al hacer hover
                label.setBackground(FONDO_HOVER);           // Fondo azul brillante
                // Borde más definido alrededor
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE_HOVER, 2, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(COLOR_PRIMARIO);        // Vuelve al color original del texto
                label.setBackground(FONDO_ORIGINAL);        // Vuelve al fondo original
                label.setBorder(margen);                    // Vuelve al borde original (sin línea)
            }
        });
    }

}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
