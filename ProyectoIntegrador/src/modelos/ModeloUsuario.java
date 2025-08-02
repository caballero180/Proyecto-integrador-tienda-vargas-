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
public class ModeloUsuario {
    
    private Connection conexion = null;
    private vistaConsultas consultas;
    
    public void altaUsuario(String nombre, 
            String materno, 
            String paterno,
            String usuario,
            String contraseña,
            String telefono){
        Statement comando;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            
            comando = conexion.createStatement();
            ResultSet registro
                    = comando.executeQuery("select * from empleado where usuario_empleado='"
                            + usuario + "'");
            if (registro.next() == false) {

                String sql = "insert into empleado (nombre_empleado, "
                        + "materno_empleado, "
                        + "paterno_empleado, "
                        + "usuario_empleado,"
                        + " contraseña_empleado,"
                        + " telefono_empleado) "
                        + "values ('"
                        + nombre + "','"
                        + materno + "','"
                        + paterno + "','"
                        + usuario + "','"
                        + contraseña + "','" 
                        + telefono +"')";
                System.out.println("" + sql);
                comando.executeUpdate("insert into empleado(nombre_empleado, materno_empleado, paterno_empleado, usuario_empleado, contraseña_empleado, telefono_empleado) values ('"
                        + nombre + "','"
                        + materno + "','"
                        + paterno + "','"
                        + usuario + "','"
                        + contraseña+ "','" 
                        + telefono +"')"
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
        this.consultas.tblUsuario.setModel(modeloTabla);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            comando = conexion.createStatement();

            ResultSet registro
                    = comando.executeQuery("select * from empleado");
            modeloTabla.setRowCount(0);

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_empleado");
                String nombre = registro.getString("nombre_empleado");
                String paterno = registro.getString("paterno_empleado");
                String materno = registro.getString("materno_empleado");
                String telefono = registro.getString("telefono_empleado");
                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, telefono});
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
        
        String[] columnas = {"Id_proveedor",
            "Empresa_proveedor",
            "Nombre_proveedor",
            "Telefono_proveedor",
            "Email_proveedor"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        this.consultas.tblUsuario.setModel(modeloTabla);
        
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
                    "SELECT * FROM empleado WHERE nombre_empleado = '" + referencia + "'");

            // Preparar modelo de tabla
            modeloTabla.setRowCount(0); // Limpiar tabla antes de insertar nuevos datos
            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_empleado");
                String nombre = registro.getString("nombre_empleado");
                String paterno = registro.getString("paterno_empleado");
                String materno = registro.getString("materno_empleado");
                String telefono = registro.getString("telefono_empleado");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, telefono});
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
    
    public void CambiosUsuario(String nombre, String paterno, String materno ,String telefono){
        
        Statement comando;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");
            comando = conexion.createStatement();
    
            comando.executeUpdate("update tcliente set nombre_empleado='"
                    + nombre + "', paterno_empleado='"
                    + paterno + "', materno_empleado='"
                    + materno + "', telefono_empleado='"
                    + telefono + "' where nombre_empleado='"
                    + nombre +"'");
            conexion.close();

        }
        catch (SQLException ex) {
            Logger.getLogger(ModeloUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModeloUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
