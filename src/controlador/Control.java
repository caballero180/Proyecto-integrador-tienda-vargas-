/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import modelos.*;
import vistas.*;

/**
 *
 * @author jl393
 */
public class Control implements MouseListener {

    //vistas
    private vistaPrincipal principal;
    private visatAltas altasvista;
    private vistaConsultas consulta;
    private vistaVentas vistaVentas;
    private InicioSesion inicio;
    //modelos
    private Modelo modelo;
    private ModeloCategoria categoria;
    private ModeloProducto producto;
    private ModeloProveedor proveedor;
    private ModeloUsuario usuario;
    private ModeloVenta ventas;

    public Control(vistaPrincipal principal,
            Modelo modelo,
            ModeloCategoria categoria,
            ModeloProducto producto,
            ModeloProveedor proveedor,
            ModeloUsuario usuario,
            visatAltas altavista,
            vistaVentas vistaVentas,
            ModeloVenta ventas,
            vistaConsultas consulta,
            InicioSesion inicio) {

        //vistas
        this.consulta = consulta;
        this.altasvista = altavista;
        this.vistaVentas = vistaVentas;
        this.principal = principal;
        this.inicio = inicio;
        //modelos
        this.modelo = modelo;
        this.categoria = categoria;
        this.consulta = consulta;
        this.producto = producto;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.ventas = ventas;
        //botones pantalla principal
        this.principal.lbAltas.addMouseListener(this);
        this.principal.lbConsultas.addMouseListener(this);
        this.principal.lbVentas.addMouseListener(this);
        this.principal.cerrarSesion.addMouseListener(this);
        this.principal.Ingresar.addMouseListener(this);
        this.inicio.Ingresar.addMouseListener(this);
        
        this.principal.panelLateral.setLayout(new BoxLayout(this.principal.panelLateral, BoxLayout.Y_AXIS));
        this.principal.panelLateral.setPreferredSize(new Dimension(120, 607));
        this.principal.panelLateral.setMinimumSize(new Dimension(120, 607));
        this.principal.panelLateral.setMaximumSize(new Dimension(120, 607));

        this.modelo.habilitarBoton(this.principal.cerrarSesion, false);
        this.modelo.habilitarBoton(this.principal.lbAltas, false);
        this.modelo.habilitarBoton(this.principal.lbConsultas, false);
        this.modelo.habilitarBoton(this.principal.lbVentas, false);

//        this.principal.panelLateral.setPreferredSize(new Dimension(120, 615));
//        this.principal.panelLateral.setMinimumSize(new Dimension(120, 615));
//        this.principal.panelLateral.setMaximumSize(new Dimension(120, 615));
        //vista de alta
        this.altasvista.proveedorProducto.addMouseListener(this);

        // En el constructor
        altasvista.categoriaProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Solo cargar si está vacío o si se desea refrescar
                if (altasvista.categoriaProducto.getItemCount() <= 1) {
                    modelo.CargarCategorias(altasvista);
                }
            }
        });

        this.altasvista.proveedorProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                modelo.CargarProveedores(altasvista);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //navegacion entre pantallas
        if (e.getSource() == this.principal.lbAltas) {
            this.modelo.Navegar(this.principal, this.altasvista);
        }

        if (e.getSource() == this.principal.lbConsultas) {
            this.modelo.Navegar(this.principal, this.consulta);
            this.categoria.Listado(consulta);
            this.producto.Listado(consulta);
            this.proveedor.Listado(consulta);
            this.usuario.Listado(consulta);

        }

        if (e.getSource().equals(this.principal.Ingresar)) {
            this.modelo.IniciarSesion(principal, this.vistaVentas);
        }
        if (e.getSource().equals(this.inicio.Ingresar)) {
            this.modelo.Inicio(inicio, vistaVentas);
        }

        if (e.getSource().equals(this.principal.cerrarSesion)) {
            this.modelo.CerrarSesion(principal, this.inicio);

        }

        if (e.getSource().equals(this.principal.lbVentas)) {
            this.modelo.Navegar(principal, vistaVentas);
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
