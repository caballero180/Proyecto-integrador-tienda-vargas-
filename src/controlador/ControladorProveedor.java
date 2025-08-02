<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modelos.ModeloProveedor;
import vistas.visatAltas;
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ControladorProveedor implements MouseListener {

    private ModeloProveedor proveedor;
    private visatAltas altasvista;
    private vistaConsultas consultas;

    public ControladorProveedor(visatAltas altasvista, vistaConsultas consultas, ModeloProveedor proveedor) {

        this.proveedor = proveedor;
        this.altasvista = altasvista;
        this.consultas = consultas;

        this.altasvista.btnRProveedor.addMouseListener(this);
        this.consultas.listadoProveedor.addMouseListener(this);
        this.consultas.cambiosProveedor.addMouseListener(this);

        // Supón que tienes: JTextField txtBuscarProveedor
        this.consultas.referenciaProveedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaProveedor.getText().trim();
                proveedor.ConsultaProveedor(consultas, texto);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //boton alta de proveedor
        if (e.getSource().equals(this.altasvista.btnRProveedor)) {
            this.proveedor.altaProveedor(this.altasvista.empresaProveedor.getText().toUpperCase(),
                    this.altasvista.nombreProveedor.getText().toUpperCase(),
                    this.altasvista.telefonoProveedor.getText(),
                    this.altasvista.emailProveedor.getText());
        }

        if (e.getSource().equals(this.consultas.listadoProveedor)) {
            this.proveedor.Listado(consultas);
        }

        

        if (e.getSource().equals(this.consultas.cambiosProveedor)) {
            this.proveedor.GuardarCambiosProveedores(consultas);
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modelos.ModeloProveedor;
import vistas.visatAltas;
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ControladorProveedor implements MouseListener {

    private ModeloProveedor proveedor;
    private visatAltas altasvista;
    private vistaConsultas consultas;

    public ControladorProveedor(visatAltas altasvista, vistaConsultas consultas, ModeloProveedor proveedor) {

        this.proveedor = proveedor;
        this.altasvista = altasvista;
        this.consultas = consultas;

        this.altasvista.btnRProveedor.addMouseListener(this);
        this.consultas.listadoProveedor.addMouseListener(this);
        this.consultas.cambiosProveedor.addMouseListener(this);

        // Supón que tienes: JTextField txtBuscarProveedor
        this.consultas.referenciaProveedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaProveedor.getText().trim();
                proveedor.ConsultaProveedor(consultas, texto);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //boton alta de proveedor
        if (e.getSource().equals(this.altasvista.btnRProveedor)) {
            this.proveedor.altaProveedor(this.altasvista.empresaProveedor.getText().toUpperCase(),
                    this.altasvista.nombreProveedor.getText().toUpperCase(),
                    this.altasvista.telefonoProveedor.getText(),
                    this.altasvista.emailProveedor.getText());
        }

        if (e.getSource().equals(this.consultas.listadoProveedor)) {
            this.proveedor.Listado(consultas);
        }

        

        if (e.getSource().equals(this.consultas.cambiosProveedor)) {
            this.proveedor.GuardarCambiosProveedores(consultas);
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
