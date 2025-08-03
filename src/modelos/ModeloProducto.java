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
import vistas.visatAltas;
import vistas.vistaConsultas;
import java.sql.PreparedStatement;

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

        // === 1. Validaciones ===
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El nombre del producto es obligatorio.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombreLimpio = nombre.trim();

        // Validar descripción (opcional, pero si viene vacía, asignar valor por defecto)
        if (descripcion == null || descripcion.trim().isEmpty()) {
            descripcion = "Sin descripción";
        } else {
            descripcion = descripcion.trim();
        }

        // Validar stock, compra y precio
        if (stock < 0) {
            JOptionPane.showMessageDialog(null,
                    "El stock no puede ser negativo.",
                    "Stock inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (compra < 0) {
            JOptionPane.showMessageDialog(null,
                    "El precio de compra no puede ser negativo.",
                    "Precio de compra inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (precio < 0) {
            JOptionPane.showMessageDialog(null,
                    "El precio de venta no puede ser negativo.",
                    "Precio de venta inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (precio < compra) {
            JOptionPane.showMessageDialog(null,
                    "Advertencia: El precio de venta es menor al de compra.",
                    "Precio inusual", JOptionPane.WARNING_MESSAGE);
            // Permitimos continuar, pero con advertencia
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

            // === 2. Verificar si el producto ya existe (por nombre) ===
            String sqlConsulta = "SELECT * FROM producto WHERE nombre_producto = ?";
            consulta = conexion.prepareStatement(sqlConsulta);
            consulta.setString(1, nombreLimpio);
            registro = consulta.executeQuery();

            if (registro.next()) {
                JOptionPane.showMessageDialog(null,
                        "Ya existe un producto con el nombre:\n" + nombreLimpio,
                        "Producto duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // === 3. Insertar nuevo producto ===
            String sqlInsert = "INSERT INTO producto "
                    + "(nombre_producto, id_categoria, id_proveedor, "
                    + "descripcion_producto, stock, compra, precio) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            comando = conexion.prepareStatement(sqlInsert);
            comando.setString(1, nombreLimpio);
            comando.setInt(2, categoria);
            comando.setInt(3, proveedor);
            comando.setString(4, descripcion);
            comando.setInt(5, stock);
            comando.setDouble(6, compra);
            comando.setDouble(7, precio);

            int filas = comando.executeUpdate();

            // === 4. Mensaje de resultado ===
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Producto registrado con éxito.\n\n"
                        + "Nombre: " + nombreLimpio + "\n"
                        + "Categoría ID: " + categoria + "\n"
                        + "Proveedor ID: " + proveedor + "\n"
                        + "Stock: " + stock + "\n"
                        + "Precio: $" + String.format("%.2f", precio),
                        "Alta exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el producto.",
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

        Connection conexion = null;
        PreparedStatement comando = null;
        ResultSet registro = null;

        // Definir columnas de la tabla
        String[] columnas = {
            "Id_producto",
            "nombre",
            "categoria",
            "proveedor",
            "Descripcion",
            "Stock",
            "Compra",
            "Precio"
        };

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

            // Consulta con LIKE para búsqueda parcial
            String sql = "SELECT p.id_producto, p.nombre_producto, p.descripcion_producto, "
                    + "p.stock, p.compra, p.precio, "
                    + "c.cat AS categoria, pr.empresa_proveedor AS proveedor "
                    + "FROM producto p "
                    + "LEFT JOIN categoria c ON p.id_categoria = c.id_cat "
                    + "LEFT JOIN proveedor pr ON p.id_proveedor = pr.id_proveedor "
                    + "WHERE p.nombre_producto LIKE ?";

            comando = conexion.prepareStatement(sql);
            comando.setString(1, "%" + referencia + "%"); // Búsqueda parcial

            // Ejecutar consulta
            registro = comando.executeQuery();
            modeloTabla.setRowCount(0); // Limpiar tabla antes de agregar nuevos datos

            boolean encontrado = false;
            while (registro.next()) {
                String id = registro.getString("id_producto");
                String nombre = registro.getString("nombre_producto");
                String categoria = registro.getString("categoria"); // Nombre de la categoría
                String proveedor = registro.getString("proveedor"); // Nombre del proveedor
                String descripcion = registro.getString("descripcion_producto");
                String stock = registro.getString("stock");
                String compra = registro.getString("compra");
                String precio = registro.getString("precio");

                // Agregar fila al modelo de tabla
                modeloTabla.addRow(new Object[]{
                    id, nombre, categoria, proveedor, descripcion, stock, compra, precio
                });
                encontrado = true;
            }

            // Opcional: si no se encontró nada y el texto no está vacío
            /*
        if (!encontrado && !referencia.trim().isEmpty()) {
            JOptionPane.showMessageDialog(consultas,
                "No se encontraron productos con ese nombre.",
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

    public void GuardarCambios(vistaConsultas consultas) {
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
                    "cisco1234");

            // Preparar consulta de actualización
            String sql = "UPDATE producto SET "
                    + "nombre_producto = ?, "
                    + "id_categoria = ?, "
                    + "id_proveedor = ?, "
                    + "descripcion_producto = ?, "
                    + "stock = ?, "
                    + "compra = ?, "
                    + "precio = ? "
                    + "WHERE id_producto = ?";

            comando = conexion.prepareStatement(sql);

            // Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) this.consultas.tblConsulta.getModel();

            // Recorrer todas las filas de la tabla
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String id = modelo.getValueAt(i, 0).toString();
                String nombre = modelo.getValueAt(i, 1).toString().toUpperCase();
                String categoria = modelo.getValueAt(i, 2).toString();
                String proveedor = modelo.getValueAt(i, 3).toString();
                String descripcion = modelo.getValueAt(i, 4).toString().toUpperCase();
                String stock = modelo.getValueAt(i, 5).toString();
                String compra = modelo.getValueAt(i, 6).toString();
                String precio = modelo.getValueAt(i, 7).toString();

                // Asignar valores al PreparedStatement
                comando.setString(1, nombre);
                comando.setString(2, categoria);
                comando.setString(3, proveedor);
                comando.setString(4, descripcion);
                comando.setString(5, stock);
                comando.setString(6, compra);
                comando.setString(7, precio);
                comando.setString(8, id); // Condición WHERE

                // Ejecutar actualización
                comando.executeUpdate();
            }

            JOptionPane.showMessageDialog(consultas, "Cambios guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(consultas, "Error al guardar en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(consultas, "Error al cargar el driver: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
