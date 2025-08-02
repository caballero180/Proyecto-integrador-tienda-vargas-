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
import javax.swing.table.DefaultTableModel;
import vistas.vistaConsultas;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

/**
 *
 * @author jl393
 */
public class ModeloCategoria {

    private vistaConsultas consultas;

    public void altaCategoria(String nombre) {
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

            // === 1. Validar que el nombre no esté vacío ===
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "El nombre de la categoría no puede estar vacío.",
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreLimpio = nombre.trim();

            // === 2. Verificar si la categoría ya existe (CASE INSENSITIVE) ===
            String sqlConsulta = "SELECT * FROM categoria WHERE UPPER(cat) = UPPER(?)";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, nombreLimpio);
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "La categoría '" + nombreLimpio + "' ya existe en la base de datos.",
                        "Categoría duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 3. Insertar nueva categoría ===
            String sqlInsert = "INSERT INTO categoria (cat) VALUES (?)";
            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, nombreLimpio);

            int filas = comando.executeUpdate();

            // === 4. Mostrar mensaje de éxito ===
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Categoría registrada con éxito:\n\n"
                        + "Categoría: " + nombreLimpio,
                        "Alta exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar la categoría.",
                        "Error en el registro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el driver de MySQL:\n" + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado:\n" + ex.getMessage(),
                    "Error crítico", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
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

        Connection conexion = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        // Definir columnas de la tabla
        String[] columnas = {
            "Id_categoria",
            "Categoria"
        };

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

            // Consulta con LIKE para búsqueda parcial
            String sql = "SELECT id_cat, cat "
                    + "FROM categoria "
                    + "WHERE cat LIKE ?";

            comando = conexion.prepareStatement(sql);
            comando.setString(1, "%" + referencia + "%"); // Coincidencia parcial

            // Ejecutar consulta
            registro = comando.executeQuery();
            modeloTabla.setRowCount(0); // Limpiar tabla antes de agregar nuevos datos

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_cat");
                String categoria = registro.getString("cat");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, categoria});
                encontrado = true;
            }

            // Opcional: mostrar mensaje si no hay resultados (y el campo no está vacío)
            /*
        if (!encontrado && !referencia.trim().isEmpty()) {
            JOptionPane.showMessageDialog(consultas,
                "No se encontraron categorías con ese nombre.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
             */
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos en orden
            try {
                if (registro != null) {
                    registro.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (comando != null) {
                    comando.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void GuardarCambiosCategorias(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar categoría
            String sql = "UPDATE categoria SET cat = ? WHERE id_cat = ?";
            comando = conexion.prepareStatement(sql);

            // Obtener modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) consultas.tblCat.getModel();
            int filasActualizadas = 0;

            // Recorrer filas
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();
                String categoria = modelo.getValueAt(i, 1).toString().trim().toUpperCase(); // ya está en mayúsculas, pero aseguramos

                // Validación: no permitir categoría vacía
                if (categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "La categoría no puede estar vacía (ID: " + id + ")",
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                // Asignar valores
                comando.setString(1, categoria);
                comando.setString(2, id);

                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            // Mensaje final
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nCategorías actualizadas: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizaron actualizaciones.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
            try {
                if (comando != null) {
                    comando.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
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
import javax.swing.table.DefaultTableModel;
import vistas.vistaConsultas;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

/**
 *
 * @author jl393
 */
public class ModeloCategoria {

    private vistaConsultas consultas;

    public void altaCategoria(String nombre) {
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

            // === 1. Validar que el nombre no esté vacío ===
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "El nombre de la categoría no puede estar vacío.",
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreLimpio = nombre.trim();

            // === 2. Verificar si la categoría ya existe (CASE INSENSITIVE) ===
            String sqlConsulta = "SELECT * FROM categoria WHERE UPPER(cat) = UPPER(?)";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, nombreLimpio);
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "La categoría '" + nombreLimpio + "' ya existe en la base de datos.",
                        "Categoría duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 3. Insertar nueva categoría ===
            String sqlInsert = "INSERT INTO categoria (cat) VALUES (?)";
            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, nombreLimpio);

            int filas = comando.executeUpdate();

            // === 4. Mostrar mensaje de éxito ===
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Categoría registrada con éxito:\n\n"
                        + "Categoría: " + nombreLimpio,
                        "Alta exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar la categoría.",
                        "Error en el registro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el driver de MySQL:\n" + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado:\n" + ex.getMessage(),
                    "Error crítico", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
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

        Connection conexion = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        // Definir columnas de la tabla
        String[] columnas = {
            "Id_categoria",
            "Categoria"
        };

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

            // Consulta con LIKE para búsqueda parcial
            String sql = "SELECT id_cat, cat "
                    + "FROM categoria "
                    + "WHERE cat LIKE ?";

            comando = conexion.prepareStatement(sql);
            comando.setString(1, "%" + referencia + "%"); // Coincidencia parcial

            // Ejecutar consulta
            registro = comando.executeQuery();
            modeloTabla.setRowCount(0); // Limpiar tabla antes de agregar nuevos datos

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_cat");
                String categoria = registro.getString("cat");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{id, categoria});
                encontrado = true;
            }

            // Opcional: mostrar mensaje si no hay resultados (y el campo no está vacío)
            /*
        if (!encontrado && !referencia.trim().isEmpty()) {
            JOptionPane.showMessageDialog(consultas,
                "No se encontraron categorías con ese nombre.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
             */
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos en orden
            try {
                if (registro != null) {
                    registro.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (comando != null) {
                    comando.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void GuardarCambiosCategorias(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar categoría
            String sql = "UPDATE categoria SET cat = ? WHERE id_cat = ?";
            comando = conexion.prepareStatement(sql);

            // Obtener modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) consultas.tblCat.getModel();
            int filasActualizadas = 0;

            // Recorrer filas
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();
                String categoria = modelo.getValueAt(i, 1).toString().trim().toUpperCase(); // ya está en mayúsculas, pero aseguramos

                // Validación: no permitir categoría vacía
                if (categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "La categoría no puede estar vacía (ID: " + id + ")",
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                // Asignar valores
                comando.setString(1, categoria);
                comando.setString(2, id);

                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            // Mensaje final
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nCategorías actualizadas: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizaron actualizaciones.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
            try {
                if (comando != null) {
                    comando.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
