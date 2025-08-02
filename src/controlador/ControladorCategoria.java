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
import modelos.ModeloCategoria;
import vistas.*;

/**
 *
 * @author jl393
 */
public class ControladorCategoria implements MouseListener {

    private visatAltas altasvista;

    private vistaConsultas consultavista;
    private ModeloCategoria Categoria;

    public ControladorCategoria(visatAltas altasvista,
            vistaConsultas consultas,
            ModeloCategoria Categoria) {

        this.altasvista = altasvista;
        this.Categoria = Categoria;
        this.consultavista = consultas;

        this.altasvista.btnRCategoria.addMouseListener(this);
        this.consultavista.listadoCat.addMouseListener(this);
        this.consultavista.cambiosCategoria.addMouseListener(this);

        // Supón que tienes un JTextField llamado txtBuscarCategoria
        this.consultavista.referenciaCat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaCat.getText().trim();
                Categoria.Consultacat(consultas, texto);
            }
        });

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //boton registro de categoria
        if (e.getSource().equals(this.altasvista.btnRCategoria)) {
            this.Categoria.altaCategoria(this.altasvista.nombreCategoria.getText().toUpperCase());
        }

        if (e.getSource().equals(this.consultavista.listadoCat)) {
            this.Categoria.Listado(consultavista);
        }

        if (e.getSource().equals(this.consultavista.cambiosCategoria)) {
            this.Categoria.GuardarCambiosCategorias(consultavista);
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
import modelos.ModeloCategoria;
import vistas.*;

/**
 *
 * @author jl393
 */
public class ControladorCategoria implements MouseListener {

    private visatAltas altasvista;

    private vistaConsultas consultavista;
    private ModeloCategoria Categoria;

    public ControladorCategoria(visatAltas altasvista,
            vistaConsultas consultas,
            ModeloCategoria Categoria) {

        this.altasvista = altasvista;
        this.Categoria = Categoria;
        this.consultavista = consultas;

        this.altasvista.btnRCategoria.addMouseListener(this);
        this.consultavista.listadoCat.addMouseListener(this);
        this.consultavista.cambiosCategoria.addMouseListener(this);

        // Supón que tienes un JTextField llamado txtBuscarCategoria
        this.consultavista.referenciaCat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaCat.getText().trim();
                Categoria.Consultacat(consultas, texto);
            }
        });

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //boton registro de categoria
        if (e.getSource().equals(this.altasvista.btnRCategoria)) {
            this.Categoria.altaCategoria(this.altasvista.nombreCategoria.getText().toUpperCase());
        }

        if (e.getSource().equals(this.consultavista.listadoCat)) {
            this.Categoria.Listado(consultavista);
        }

        if (e.getSource().equals(this.consultavista.cambiosCategoria)) {
            this.Categoria.GuardarCambiosCategorias(consultavista);
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
