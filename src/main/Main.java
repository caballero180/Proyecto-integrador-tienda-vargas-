/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import controlador.*;
import vistas.*;
import modelos.*;

/**
 *
 * @author jl393
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // vistas
        vistaPrincipal principal = new vistaPrincipal();
        visatAltas alta = new visatAltas();
        vistaConsultas consulta = new vistaConsultas();
        vistaVentas vistaVentas = new vistaVentas();
        InicioSesion inicio = new InicioSesion();
        //modelos 
        Modelo modelo = new Modelo();
        ModeloCategoria modeloCategoria = new ModeloCategoria();
        ModeloProveedor modeloProveedor = new ModeloProveedor();
        ModeloUsuario modeloUsuario = new ModeloUsuario();
        ModeloProducto modeloproducto = new ModeloProducto();
        ModeloVenta modeloventa = new ModeloVenta(); 
        Diseño  modeloDiseño = new Diseño();
        
        Control control = new Control(principal,
                modelo,
                modeloCategoria,
                modeloproducto,
                modeloProveedor,
                modeloUsuario, 
                alta,
                vistaVentas,
                modeloventa,
                consulta,
                inicio);
        
        ControladorCategoria  categorias = new ControladorCategoria(alta,
                consulta,
                modeloCategoria);
        
        ControladorProveedor proveedores = new ControladorProveedor(alta,
                consulta,
                modeloProveedor);
        
        ControladorUsuario usuarios = new ControladorUsuario(alta,
                consulta,
                modeloUsuario);
        
        
        ControladorProducto productos = new ControladorProducto(alta,
                consulta,
                modeloproducto); 
        
        ControladorVenta venta = new ControladorVenta(modeloventa,
                vistaVentas); 
        
        ControlDiseño diseño = new ControlDiseño(modeloDiseño,
                alta,
                consulta,
                vistaVentas,
                inicio,
                principal);
        
        principal.setVisible(true);
        
        
    }
    
}
