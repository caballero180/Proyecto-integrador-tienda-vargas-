<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import vistas.vistaVentas;

/**
 *
 * @author jl393
 */
public class ModeloVenta {

    private Connection conexion = null;
    private DefaultTableModel modeloResultados;
    private vistaVentas vista;

    public void buscarProducto(JTextField txtBuscar, JTable tblResultados) {
        // Obtener el texto de búsqueda
        String textoBusqueda = txtBuscar.getText().trim().toUpperCase();

        // Validar que no esté vacío
        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre para buscar.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Asegúrate de que el modelo esté asignado a la tabla (una sola vez, no aquí)
        DefaultTableModel modelo = (DefaultTableModel) tblResultados.getModel();
        modelo.setRowCount(0); // Limpiar resultados anteriores

        // Consulta SQL con parámetro ?
        String sql = "SELECT id_producto, nombre_producto, precio, stock "
                + "FROM producto "
                + "WHERE nombre_producto LIKE ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_vargas", "root", "cisco1234"); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Cargar el driver (una sola vez en toda la app, pero por si acaso)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer el parámetro con % para búsqueda parcial
            pstmt.setString(1, "%" + textoBusqueda + "%");

            // Ejecutar consulta
            ResultSet rs = pstmt.executeQuery();

            boolean encontrado = false;
            while (rs.next()) {
                String id = rs.getString("id_producto");
                String nombre = rs.getString("nombre_producto");
                String precio = rs.getString("precio");
                String stock = rs.getString("stock");

                modelo.addRow(new Object[]{id, nombre, precio, stock});
                encontrado = true;
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "No se encontraron productos con ese nombre.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(),
                    "Error de SQL", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloVenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarDetallesProducto(vistaVentas vista) {
        this.vista = vista;

        int fila = this.vista.tblProducto.getSelectedRow();

        if (fila >= 0) { // Asegurarse de que haya una fila seleccionada
            try {
                // Obtener los valores de la fila
                Object nombreObj = this.vista.tblProducto.getValueAt(fila, 1);
                Object precioObj = this.vista.tblProducto.getValueAt(fila, 2);
                Object stockObj = this.vista.tblProducto.getValueAt(fila, 3);

                // Validar que los valores no sean nulos
                if (nombreObj == null || precioObj == null || stockObj == null) {
                    JOptionPane.showMessageDialog(this.vista,
                            "Error: Datos incompletos en la fila seleccionada.",
                            "Datos faltantes", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nombre = nombreObj.toString();
                String precio = precioObj.toString();
                String stock = stockObj.toString();

                // Llenar las cajas de texto
                this.vista.lblNombre.setText(nombre);
                this.vista.lblPrecio.setText(precio);
                this.vista.lblStock.setText(stock);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.vista,
                        "Error al obtener detalles del producto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void calcularTotalParcial(vistaVentas vista) {
        this.vista = vista;

        try {
            // Obtener cantidad ingresada
            String cantidadTexto = vista.cantPrecio.getText().trim();
            if (cantidadTexto.isEmpty()) {
                vista.lblSubtotal.setText("");
                return;
            }

            int cantidad = Integer.parseInt(cantidadTexto);

            // Obtener precio unitario
            String precioTexto = vista.lblPrecio.getText().trim();
            if (precioTexto.isEmpty()) {
                return;
            }

            double precio = Double.parseDouble(precioTexto);

            // Validar stock
            String stockTexto = vista.lblStock.getText().trim();
            int stock = Integer.parseInt(stockTexto);

            if (cantidad > stock) {
                JOptionPane.showMessageDialog(vista,
                        "La cantidad solicitada excede el stock disponible (" + stock + ")",
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                vista.cantPrecio.setText(String.valueOf(stock)); // Ajustar a stock máximo
                cantidad = stock;
            }

            // Calcular total parcial
            double total = cantidad * precio;

            // Mostrar total formateado (2 decimales)
            vista.lblSubtotal.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            // Si el usuario escribe letras o caracteres inválidos
            vista.lblSubtotal.setText("0.00");
        } catch (Exception ex) {
            vista.lblSubtotal.setText("Error");
        }
    }

    public void actualizarTotalVenta(vistaVentas vista) {
        this.vista = vista;
        double total = 0.0;

        DefaultTableModel modelo = (DefaultTableModel) this.vista.tblVenta.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String totalStr = modelo.getValueAt(i, 4).toString(); // Columna "Total"
            total += Double.parseDouble(totalStr);
        }

        // Mostrar el total formateado
        vista.totalVenta.setText(String.format("%.2f", total));
    }

    public void agregarProductoAVenta(vistaVentas vista) {
        this.vista = vista;

        // Obtener la fila seleccionada en tblProducto
        int filaSeleccionada = vista.tblProducto.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor, selecciona un producto de la lista.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos desde tblProducto (índices: 0=id, 1=nombre, 2=precio, 3=stock)
        String idProducto = vista.tblProducto.getValueAt(filaSeleccionada, 0).toString();
        String nombre = vista.tblProducto.getValueAt(filaSeleccionada, 1).toString();
        String precioStr = vista.tblProducto.getValueAt(filaSeleccionada, 2).toString();
        String stockStr = vista.tblProducto.getValueAt(filaSeleccionada, 3).toString();

        // Obtener cantidad ingresada por el usuario
        String cantidadStr = vista.cantPrecio.getText().trim();

        // Validar que se haya ingresado una cantidad
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Ingresa la cantidad que deseas vender.",
                    "Cantidad requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar formato de números
        int cantidad;
        double precio;
        int stock;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el formato de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones lógicas
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a 0.",
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cantidad > stock) {
            JOptionPane.showMessageDialog(vista,
                    "Stock insuficiente. Disponible: " + stock,
                    "Error de inventario", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Calcular total parcial
        double totalParcial = (cantidad * precio);

        // Obtener modelo de la tabla de venta
        DefaultTableModel modeloVenta = (DefaultTableModel) vista.tblVenta.getModel();

        // Agregar fila: [id_producto, nombre, precio, cantidad, total]
        modeloVenta.addRow(new Object[]{
            idProducto, // Columna 0 - ID (oculto)
            nombre, // Columna 1 - Nombre
            String.format("%.2f", precio), // Columna 2 - Precio
            cantidad, // Columna 3 - Cantidad
            String.format("%.2f", totalParcial) // Columna 4 - Total
        });

        // Actualizar el total acumulado de la venta
        actualizarTotalVenta(vista);

        // Opcional: limpiar campo de cantidad para siguiente producto
        vista.cantPrecio.setText("");
        this.vista.lblNombre.setText("");
        this.vista.lblPrecio.setText("");
        this.vista.lblStock.setText("");
        this.vista.lblSubtotal.setText("0.00");
    }

    public void eliminarProductoDeVenta(vistaVentas vista) {
        this.vista = vista;

        // Obtener la fila seleccionada
        int fila = vista.tblVenta.getSelectedRow();

        // Validar que haya una fila seleccionada
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor, selecciona un producto de la lista para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación (opcional, pero recomendado)
        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Estás seguro de que deseas eliminar este producto de la venta?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Si elige "No", no hace nada
        }

        try {
            // Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) vista.tblVenta.getModel();

            // Eliminar la fila seleccionada
            modelo.removeRow(fila);

            // Actualizar el total acumulado de la venta
            actualizarTotalVenta(vista);

            JOptionPane.showMessageDialog(vista,
                    "Producto eliminado de la venta.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al eliminar el producto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void cobrar(vistaVentas vista) {
        this.vista = vista;

        // Validar que haya productos en la venta
        DefaultTableModel modeloVenta = (DefaultTableModel) vista.tblVenta.getModel();
        if (modeloVenta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista,
                    "No hay productos en la venta.",
                    "Venta vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que se haya ingresado el ID del empleado
        String idEmpleadoStr = this.vista.idUsuario.getText().trim();
        if (idEmpleadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Ingresa el ID del empleado que realiza la venta.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEmpleado;
        try {
            idEmpleado = Integer.parseInt(idEmpleadoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "El ID del empleado debe ser un número.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener total de la venta desde el campo txtTotalVenta
        double totalVenta;
        try {
            totalVenta = Double.parseDouble(vista.totalVenta.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el total de la venta.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;
        PreparedStatement pstmtStock = null;
        ResultSet generatedKeys = null;

        try {
            // Conectar a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            // Desactivar autocommit para usar transacción
            conn.setAutoCommit(false);

            // 1. Insertar en la tabla 'venta'
            String sqlVenta = "INSERT INTO venta (id_empleado, total_venta, estatus_venta) VALUES (?, ?, ?)";
            pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setInt(1, idEmpleado);
            pstmtVenta.setDouble(2, totalVenta);
            pstmtVenta.setString(3, "cerrada");
            pstmtVenta.executeUpdate();

            // Obtener el ID de la venta generada
            generatedKeys = pstmtVenta.getGeneratedKeys();
            int idVenta;
            if (generatedKeys.next()) {
                idVenta = generatedKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID de la venta.");
            }

            // 2. Insertar cada producto en 'detalle_venta'
            String sqlDetalle = "INSERT INTO venta_produto (id_venta, id_producto, fecha_venta) VALUES (?, ?, NOW())";
            pstmtDetalle = conn.prepareStatement(sqlDetalle);

            // 3. Actualizar el stock de cada producto
            String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
            pstmtStock = conn.prepareStatement(sqlStock);

            for (int i = 0; i < modeloVenta.getRowCount(); i++) {
                int idProducto = Integer.parseInt(modeloVenta.getValueAt(i, 0).toString()); // ID oculto
                int cantidad = Integer.parseInt(modeloVenta.getValueAt(i, 3).toString());  // Cantidad

                // Insertar en detalle_venta
                pstmtDetalle.setInt(1, idVenta);
                pstmtDetalle.setInt(2, idProducto);
                pstmtDetalle.addBatch(); // Usar batch para mejor rendimiento

                // Actualizar stock
                pstmtStock.setInt(1, cantidad);
                pstmtStock.setInt(2, idProducto);
                pstmtStock.addBatch();
            }

            // Ejecutar lotes
            pstmtDetalle.executeBatch();
            pstmtStock.executeBatch();

            // Confirmar transacción
            conn.commit();

            // Mensaje de éxito
            JOptionPane.showMessageDialog(vista,
                    "Venta registrada con éxito.\n"
                    + "ID de venta: " + idVenta + "\n"
                    + "Total: $" + String.format("%.2f", totalVenta),
                    "Venta completada", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar interfaz
            limpiarInterfaz();

        } catch (ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error: No se encontró el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error al registrar la venta: " + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pstmtVenta != null) {
                    pstmtVenta.close();
                }
                if (pstmtDetalle != null) {
                    pstmtDetalle.close();
                }
                if (pstmtStock != null) {
                    pstmtStock.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarInterfaz() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tblVenta.getModel();
        modelo.setRowCount(0); // Limpiar tabla de venta

        vista.totalVenta.setText("0.00");     // Reiniciar total
        vista.idUsuario.setText("");         // Limpiar ID empleado
        vista.cantPrecio.setText("");            // Limpiar cantidad
        // Opcional: limpiar campos de producto
        vista.lblNombre.setText("");
        vista.lblPrecio.setText("");
        vista.lblStock.setText("");
    }

}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import vistas.vistaVentas;

/**
 *
 * @author jl393
 */
public class ModeloVenta {

    private Connection conexion = null;
    private DefaultTableModel modeloResultados;
    private vistaVentas vista;

    public void buscarProducto(JTextField txtBuscar, JTable tblResultados) {
        // Obtener el texto de búsqueda
        String textoBusqueda = txtBuscar.getText().trim().toUpperCase();

        // Validar que no esté vacío
        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre para buscar.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Asegúrate de que el modelo esté asignado a la tabla (una sola vez, no aquí)
        DefaultTableModel modelo = (DefaultTableModel) tblResultados.getModel();
        modelo.setRowCount(0); // Limpiar resultados anteriores

        // Consulta SQL con parámetro ?
        String sql = "SELECT id_producto, nombre_producto, precio, stock "
                + "FROM producto "
                + "WHERE nombre_producto LIKE ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_vargas", "root", "cisco1234"); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Cargar el driver (una sola vez en toda la app, pero por si acaso)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer el parámetro con % para búsqueda parcial
            pstmt.setString(1, "%" + textoBusqueda + "%");

            // Ejecutar consulta
            ResultSet rs = pstmt.executeQuery();

            boolean encontrado = false;
            while (rs.next()) {
                String id = rs.getString("id_producto");
                String nombre = rs.getString("nombre_producto");
                String precio = rs.getString("precio");
                String stock = rs.getString("stock");

                modelo.addRow(new Object[]{id, nombre, precio, stock});
                encontrado = true;
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "No se encontraron productos con ese nombre.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(),
                    "Error de SQL", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloVenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ModeloVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarDetallesProducto(vistaVentas vista) {
        this.vista = vista;

        int fila = this.vista.tblProducto.getSelectedRow();

        if (fila >= 0) { // Asegurarse de que haya una fila seleccionada
            try {
                // Obtener los valores de la fila
                Object nombreObj = this.vista.tblProducto.getValueAt(fila, 1);
                Object precioObj = this.vista.tblProducto.getValueAt(fila, 2);
                Object stockObj = this.vista.tblProducto.getValueAt(fila, 3);

                // Validar que los valores no sean nulos
                if (nombreObj == null || precioObj == null || stockObj == null) {
                    JOptionPane.showMessageDialog(this.vista,
                            "Error: Datos incompletos en la fila seleccionada.",
                            "Datos faltantes", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nombre = nombreObj.toString();
                String precio = precioObj.toString();
                String stock = stockObj.toString();

                // Llenar las cajas de texto
                this.vista.lblNombre.setText(nombre);
                this.vista.lblPrecio.setText(precio);
                this.vista.lblStock.setText(stock);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.vista,
                        "Error al obtener detalles del producto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void calcularTotalParcial(vistaVentas vista) {
        this.vista = vista;

        try {
            // Obtener cantidad ingresada
            String cantidadTexto = vista.cantPrecio.getText().trim();
            if (cantidadTexto.isEmpty()) {
                vista.lblSubtotal.setText("");
                return;
            }

            int cantidad = Integer.parseInt(cantidadTexto);

            // Obtener precio unitario
            String precioTexto = vista.lblPrecio.getText().trim();
            if (precioTexto.isEmpty()) {
                return;
            }

            double precio = Double.parseDouble(precioTexto);

            // Validar stock
            String stockTexto = vista.lblStock.getText().trim();
            int stock = Integer.parseInt(stockTexto);

            if (cantidad > stock) {
                JOptionPane.showMessageDialog(vista,
                        "La cantidad solicitada excede el stock disponible (" + stock + ")",
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                vista.cantPrecio.setText(String.valueOf(stock)); // Ajustar a stock máximo
                cantidad = stock;
            }

            // Calcular total parcial
            double total = cantidad * precio;

            // Mostrar total formateado (2 decimales)
            vista.lblSubtotal.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            // Si el usuario escribe letras o caracteres inválidos
            vista.lblSubtotal.setText("0.00");
        } catch (Exception ex) {
            vista.lblSubtotal.setText("Error");
        }
    }

    public void actualizarTotalVenta(vistaVentas vista) {
        this.vista = vista;
        double total = 0.0;

        DefaultTableModel modelo = (DefaultTableModel) this.vista.tblVenta.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String totalStr = modelo.getValueAt(i, 4).toString(); // Columna "Total"
            total += Double.parseDouble(totalStr);
        }

        // Mostrar el total formateado
        vista.totalVenta.setText(String.format("%.2f", total));
    }

    public void agregarProductoAVenta(vistaVentas vista) {
        this.vista = vista;

        // Obtener la fila seleccionada en tblProducto
        int filaSeleccionada = vista.tblProducto.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor, selecciona un producto de la lista.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos desde tblProducto (índices: 0=id, 1=nombre, 2=precio, 3=stock)
        String idProducto = vista.tblProducto.getValueAt(filaSeleccionada, 0).toString();
        String nombre = vista.tblProducto.getValueAt(filaSeleccionada, 1).toString();
        String precioStr = vista.tblProducto.getValueAt(filaSeleccionada, 2).toString();
        String stockStr = vista.tblProducto.getValueAt(filaSeleccionada, 3).toString();

        // Obtener cantidad ingresada por el usuario
        String cantidadStr = vista.cantPrecio.getText().trim();

        // Validar que se haya ingresado una cantidad
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Ingresa la cantidad que deseas vender.",
                    "Cantidad requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar formato de números
        int cantidad;
        double precio;
        int stock;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el formato de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones lógicas
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a 0.",
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cantidad > stock) {
            JOptionPane.showMessageDialog(vista,
                    "Stock insuficiente. Disponible: " + stock,
                    "Error de inventario", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Calcular total parcial
        double totalParcial = (cantidad * precio);

        // Obtener modelo de la tabla de venta
        DefaultTableModel modeloVenta = (DefaultTableModel) vista.tblVenta.getModel();

        // Agregar fila: [id_producto, nombre, precio, cantidad, total]
        modeloVenta.addRow(new Object[]{
            idProducto, // Columna 0 - ID (oculto)
            nombre, // Columna 1 - Nombre
            String.format("%.2f", precio), // Columna 2 - Precio
            cantidad, // Columna 3 - Cantidad
            String.format("%.2f", totalParcial) // Columna 4 - Total
        });

        // Actualizar el total acumulado de la venta
        actualizarTotalVenta(vista);

        // Opcional: limpiar campo de cantidad para siguiente producto
        vista.cantPrecio.setText("");
        this.vista.lblNombre.setText("");
        this.vista.lblPrecio.setText("");
        this.vista.lblStock.setText("");
        this.vista.lblSubtotal.setText("0.00");
    }

    public void eliminarProductoDeVenta(vistaVentas vista) {
        this.vista = vista;

        // Obtener la fila seleccionada
        int fila = vista.tblVenta.getSelectedRow();

        // Validar que haya una fila seleccionada
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor, selecciona un producto de la lista para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación (opcional, pero recomendado)
        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Estás seguro de que deseas eliminar este producto de la venta?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Si elige "No", no hace nada
        }

        try {
            // Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) vista.tblVenta.getModel();

            // Eliminar la fila seleccionada
            modelo.removeRow(fila);

            // Actualizar el total acumulado de la venta
            actualizarTotalVenta(vista);

            JOptionPane.showMessageDialog(vista,
                    "Producto eliminado de la venta.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al eliminar el producto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void cobrar(vistaVentas vista) {
        this.vista = vista;

        // Validar que haya productos en la venta
        DefaultTableModel modeloVenta = (DefaultTableModel) vista.tblVenta.getModel();
        if (modeloVenta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista,
                    "No hay productos en la venta.",
                    "Venta vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que se haya ingresado el ID del empleado
        String idEmpleadoStr = this.vista.idUsuario.getText().trim();
        if (idEmpleadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Ingresa el ID del empleado que realiza la venta.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEmpleado;
        try {
            idEmpleado = Integer.parseInt(idEmpleadoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "El ID del empleado debe ser un número.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener total de la venta desde el campo txtTotalVenta
        double totalVenta;
        try {
            totalVenta = Double.parseDouble(vista.totalVenta.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el total de la venta.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;
        PreparedStatement pstmtStock = null;
        ResultSet generatedKeys = null;

        try {
            // Conectar a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda_vargas",
                    "root",
                    "cisco1234");

            // Desactivar autocommit para usar transacción
            conn.setAutoCommit(false);

            // 1. Insertar en la tabla 'venta'
            String sqlVenta = "INSERT INTO venta (id_empleado, total_venta, estatus_venta) VALUES (?, ?, ?)";
            pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setInt(1, idEmpleado);
            pstmtVenta.setDouble(2, totalVenta);
            pstmtVenta.setString(3, "cerrada");
            pstmtVenta.executeUpdate();

            // Obtener el ID de la venta generada
            generatedKeys = pstmtVenta.getGeneratedKeys();
            int idVenta;
            if (generatedKeys.next()) {
                idVenta = generatedKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID de la venta.");
            }

            // 2. Insertar cada producto en 'detalle_venta'
            String sqlDetalle = "INSERT INTO venta_produto (id_venta, id_producto, fecha_venta) VALUES (?, ?, NOW())";
            pstmtDetalle = conn.prepareStatement(sqlDetalle);

            // 3. Actualizar el stock de cada producto
            String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
            pstmtStock = conn.prepareStatement(sqlStock);

            for (int i = 0; i < modeloVenta.getRowCount(); i++) {
                int idProducto = Integer.parseInt(modeloVenta.getValueAt(i, 0).toString()); // ID oculto
                int cantidad = Integer.parseInt(modeloVenta.getValueAt(i, 3).toString());  // Cantidad

                // Insertar en detalle_venta
                pstmtDetalle.setInt(1, idVenta);
                pstmtDetalle.setInt(2, idProducto);
                pstmtDetalle.addBatch(); // Usar batch para mejor rendimiento

                // Actualizar stock
                pstmtStock.setInt(1, cantidad);
                pstmtStock.setInt(2, idProducto);
                pstmtStock.addBatch();
            }

            // Ejecutar lotes
            pstmtDetalle.executeBatch();
            pstmtStock.executeBatch();

            // Confirmar transacción
            conn.commit();

            // Mensaje de éxito
            JOptionPane.showMessageDialog(vista,
                    "Venta registrada con éxito.\n"
                    + "ID de venta: " + idVenta + "\n"
                    + "Total: $" + String.format("%.2f", totalVenta),
                    "Venta completada", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar interfaz
            limpiarInterfaz();

        } catch (ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error: No se encontró el driver de MySQL.",
                    "Error de driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error al registrar la venta: " + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(vista,
                    "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pstmtVenta != null) {
                    pstmtVenta.close();
                }
                if (pstmtDetalle != null) {
                    pstmtDetalle.close();
                }
                if (pstmtStock != null) {
                    pstmtStock.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarInterfaz() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tblVenta.getModel();
        modelo.setRowCount(0); // Limpiar tabla de venta

        vista.totalVenta.setText("0.00");     // Reiniciar total
        vista.idUsuario.setText("");         // Limpiar ID empleado
        vista.cantPrecio.setText("");            // Limpiar cantidad
        // Opcional: limpiar campos de producto
        vista.lblNombre.setText("");
        vista.lblPrecio.setText("");
        vista.lblStock.setText("");
    }

}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
