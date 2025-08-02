/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import vistas.vistaCambios;
import vistas.vistaConsultas;
import vistas.vistaPrincipal;
import vistas.visatAltas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modelos.*;


/**
 *
 * @author jl393
 */
public class Control implements MouseListener{
    //vistas
    private vistaPrincipal principal;
    private visatAltas altasvista;
    private vistaCambios cambios;
    private vistaConsultas consulta;
    //modelos
    private Modelo modelo;
    
    
    
    
    
    public Control(vistaPrincipal principal,
            Modelo modelo,
            visatAltas altavista,
            vistaConsultas consulta,
            vistaCambios cambios){
        
        //vistas
        this.consulta = consulta;
        this.altasvista = altavista;
        this.cambios = cambios;
        this.principal = principal;
        
        //modelos
        this.modelo = modelo;
        
        
        //botones pantalla principal
        this.principal.lbAltas.addMouseListener(this);
        this.principal.lbConsultas.addMouseListener(this);
        this.principal.lbCambios.addMouseListener(this);
        
        this.altasvista.categoriaProducto.addMouseListener(this);
        this.altasvista.proveedorProducto.addMouseListener(this);
    
        
        
    }

    

    @Override
    public void mouseClicked(MouseEvent e) {
        
        //navegacion entre pantallas
        if (e.getSource() == this.principal.lbAltas) {
            this.modelo.Navegar(this.principal, this.altasvista);
        }
        
        if (e.getSource() == this.principal.lbConsultas) {
            this.modelo.Navegar(this.principal, this.consulta);
        }
        if  (e.getSource().equals(this.principal.lbCambios)){
            this.modelo.Navegar(this.principal,this.cambios );
        }   
        
        if (e.getSource().equals(this.altasvista.categoriaProducto)){
            this.modelo.CargarCategorias(altasvista);
        }
        
        if (e.getSource().equals(this.altasvista.proveedorProducto)){
            this.modelo.CargarProveedores(altasvista);
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
