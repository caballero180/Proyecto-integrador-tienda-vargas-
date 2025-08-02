/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import vistas.visatAltas;
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ModeloProducto {

    private Connection conexion = null;
    private visatAltas altasvista;
    private vistaConsultas consultas;

    public void altaProducto(String nombre,
            int categoria,
            int proveedor,
            String descripcion,
            int stock,
            double compra,
            double precio) {
        Statement comando;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            comando = conexion.createStatement();
            ResultSet registro
                    = comando.executeQuery("select * from producto where nombre_producto='"
                            + nombre + "'");
            if (registro.next() == false) {

                String sql = "insert into producto (nombre_producto, "
                        + "id_categoria, "
                        + "id_proveedor, "
                        + "descripcion_producto,"
                        + "stock,"
                        + "compra,"
                        + "precio) "
                        + "values ('"
                        + nombre + "','"
                        + categoria + "','"
                        + proveedor + "','"
                        + descripcion + "','"
                        + stock + "','"
                        + compra + "','"
                        + precio + "')";
                System.out.println("" + sql);
                comando.executeUpdate("insert into producto(nombre_producto, "
                        + "id_categoria, "
                        + "id_proveedor, "
                        + "descripcion_producto,"
                        + "stock,"
                        + "compra,"
                        + "precio) "
                        + "values ('"
                        + nombre + "','"
                        + categoria + "','"
                        + proveedor + "','"
                        + descripcion + "','"
                        + stock + "','"
                        + compra + "','"
                        + precio + "')"
                );

            }
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getIdProveedorSeleccionado(visatAltas altasvista) {
        this.altasvista = altasvista;

        String nombreSeleccionado = (String) this.altasvista.proveedorProducto.getSelectedItem();
        if (nombreSeleccionado == null) {
            return -1; // No hay selección
        }

        String url = "jdbc:mysql://localhost:3306/tienda_vargas";
        String user = "root";
        String pass = "cisco1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(url, user, pass);

            var pstmt = conexion.prepareStatement(
                    "SELECT id_proveedor FROM proveedor WHERE empresa_proveedor = ?");
            pstmt.setString(1, nombreSeleccionado);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int idProveedor = rs.getInt("id_proveedor");
                rs.close();
                pstmt.close();
                conexion.close();
                return idProveedor;
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Retorna -1 si no se encontró el proveedor
    }

    public int getIdCategoriaSeleccionado(visatAltas altasvista) {
        this.altasvista = altasvista;
        String nombreSeleccionado = (String) this.altasvista.categoriaProducto.getSelectedItem();
        if (nombreSeleccionado == null) {
            return -1; // No hay selección
        }

        String url = "jdbc:mysql://localhost:3306/tienda_vargas";
        String user = "root";
        String pass = "cisco1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(url, user, pass);

            var pstmt = conexion.prepareStatement(
                    "SELECT id_cat FROM categoria WHERE cat = ?");
            pstmt.setString(1, nombreSeleccionado);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int idProveedor = rs.getInt("id_cat");
                rs.close();
                pstmt.close();
                conexion.close();
                return idProveedor;
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Retorna -1 si no se encontró el proveedor
    }

    public void Listado(vistaConsultas consultas) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_producto",
            "nombre",
            "categoria",
            "proveedor",
            "Descripcion",
            "Stock",
            "Compra",
            "Precio"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblConsulta.setModel(modeloTabla);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            comando = conexion.createStatement();

            ResultSet registro
                    = comando.executeQuery("select * from producto");
            modeloTabla.setRowCount(0);

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_producto");
                String nombre = registro.getString("nombre_producto");
                String categoria = registro.getString("id_categoria");
                String proveedor = registro.getString("id_proveedor");
                String descripcion = registro.getString("descripcion_producto");
                String stock = registro.getString("stock");
                String compra = registro.getString("compra");
                String precio = registro.getString("precio");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, categoria, proveedor, descripcion, stock, compra, precio});
                encontrado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ConsultaProducto(vistaConsultas consultas, String referencia) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_producto",
            "nombre",
            "categoria",
            "proveedor",
            "Descripcion",
            "Stock",
            "Compra",
            "Precio"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblConsulta.setModel(modeloTabla);

        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            comando = conexion.createStatement();

            // Ejecutar consulta
            ResultSet registro = comando.executeQuery(
                    "SELECT * FROM producto WHERE nombre_producto = '" + referencia + "'");

            // Preparar modelo de tabla
            modeloTabla.setRowCount(0); // Limpiar tabla antes de insertar nuevos datos
            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_producto");
                String nombre = registro.getString("nombre_producto");
                String categoria = registro.getString("id_categoria");
                String proveedor = registro.getString("id_proveedor");
                String descripcion = registro.getString("descripcion_producto");
                String stock = registro.getString("stock");
                String compra = registro.getString("compra");
                String precio = registro.getString("precio");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, categoria, proveedor, descripcion, stock, compra, precio});
                encontrado = true;
            }
            registro.close();
            comando.close();
            conexion.close();

        } catch (SQLException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
}
