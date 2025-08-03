/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import vistas.*;
import modelos.Diseño;

/**
 *
 * @author jl393
 */
public class ControlDiseño {

    private visatAltas altas;
    private vistaPrincipal principal;
    private Diseño diseño;
    private vistaConsultas consultas;
    private vistaVentas ventas;
    private InicioSesion inicio;

    public ControlDiseño(Diseño diseño,
            visatAltas altas,
            vistaConsultas consultas,
            vistaVentas ventas,
            InicioSesion incio,
            vistaPrincipal principal) {
        
        this.altas = altas;
        this.diseño = diseño;
        this.principal = principal;
        this.consultas = consultas;
        this.ventas = ventas;
        this.inicio = incio;

        //diseño de la pestaña de altas
        this.altas.setBackground(diseño.FONDO_PRINCIPAL);
        //estilo de paneles
        this.diseño.aplicarEstiloPanel(this.altas.panelCategoria);
        this.diseño.aplicarEstiloPanel(this.altas.panelProducto);
        this.diseño.aplicarEstiloPanel(this.altas.panelProveedor);
        this.diseño.aplicarEstiloPanel(this.altas.altasUsuario);
        
        //estilos de botones de guardado
        this.diseño.aplicarEstiloBoton(this.altas.btnRUsuario, "Registrar Usuario");
        this.diseño.aplicarEstiloBoton(this.altas.btnRProducto, "Registrar Producto");
        this.diseño.aplicarEstiloBoton(this.altas.btnRCategoria, "Registrar Categoría");
        this.diseño.aplicarEstiloBoton(this.altas.btnRProveedor, "Registrar Proveedor");
        
        
        //diseño de la vista consultas
        this.principal.setBackground(diseño.FONDO_PRINCIPAL);
        //estilo paneles
        this.diseño.aplicarEstiloPanel(this.principal.panelLateral);
        this.diseño.aplicarEstiloPanel(this.principal.Contenedor);
        //estilo botones
        this.diseño.aplicarEstiloBoton(this.principal.lbAltas, "Altas");
        this.diseño.aplicarEstiloBoton(this.principal.lbConsultas, "C.C");
        this.diseño.aplicarEstiloBoton(this.principal.lbVentas, "Ventas");
        this.diseño.aplicarEstiloBoton(this.principal.Ingresar, "Ingresar");
        this.diseño.aplicarEstiloBoton(this.principal.cerrarSesion, "Cerrar sesion");
        
        //diseño de consultas/cambios
        this.consultas.setBackground(diseño.FONDO_PRINCIPAL);
        //diseño de paneles
        this.diseño.aplicarEstiloPanel(this.consultas.CCat);
        this.diseño.aplicarEstiloPanel(this.consultas.CProductos);
        this.diseño.aplicarEstiloPanel(this.consultas.CProveedor);
        this.diseño.aplicarEstiloPanel(this.consultas.CUsusarios);
        //botones de cambios
        this.diseño.aplicarEstiloBoton(this.consultas.cambiosCategoria, "Registrar cambios");
        this.diseño.aplicarEstiloBoton(this.consultas.cambiosProductos, "Registrar cambios");
        this.diseño.aplicarEstiloBoton(this.consultas.cambiosProveedor, "Registrar cambios");
        this.diseño.aplicarEstiloBoton(this.consultas.cambiosUsuarios, "Registrar cambios");
        //botones de consultas
        this.diseño.aplicarEstiloBoton(this.consultas.listadoCat, "Listado de categorias");
        this.diseño.aplicarEstiloBoton(this.consultas.listadoProducto, "Listado de productos");
        this.diseño.aplicarEstiloBoton(this.consultas.listadoProveedor, "Listado de proveedores");
        this.diseño.aplicarEstiloBoton(this.consultas.listadoUsuario, "Listado de usuarios");
        
        
        //diseño del las ventas
        this.diseño.aplicarEstiloPanel(this.ventas.ventasPanel);
        this.diseño.aplicarEstiloBoton(this.ventas.agregarProducto, "Agregar producto");
        this.diseño.aplicarEstiloBoton(this.ventas.eliminarProducto, "Eliminar producto");
        this.diseño.aplicarEstiloBoton(this.ventas.lblCobrar, "Cobrar");
        
        //inicio de sesion
        this.inicio.setBackground(diseño.FONDO_PRINCIPAL);
        this.diseño.aplicarEstiloBoton(this.inicio.Ingresar, "Ingresar");
        this.diseño.aplicarEstiloPanel(this.inicio.Contenedor);

    }

}
