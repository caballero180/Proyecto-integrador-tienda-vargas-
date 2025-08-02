package modelos;


import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import vistas.visatAltas;
import vistas.vistaPrincipal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jl393
 */
public class Modelo {

    vistaPrincipal principal;
    JPanel panel;
    Connection conexion = null;
    visatAltas altasvista;

    public void Navegar(vistaPrincipal principal, JPanel panel) {
        this.principal = principal;
        this.panel = panel;
        this.principal.Contenedor.removeAll();
        this.panel.setSize(this.principal.Contenedor.getWidth(), this.principal.Contenedor.getHeight());
        this.panel.setLocation(0, 0);
        this.panel.setVisible(true);
        this.principal.Contenedor.add(this.panel, BorderLayout.CENTER);
        this.principal.Contenedor.revalidate();
        this.principal.Contenedor.repaint();

    }

    public void CargarCategorias(visatAltas altasvista) {

        String url = "jdbc:mysql://localhost:3306/tienda_vargas"; // Cambia por tu BD
        String usuario = "root";
        String contrasena = "cisco1234";
        this.altasvista = altasvista;
        this.altasvista.categoriaProducto.removeAllItems();

        try {
            // Cargar driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer conexión
            Connection conexion = DriverManager.getConnection(url, usuario, contrasena);

            // Consulta SQL
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cat FROM categoria");
            // Agregar los resultados al JComboBox
            while (rs.next()) {
                String categoria = rs.getString("cat");
                this.altasvista.categoriaProducto.addItem(categoria);
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            conexion.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void CargarProveedores(visatAltas altasvista) {

        String url = "jdbc:mysql://localhost:3306/tienda_vargas"; // Cambia por tu BD
        String usuario = "root";
        String contrasena = "cisco1234";
        this.altasvista = altasvista;
        this.altasvista.proveedorProducto.removeAllItems();

        try {
            // Cargar driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer conexión
            Connection conexion = DriverManager.getConnection(url, usuario, contrasena);

            // Consulta SQL
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT empresa_proveedor FROM proveedor");
            // Agregar los resultados al JComboBox
            while (rs.next()) {
                String categoria = rs.getString("empresa_proveedor");
                this.altasvista.proveedorProducto.addItem(categoria);
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            conexion.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}

