<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import vistas.vistaVentas;
import modelos.ModeloVenta;

/**
 *
 * @author jl393
 */
public class ControladorVenta implements MouseListener{

    private ModeloVenta venta;
    private vistaVentas vista;

    public ControladorVenta(ModeloVenta venta,
            vistaVentas vista) {
        this.venta = venta;
        this.vista = vista;

        JTextField txtBuscar = this.vista.busquedaProducto;
        JTable tblResultado = this.vista.tblProducto;
        
        this.vista.agregarProducto.addMouseListener(this);
        this.vista.eliminarProducto.addMouseListener(this);
        this.vista.lblCobrar.addMouseListener(this);

        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                venta.buscarProducto(txtBuscar, tblResultado);
            }
        });

        this.vista.tblProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble clic (opcional), o solo un clic
                if (e.getClickCount() >= 1) { // 1 clic es suficiente
                    venta.mostrarDetallesProducto(vista);
                }
            }
        });

        // Detectar cambios en la caja de cantidad
        this.vista.cantPrecio.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource().equals(this.vista.agregarProducto)){
            this.venta.agregarProductoAVenta(this.vista);
        }
        
        if(e.getSource().equals(this.vista.eliminarProducto)){
            this.venta.eliminarProductoDeVenta(this.vista);
        }
        
        if(e.getSource().equals(this.vista.lblCobrar)){
            this.venta.cobrar(vista);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import vistas.vistaVentas;
import modelos.ModeloVenta;

/**
 *
 * @author jl393
 */
public class ControladorVenta implements MouseListener{

    private ModeloVenta venta;
    private vistaVentas vista;

    public ControladorVenta(ModeloVenta venta,
            vistaVentas vista) {
        this.venta = venta;
        this.vista = vista;

        JTextField txtBuscar = this.vista.busquedaProducto;
        JTable tblResultado = this.vista.tblProducto;
        
        this.vista.agregarProducto.addMouseListener(this);
        this.vista.eliminarProducto.addMouseListener(this);
        this.vista.lblCobrar.addMouseListener(this);

        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                venta.buscarProducto(txtBuscar, tblResultado);
            }
        });

        this.vista.tblProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble clic (opcional), o solo un clic
                if (e.getClickCount() >= 1) { // 1 clic es suficiente
                    venta.mostrarDetallesProducto(vista);
                }
            }
        });

        // Detectar cambios en la caja de cantidad
        this.vista.cantPrecio.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                venta.calcularTotalParcial(vista);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource().equals(this.vista.agregarProducto)){
            this.venta.agregarProductoAVenta(this.vista);
        }
        
        if(e.getSource().equals(this.vista.eliminarProducto)){
            this.venta.eliminarProductoDeVenta(this.vista);
        }
        
        if(e.getSource().equals(this.vista.lblCobrar)){
            this.venta.cobrar(vista);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
