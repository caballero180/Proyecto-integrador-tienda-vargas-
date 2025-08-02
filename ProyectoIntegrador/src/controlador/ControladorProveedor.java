/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modelos.ModeloProveedor;
import vistas.visatAltas;
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ControladorProveedor implements MouseListener{
    private ModeloProveedor proveedor;
    private visatAltas altasvista;
    private vistaConsultas consultas;
    
    public ControladorProveedor(visatAltas altasvista,vistaConsultas consultas,ModeloProveedor proveedor){
        
        this.proveedor = proveedor;
        this.altasvista = altasvista;
        this.consultas = consultas;
        
        this.altasvista.btnRProveedor.addMouseListener(this);
        this.consultas.listadoProveedor.addMouseListener(this);
        this.consultas.consultaProveedor.addMouseListener(this);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        //boton alta de proveedor
        if (e.getSource().equals(this.altasvista.btnRProveedor)){
            this.proveedor.altaProveedor(this.altasvista.empresaProveedor.getText().toUpperCase(),
                    this.altasvista.nombreProveedor.getText().toUpperCase(), 
                    this.altasvista.telefonoProveedor.getText(), 
                    this.altasvista.emailProveedor.getText());
        }
        
        if (e.getSource().equals(this.consultas.listadoProveedor)){
            this.proveedor.Listado(consultas);
        }
        
        if (e.getSource().equals(this.consultas.consultaProveedor)){
            this.proveedor.ConsultaProveedor(consultas, this.consultas.referenciaProveedor.getText().toUpperCase());
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
