<<<<<<< HEAD
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vistas.vistaConsultas;
import java.sql.PreparedStatement;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.logging.Level;

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
            String telefono) {

        // === VALIDACIONES ===
        // Verificar que ningún campo esté vacío
        if (nombre.trim().isEmpty()
                || paterno.trim().isEmpty()
                || usuario.trim().isEmpty()
                || contraseña.trim().isEmpty()
                || telefono.trim().isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que el teléfono tenga exactamente 10 dígitos y solo números
        if (!telefono.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null,
                    "El teléfono debe contener exactamente 10 dígitos numéricos.",
                    "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conexion = null;
        PreparedStatement consulta = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            // Verificar si el usuario ya existe
            String sqlConsulta = "SELECT * FROM empleado WHERE usuario_empleado = ?";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, usuario.trim());
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "El nombre de usuario ya está en uso.",
                        "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Insertar nuevo usuario (contraseña en texto plano)
            String sqlInsert = "INSERT INTO empleado "
                    + "(nombre_empleado, materno_empleado, paterno_empleado, "
                    + "usuario_empleado, contraseña_empleado, telefono_empleado) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, nombre.trim());
            comando.setString(2, materno.trim());
            comando.setString(3, paterno.trim());
            comando.setString(4, usuario.trim());
            comando.setString(5, contraseña); 
            comando.setString(6, telefono);

            int filas = comando.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Usuario registrado correctamente.\nUsuario: " + usuario,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error en la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos en orden inverso
            try {
                if (registro != null) {
                    registro.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (consulta != null) {
                    consulta.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (comando != null) {
                    comando.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Listado(vistaConsultas consultas) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_empleado",
            "Nombre_empleado",
            "Paterno_empleado",
            "Materno_empleado",
            "Usuario_empleado",
            "Telefono_empleado"};

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
                String usuario = registro.getString("usuario_empleado");
                String telefono = registro.getString("telefono_empleado");
                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, usuario, telefono});
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

    Connection conexion = null;
    PreparedStatement comando = null;
    ResultSet registro = null;

    String[] columnas = {
        "Id_empleado",
        "Nombre_empleado",
        "Paterno_empleado",
        "Materno_empleado",
        "Usuario_empleado",
        "Telefono_empleado"
    };

    DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    this.consultas.tblUsuario.setModel(modeloTabla);

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_vargas",
                "root",
                "cisco1234");

        // Usar LIKE para búsqueda parcial
        String sql = "SELECT id_empleado, nombre_empleado, paterno_empleado, " +
                     "materno_empleado, usuario_empleado, telefono_empleado " +
                     "FROM empleado " +
                     "WHERE nombre_empleado LIKE ?";

        comando = conexion.prepareStatement(sql);
        comando.setString(1, "%" + referencia + "%"); // Busca cualquier coincidencia

        registro = comando.executeQuery();
        modeloTabla.setRowCount(0); // Limpiar tabla

        boolean encontrado = false;
        while (registro.next()) {
            String id = registro.getString("id_empleado");
            String nombre = registro.getString("nombre_empleado");
            String paterno = registro.getString("paterno_empleado");
            String materno = registro.getString("materno_empleado");
            String usuario = registro.getString("usuario_empleado");
            String telefono = registro.getString("telefono_empleado");

            modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, usuario, telefono});
            encontrado = true;
        }

        if (!encontrado && !referencia.isEmpty()) {
            // Opcional: mostrar mensaje si no hay resultados
            // JOptionPane.showMessageDialog(consultas, "No se encontraron empleados con ese nombre.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(consultas, "Error al cargar el driver: " + ex.getMessage());
        Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(consultas, "Error en la base de datos: " + ex.getMessage());
        Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (registro != null) registro.close();
            if (comando != null) comando.close();
            if (conexion != null) conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    public void GuardarCambiosEmpleados(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar empleado
            String sql = "UPDATE empleado SET "
                    + "nombre_empleado = ?, "
                    + "paterno_empleado = ?, "
                    + "materno_empleado = ?, "
                    + "usuario_empleado = ?,"
                    + "telefono_empleado = ? "
                    + "WHERE id_empleado = ?";

            comando = conexion.prepareStatement(sql);

            DefaultTableModel modelo = (DefaultTableModel) this.consultas.tblUsuario.getModel();
            int filasActualizadas = 0;

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();

                // Obtener y convertir a mayúsculas
                String nombre = modelo.getValueAt(i, 1).toString().trim().toUpperCase();
                String paterno = modelo.getValueAt(i, 2).toString().trim().toUpperCase();
                String materno = modelo.getValueAt(i, 3).toString().trim().toUpperCase();
                String usuario = modelo.getValueAt(i, 4).toString().trim().toUpperCase();
                String telefono = modelo.getValueAt(i, 5).toString().trim();

                // Validación básica
                if (nombre.isEmpty() || paterno.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "Los campos Nombre, Apellido Paterno y Teléfono son obligatorios (ID: " + id + ")",
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                // Asignar valores al PreparedStatement
                comando.setString(1, nombre);
                comando.setString(2, paterno);
                comando.setString(3, materno);
                comando.setString(4, usuario);
                comando.setString(5, telefono);
                comando.setString(6, id);

                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nEmpleados actualizados: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizaron actualizaciones.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (comando != null) {
                    comando.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
=======
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vistas.vistaConsultas;
import java.sql.PreparedStatement;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.logging.Level;

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
            String telefono) {

        // === VALIDACIONES ===
        // Verificar que ningún campo esté vacío
        if (nombre.trim().isEmpty()
                || paterno.trim().isEmpty()
                || usuario.trim().isEmpty()
                || contraseña.trim().isEmpty()
                || telefono.trim().isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que el teléfono tenga exactamente 10 dígitos y solo números
        if (!telefono.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null,
                    "El teléfono debe contener exactamente 10 dígitos numéricos.",
                    "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conexion = null;
        PreparedStatement consulta = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            // Verificar si el usuario ya existe
            String sqlConsulta = "SELECT * FROM empleado WHERE usuario_empleado = ?";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, usuario.trim());
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "El nombre de usuario ya está en uso.",
                        "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Insertar nuevo usuario (contraseña en texto plano)
            String sqlInsert = "INSERT INTO empleado "
                    + "(nombre_empleado, materno_empleado, paterno_empleado, "
                    + "usuario_empleado, contraseña_empleado, telefono_empleado) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, nombre.trim());
            comando.setString(2, materno.trim());
            comando.setString(3, paterno.trim());
            comando.setString(4, usuario.trim());
            comando.setString(5, contraseña); 
            comando.setString(6, telefono);

            int filas = comando.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Usuario registrado correctamente.\nUsuario: " + usuario,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error en la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos en orden inverso
            try {
                if (registro != null) {
                    registro.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (consulta != null) {
                    consulta.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (comando != null) {
                    comando.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Listado(vistaConsultas consultas) {
        this.consultas = consultas;

        Statement comando;
        Connection conexion = null;

        String[] columnas = {"Id_empleado",
            "Nombre_empleado",
            "Paterno_empleado",
            "Materno_empleado",
            "Usuario_empleado",
            "Telefono_empleado"};

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
                String usuario = registro.getString("usuario_empleado");
                String telefono = registro.getString("telefono_empleado");
                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, usuario, telefono});
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

    Connection conexion = null;
    PreparedStatement comando = null;
    ResultSet registro = null;

    String[] columnas = {
        "Id_empleado",
        "Nombre_empleado",
        "Paterno_empleado",
        "Materno_empleado",
        "Usuario_empleado",
        "Telefono_empleado"
    };

    DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    this.consultas.tblUsuario.setModel(modeloTabla);

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_vargas",
                "root",
                "cisco1234");

        // Usar LIKE para búsqueda parcial
        String sql = "SELECT id_empleado, nombre_empleado, paterno_empleado, " +
                     "materno_empleado, usuario_empleado, telefono_empleado " +
                     "FROM empleado " +
                     "WHERE nombre_empleado LIKE ?";

        comando = conexion.prepareStatement(sql);
        comando.setString(1, "%" + referencia + "%"); // Busca cualquier coincidencia

        registro = comando.executeQuery();
        modeloTabla.setRowCount(0); // Limpiar tabla

        boolean encontrado = false;
        while (registro.next()) {
            String id = registro.getString("id_empleado");
            String nombre = registro.getString("nombre_empleado");
            String paterno = registro.getString("paterno_empleado");
            String materno = registro.getString("materno_empleado");
            String usuario = registro.getString("usuario_empleado");
            String telefono = registro.getString("telefono_empleado");

            modeloTabla.addRow(new Object[]{id, nombre, paterno, materno, usuario, telefono});
            encontrado = true;
        }

        if (!encontrado && !referencia.isEmpty()) {
            // Opcional: mostrar mensaje si no hay resultados
            // JOptionPane.showMessageDialog(consultas, "No se encontraron empleados con ese nombre.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(consultas, "Error al cargar el driver: " + ex.getMessage());
        Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(consultas, "Error en la base de datos: " + ex.getMessage());
        Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (registro != null) registro.close();
            if (comando != null) comando.close();
            if (conexion != null) conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    public void GuardarCambiosEmpleados(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar empleado
            String sql = "UPDATE empleado SET "
                    + "nombre_empleado = ?, "
                    + "paterno_empleado = ?, "
                    + "materno_empleado = ?, "
                    + "usuario_empleado = ?,"
                    + "telefono_empleado = ? "
                    + "WHERE id_empleado = ?";

            comando = conexion.prepareStatement(sql);

            DefaultTableModel modelo = (DefaultTableModel) this.consultas.tblUsuario.getModel();
            int filasActualizadas = 0;

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();

                // Obtener y convertir a mayúsculas
                String nombre = modelo.getValueAt(i, 1).toString().trim().toUpperCase();
                String paterno = modelo.getValueAt(i, 2).toString().trim().toUpperCase();
                String materno = modelo.getValueAt(i, 3).toString().trim().toUpperCase();
                String usuario = modelo.getValueAt(i, 4).toString().trim().toUpperCase();
                String telefono = modelo.getValueAt(i, 5).toString().trim();

                // Validación básica
                if (nombre.isEmpty() || paterno.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "Los campos Nombre, Apellido Paterno y Teléfono son obligatorios (ID: " + id + ")",
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                // Asignar valores al PreparedStatement
                comando.setString(1, nombre);
                comando.setString(2, paterno);
                comando.setString(3, materno);
                comando.setString(4, usuario);
                comando.setString(5, telefono);
                comando.setString(6, id);

                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nEmpleados actualizados: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizaron actualizaciones.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (comando != null) {
                    comando.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
