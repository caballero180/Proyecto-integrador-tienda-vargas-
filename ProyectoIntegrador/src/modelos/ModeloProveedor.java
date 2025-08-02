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
import vistas.vistaConsultas;

/**
 *
 * @author jl393
 */
public class ModeloProveedor {

    private vistaConsultas consultas;

    public void altaProveedor(String empresa, String nombre, String telefono, String mail) {
        Connection conexion = null;
        Statement comando;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            comando = conexion.createStatement();
            ResultSet registro
                    = comando.executeQuery("select * from proveedor where empresa_proveedor='"
                            + empresa + "'");
            if (registro.next() == false) {

                String sql = "insert into proveedor (empresa_proveedor, "
                        + "nombre_proveedor, "
                        + "telefono_proveedor, "
                        + "email_proveedor)"
                        + "values ('"
                        + empresa + "','"
                        + nombre + "','"
                        + telefono + "','"
                        + mail + "')";
                System.out.println("" + sql);
                comando.executeUpdate("insert into proveedor (empresa_proveedor, "
                        + "nombre_proveedor, "
                        + "telefono_proveedor, "
                        + "email_proveedor)"
                        + "values ('"
                        + empresa + "','"
                        + nombre + "','"
                        + telefono + "','"
                        + mail + "')"
                );

            }
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Listado(vistaConsultas consultas) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_proveedor",
            "Empresa_proveedor",
            "Nombre_proveedor",
            "Telefono_proveedor",
            "Email_proveedor"};

        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblProveedor.setModel(modeloTabla);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            comando = conexion.createStatement();

            ResultSet registro
                    = comando.executeQuery("select * from proveedor");
            modeloTabla.setRowCount(0);

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_proveedor");
                String empresa = registro.getString("empresa_proveedor");
                String nombre = registro.getString("nombre_proveedor");
                String telefono = registro.getString("telefono_proveedor");
                String email = registro.getString("email_proveedor");
                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, empresa, nombre, telefono, email});
                encontrado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ConsultaProveedor(vistaConsultas consultas, String referencia) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_proveedor",
            "Empresa_proveedor",
            "Nombre_proveedor",
            "Telefono_proveedor",
            "Email_proveedor"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblProveedor.setModel(modeloTabla);

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
                    "SELECT * FROM proveedor WHERE empresa_proveedor = '" + referencia + "'");

            // Preparar modelo de tabla
            modeloTabla.setRowCount(0); // Limpiar tabla antes de insertar nuevos datos
            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_proveedor");
                String empresa = registro.getString("empresa_proveedor");
                String nombre = registro.getString("nombre_proveedor");
                String telefono = registro.getString("telefono_proveedor");
                String email = registro.getString("email_proveedor");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, empresa, nombre, telefono, email});
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
