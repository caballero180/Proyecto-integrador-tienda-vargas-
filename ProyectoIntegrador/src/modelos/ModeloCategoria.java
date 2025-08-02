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
public class ModeloCategoria {

    private vistaConsultas consultas;

    public void altaCategoria(String nombre) {
        Connection conexion = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            Statement comando;

            comando = conexion.createStatement();
            ResultSet registro
                    = comando.executeQuery("select * from categoria where cat='"
                            + nombre + "'");
            if (registro.next() == false) {

                String sql = "insert into categoria (cat) "
                        + "values ('"
                        + nombre + "')";

                System.out.println("" + sql);

                comando.executeUpdate("insert into categoria(cat)"
                        + "values ('"
                        + nombre + "')"
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

        String[] columnas = {"Id_categoria", "Categoria",};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblCat.setModel(modeloTabla);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            comando = conexion.createStatement();

            ResultSet registro
                    = comando.executeQuery("select * from categoria");
            modeloTabla.setRowCount(0);

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_cat");
                String categoria = registro.getString("cat");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, categoria});
                encontrado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Consultacat(vistaConsultas consultas, String referencia) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;
        
        String[] columnas = {"Id_categoria", "Categoria",};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblCat.setModel(modeloTabla);
        
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
                    "SELECT * FROM categoria WHERE cat = '" + referencia + "'");

            // Preparar modelo de tabla
            modeloTabla.setRowCount(0); // Limpiar tabla antes de insertar nuevos datos
            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_cat");
                String categoria = registro.getString("cat");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, categoria});
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
