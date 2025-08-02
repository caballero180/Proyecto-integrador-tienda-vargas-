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

/**
 *
 * @author jl393
 */
public class ModeloProveedor {

    private vistaConsultas consultas;

    public void altaProveedor(String empresa, String nombre, String telefono, String mail) {
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

            // === 1. Validar campos vacíos ===
            if (empresa == null || empresa.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()
                    || mail == null || mail.trim().isEmpty()) {

                JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios.\nPor favor, completa todos los datos.",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String empresaLimpia = empresa.trim();
            String nombreLimpio = nombre.trim();
            String telefonoLimpio = telefono.trim();
            String mailLimpio = mail.trim();

            // === 2. Validar teléfono: solo 10 dígitos ===
            if (!telefonoLimpio.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null,
                        "El teléfono debe contener exactamente 10 dígitos numéricos.",
                        "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 3. Validar formato de correo ===
            if (!mailLimpio.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
                JOptionPane.showMessageDialog(null,
                        "El formato del correo electrónico no es válido.\nEjemplo: ejemplo@dominio.com",
                        "Correo inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 4. Verificar si ya existe el proveedor por empresa ===
            String sqlConsulta = "SELECT * FROM proveedor WHERE UPPER(empresa_proveedor) = UPPER(?)";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, empresaLimpia);
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "Ya existe un proveedor con el nombre de empresa:\n" + empresaLimpia,
                        "Proveedor duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 5. Insertar nuevo proveedor ===
            String sqlInsert = "INSERT INTO proveedor "
                    + "(empresa_proveedor, nombre_proveedor, telefono_proveedor, email_proveedor) "
                    + "VALUES (?, ?, ?, ?)";

            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, empresaLimpia);
            comando.setString(2, nombreLimpio);
            comando.setString(3, telefonoLimpio);
            comando.setString(4, mailLimpio);

            int filas = comando.executeUpdate();

            // === 6. Mensaje de éxito ===
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Proveedor registrado con éxito.\n\n"
                        + "Empresa: " + empresaLimpia + "\n"
                        + "Contacto: " + nombreLimpio + "\n"
                        + "Teléfono: " + telefonoLimpio + "\n"
                        + "Correo: " + mailLimpio,
                        "Alta exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el proveedor.",
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

        Connection conexion = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        // Definir columnas de la tabla
        String[] columnas = {
            "Id_proveedor",
            "Empresa_proveedor",
            "Nombre_proveedor",
            "Telefono_proveedor",
            "Email_proveedor"
        };

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

            // Consulta con LIKE para búsqueda parcial
            String sql = "SELECT id_proveedor, empresa_proveedor, nombre_proveedor, "
                    + "telefono_proveedor, email_proveedor "
                    + "FROM proveedor "
                    + "WHERE empresa_proveedor LIKE ?";

            comando = conexion.prepareStatement(sql);
            comando.setString(1, "%" + referencia + "%"); // Coincidencia parcial

            // Ejecutar consulta
            registro = comando.executeQuery();
            modeloTabla.setRowCount(0); // Limpiar tabla

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_proveedor");
                String empresa = registro.getString("empresa_proveedor");
                String nombre = registro.getString("nombre_proveedor");
                String telefono = registro.getString("telefono_proveedor");
                String email = registro.getString("email_proveedor");

                // Agregar fila al modelo
                modeloTabla.addRow(new Object[]{id, empresa, nombre, telefono, email});
                encontrado = true;
            }

            // Opcional: puedes mostrar si no hay resultados
            // if (!encontrado && !referencia.isEmpty()) {
            //     JOptionPane.showMessageDialog(consultas, "No se encontraron proveedores.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            // }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
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

    public void GuardarCambiosProveedores(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            // Cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar un proveedor
            String sql = "UPDATE proveedor SET "
                    + "empresa_proveedor = ?, "
                    + "nombre_proveedor = ?, "
                    + "telefono_proveedor = ?, "
                    + "email_proveedor = ? "
                    + "WHERE id_proveedor = ?";

            comando = conexion.prepareStatement(sql);

            // Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) this.consultas.tblProveedor.getModel();

            int filasActualizadas = 0;

            // Recorrer todas las filas de la tabla
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();
                String empresa = modelo.getValueAt(i, 1).toString().trim().toUpperCase();
                String nombre = modelo.getValueAt(i, 2).toString().trim().toUpperCase();
                String telefono = modelo.getValueAt(i, 3).toString().trim();
                String email = modelo.getValueAt(i, 4).toString().trim();

                // Validaciones básicas
                if (empresa.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "La empresa, nombre y teléfono no pueden estar vacíos en el proveedor ID: " + id,
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue; // Saltar este registro
                }

                // Asignar valores al PreparedStatement
                comando.setString(1, empresa);
                comando.setString(2, nombre);
                comando.setString(3, telefono);
                comando.setString(4, email);
                comando.setString(5, id); // Clave primaria

                // Ejecutar actualización
                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            // Mensaje final
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nRegistros actualizados: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizó ninguna actualización.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
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

/**
 *
 * @author jl393
 */
public class ModeloProveedor {

    private vistaConsultas consultas;

    public void altaProveedor(String empresa, String nombre, String telefono, String mail) {
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

            // === 1. Validar campos vacíos ===
            if (empresa == null || empresa.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()
                    || mail == null || mail.trim().isEmpty()) {

                JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios.\nPor favor, completa todos los datos.",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String empresaLimpia = empresa.trim();
            String nombreLimpio = nombre.trim();
            String telefonoLimpio = telefono.trim();
            String mailLimpio = mail.trim();

            // === 2. Validar teléfono: solo 10 dígitos ===
            if (!telefonoLimpio.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null,
                        "El teléfono debe contener exactamente 10 dígitos numéricos.",
                        "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 3. Validar formato de correo ===
            if (!mailLimpio.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
                JOptionPane.showMessageDialog(null,
                        "El formato del correo electrónico no es válido.\nEjemplo: ejemplo@dominio.com",
                        "Correo inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 4. Verificar si ya existe el proveedor por empresa ===
            String sqlConsulta = "SELECT * FROM proveedor WHERE UPPER(empresa_proveedor) = UPPER(?)";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, empresaLimpia);
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "Ya existe un proveedor con el nombre de empresa:\n" + empresaLimpia,
                        "Proveedor duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 5. Insertar nuevo proveedor ===
            String sqlInsert = "INSERT INTO proveedor "
                    + "(empresa_proveedor, nombre_proveedor, telefono_proveedor, email_proveedor) "
                    + "VALUES (?, ?, ?, ?)";

            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, empresaLimpia);
            comando.setString(2, nombreLimpio);
            comando.setString(3, telefonoLimpio);
            comando.setString(4, mailLimpio);

            int filas = comando.executeUpdate();

            // === 6. Mensaje de éxito ===
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Proveedor registrado con éxito.\n\n"
                        + "Empresa: " + empresaLimpia + "\n"
                        + "Contacto: " + nombreLimpio + "\n"
                        + "Teléfono: " + telefonoLimpio + "\n"
                        + "Correo: " + mailLimpio,
                        "Alta exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el proveedor.",
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

        Connection conexion = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        // Definir columnas de la tabla
        String[] columnas = {
            "Id_proveedor",
            "Empresa_proveedor",
            "Nombre_proveedor",
            "Telefono_proveedor",
            "Email_proveedor"
        };

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

            // Consulta con LIKE para búsqueda parcial
            String sql = "SELECT id_proveedor, empresa_proveedor, nombre_proveedor, "
                    + "telefono_proveedor, email_proveedor "
                    + "FROM proveedor "
                    + "WHERE empresa_proveedor LIKE ?";

            comando = conexion.prepareStatement(sql);
            comando.setString(1, "%" + referencia + "%"); // Coincidencia parcial

            // Ejecutar consulta
            registro = comando.executeQuery();
            modeloTabla.setRowCount(0); // Limpiar tabla

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_proveedor");
                String empresa = registro.getString("empresa_proveedor");
                String nombre = registro.getString("nombre_proveedor");
                String telefono = registro.getString("telefono_proveedor");
                String email = registro.getString("email_proveedor");

                // Agregar fila al modelo
                modeloTabla.addRow(new Object[]{id, empresa, nombre, telefono, email});
                encontrado = true;
            }

            // Opcional: puedes mostrar si no hay resultados
            // if (!encontrado && !referencia.isEmpty()) {
            //     JOptionPane.showMessageDialog(consultas, "No se encontraron proveedores.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            // }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloCategoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
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

    public void GuardarCambiosProveedores(vistaConsultas consultas) {
        Connection conexion = null;
        PreparedStatement comando = null;
        this.consultas = consultas;

        try {
            // Cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234"
            );

            // Consulta SQL para actualizar un proveedor
            String sql = "UPDATE proveedor SET "
                    + "empresa_proveedor = ?, "
                    + "nombre_proveedor = ?, "
                    + "telefono_proveedor = ?, "
                    + "email_proveedor = ? "
                    + "WHERE id_proveedor = ?";

            comando = conexion.prepareStatement(sql);

            // Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) this.consultas.tblProveedor.getModel();

            int filasActualizadas = 0;

            // Recorrer todas las filas de la tabla
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();
                String empresa = modelo.getValueAt(i, 1).toString().trim().toUpperCase();
                String nombre = modelo.getValueAt(i, 2).toString().trim().toUpperCase();
                String telefono = modelo.getValueAt(i, 3).toString().trim();
                String email = modelo.getValueAt(i, 4).toString().trim();

                // Validaciones básicas
                if (empresa.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(consultas,
                            "La empresa, nombre y teléfono no pueden estar vacíos en el proveedor ID: " + id,
                            "Error de validación", JOptionPane.WARNING_MESSAGE);
                    continue; // Saltar este registro
                }

                // Asignar valores al PreparedStatement
                comando.setString(1, empresa);
                comando.setString(2, nombre);
                comando.setString(3, telefono);
                comando.setString(4, email);
                comando.setString(5, id); // Clave primaria

                // Ejecutar actualización
                int filas = comando.executeUpdate();
                if (filas > 0) {
                    filasActualizadas++;
                }
            }

            // Mensaje final
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(consultas,
                        "Cambios guardados correctamente.\nRegistros actualizados: " + filasActualizadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(consultas,
                        "No se realizó ninguna actualización.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al cargar el driver de MySQL: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas,
                    "Error al guardar en la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
