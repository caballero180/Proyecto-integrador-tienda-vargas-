/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modelos.ModeloUsuario;
import modelos.Modelo;
import vistas.*;

/**
 *
 * @author jl393
 */
public class ControladorUsuario implements MouseListener {

    private ModeloUsuario Usuario;
    private visatAltas altasvista;
    private vistaConsultas consultas;

    public ControladorUsuario(visatAltas altasvista,
            vistaConsultas consultas,
            ModeloUsuario Usuario) {

        this.Usuario = Usuario;
        this.altasvista = altasvista;
        this.consultas = consultas;

        this.altasvista.btnRUsuario.addMouseListener(this);
        this.consultas.listadoUsuario.addMouseListener(this);
        this.consultas.cambiosUsuarios.addMouseListener(this);
        
        this.consultas.referenciaUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaUsuario.getText().trim();
                Usuario.Consultacat(consultas, texto);
            }
        });

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //boton alta de usuario
        if (e.getSource().equals(this.altasvista.btnRUsuario)) {

            this.Usuario.altaUsuario(this.altasvista.nombreUsuario.getText().toUpperCase(),
                    this.altasvista.maternoUsuario.getText().toUpperCase(),
                    this.altasvista.paternoUsuario.getText().toUpperCase(),
                    this.altasvista.usuarioUser.getText().toUpperCase(),
                    this.altasvista.contraseñaUsuario.getText(),
                    this.altasvista.telefonoUsuario.getText().toUpperCase());
        }

        if (e.getSource().equals(this.consultas.listadoUsuario)) {
            this.Usuario.Listado(consultas);
        }
        
        if (e.getSource().equals(this.consultas.cambiosUsuarios)) {
            this.Usuario.GuardarCambiosEmpleados(consultas);
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
