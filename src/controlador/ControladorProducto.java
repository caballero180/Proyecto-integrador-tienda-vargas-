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
import modelos.ModeloProducto;
import vistas.visatAltas;
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ControladorProducto implements MouseListener {

    private visatAltas altasvista;
    private vistaConsultas consultas;
    private ModeloProducto producto;

    public ControladorProducto(visatAltas altasvista,
            vistaConsultas consultas,
            ModeloProducto producto) {

        this.altasvista = altasvista;
        this.producto = producto;
        this.consultas = consultas;

        this.altasvista.btnRProducto.addMouseListener(this);
        this.consultas.listadoProducto.addMouseListener(this);
        this.consultas.cambiosProductos.addMouseListener(this);
        
        this.consultas.referenciaProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = consultas.referenciaProducto.getText().trim();
                producto.ConsultaProducto(consultas, texto);
            }
        });

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource().equals(this.altasvista.btnRProducto)) {

            int idCat = this.producto.getIdCategoriaSeleccionado(altasvista);
            int idProv = this.producto.getIdProveedorSeleccionado(altasvista);

            String stock = this.altasvista.txtStock.getText(),
                    compra = this.altasvista.compraProducto.getText(),
                    venta = this.altasvista.ventaProducto.getText();

            int stockn = Integer.parseInt(stock);
            double compran = Integer.parseInt(compra);
            double ventan = Integer.parseInt(venta);

            this.producto.altaProducto(this.altasvista.nombreProducto.getText().toUpperCase(),
                    idCat,
                    idProv,
                    this.altasvista.descripcionProducto.getText().toUpperCase(),
                    stockn,
                    compran,
                    ventan);
        }

        if (e.getSource().equals(this.consultas.listadoProducto)) {
            this.producto.Listado(consultas);
        }
        
        

        

        if (e.getSource().equals(this.consultas.cambiosProductos)) {
            this.producto.GuardarCambios(consultas);
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
