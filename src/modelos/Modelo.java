package modelos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import vistas.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.SwingConstants;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jl393
 */
public class Modelo {

    private vistaPrincipal principal;
    private JPanel panel;
    private Connection conexion = null;
    private visatAltas altasvista;
    private vistaConsultas consultas;
    private String tipoUsuario;
    private InicioSesion inicio;
    private vistaVentas ventas;

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

    private JPanel crearPanelInicio() {
        JPanel panelInicio = new JPanel(new BorderLayout());
        JLabel lblBienvenida = new JLabel("Sistema de Ventas", SwingConstants.CENTER);
        JLabel lblLogin = new JLabel("Por favor, inicia sesión", SwingConstants.CENTER);

        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panelInicio.add(lblBienvenida, BorderLayout.CENTER);
        panelInicio.add(lblLogin, BorderLayout.SOUTH);
        panelInicio.setBackground(Color.WHITE);

        return panelInicio;
    }

    public void CargarCategorias(visatAltas altasvista) {
        this.altasvista = altasvista;

        // Limpiar y agregar opción por defecto
        this.altasvista.categoriaProducto.removeAllItems();
        this.altasvista.categoriaProducto.addItem("Cargando...");

        String url = "jdbc:mysql://localhost:3306/tienda_vargas";
        String usuarioDB = "root";
        String contrasenaDB = "cisco1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuarioDB, contrasenaDB); Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery("SELECT cat FROM categoria ORDER BY cat")) {

                // Limpiar de nuevo tras "Cargando..."
                this.altasvista.categoriaProducto.removeAllItems();
                this.altasvista.categoriaProducto.addItem("Selecciona una categoría...");

                boolean encontrado = false;
                while (rs.next()) {
                    String cat = rs.getString("cat");
                    this.altasvista.categoriaProducto.addItem(cat);
                    encontrado = true;
                }

                if (!encontrado) {
                    this.altasvista.categoriaProducto.addItem("No hay categorías");
                }

            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(altasvista,
                    "Driver no encontrado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(altasvista,
                    "Error de base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void CargarProveedores(visatAltas altasvista) {
        this.altasvista = altasvista;

        this.altasvista.proveedorProducto.removeAllItems();
        this.altasvista.proveedorProducto.addItem("Cargando...");

        String url = "jdbc:mysql://localhost:3306/tienda_vargas";
        String usuarioDB = "root";
        String contrasenaDB = "cisco1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuarioDB, contrasenaDB); Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery("SELECT empresa_proveedor FROM proveedor ORDER BY empresa_proveedor")) {

                this.altasvista.proveedorProducto.removeAllItems();
                this.altasvista.proveedorProducto.addItem("Selecciona un proveedor...");

                boolean encontrado = false;
                while (rs.next()) {
                    String prov = rs.getString("empresa_proveedor");
                    this.altasvista.proveedorProducto.addItem(prov);
                    encontrado = true;
                }

                if (!encontrado) {
                    this.altasvista.proveedorProducto.addItem("No hay proveedores");
                }

            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(altasvista,
                    "Driver no encontrado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(altasvista,
                    "Error de base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void IniciarSesion(vistaPrincipal principal, vistaVentas ventas) {
        this.ventas = ventas;
        this.principal = principal;
        String usuario = this.principal.Usuario.getText().trim();
        String contrasena = new String(this.principal.Contraseña.getPassword()).trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticar (usa tu clase Database)
        String tipo = autenticarUsuario(usuario, contrasena, ventas); // Debe devolver "Administrador", "Vendedor", o null

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si todo está bien, habilita los botones según el rol
        JOptionPane.showMessageDialog(null, "Bienvenido, " + usuario + " (" + tipo + ")");
        Navegar(principal, panel);

        // Guardamos el tipo de usuario actual
        this.tipoUsuario = tipo;

        // Aplicar privilegios
        aplicarPrivilegios(this.principal);

    }

    public void Inicio(InicioSesion inicio, vistaVentas ventas) {
        this.ventas = ventas;
        this.inicio = inicio;
        String usuario = this.inicio.Usuario.getText().trim();
        String contrasena = new String(this.inicio.Contraseña.getPassword()).trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticar (usa tu clase Database)
        String tipo = autenticarUsuario(usuario, contrasena, ventas); // Debe devolver "Administrador", "Vendedor", o null

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si todo está bien, habilita los botones según el rol
        JOptionPane.showMessageDialog(null, "Bienvenido, " + usuario + " (" + tipo + ")");

        // Guardamos el tipo de usuario actual
        this.tipoUsuario = tipo;

        // Aplicar privilegios
        aplicarPrivilegios(this.principal);

    }

    public void CerrarSesion(vistaPrincipal principal, InicioSesion inicio) {
        this.inicio = inicio;
        this.principal = principal;
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Deshabilitar todos los botones del menú
            deshabilitarTodosLosBotones(this.principal);

            // Limpiar campos de login
            if (this.principal.Usuario != null) {
                this.principal.Usuario.setText("");
            }
            if (this.principal.Contraseña != null) {
                this.principal.Contraseña.setText("");
            }
            Navegar(principal, this.inicio);

            // Opcional: mostrar mensaje de cierre
            JOptionPane.showMessageDialog(null, "Sesión cerrada. Puedes iniciar con otro usuario.", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void aplicarPrivilegios(vistaPrincipal principal) {
        this.principal = principal;
        // Primero: ocultar todos los botones
        habilitarBoton(this.principal.lbAltas, false);
        habilitarBoton(this.principal.lbConsultas, false);
        habilitarBoton(this.principal.lbVentas, false);
        habilitarBoton(this.principal.cerrarSesion, false);

        // Segundo: mostrar según el rol
        if ("administrador".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbAltas, true);
            habilitarBoton(this.principal.lbConsultas, true);
            habilitarBoton(this.principal.lbVentas, true);
            habilitarBoton(this.principal.cerrarSesion, true);
        } else if ("vendedor".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbVentas, true);
            habilitarBoton(this.principal.cerrarSesion, true);
            // No se habilitan lbAltas ni lbConsultas
        }
    }

    public void habilitarBoton(JLabel label, boolean habilitado) {
        if (label == null) {
            return;
        }

        if (habilitado) {
            label.setVisible(true);           // Mostrar
            label.setForeground(Color.LIGHT_GRAY);
            label.setEnabled(true);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        } else {
            label.setVisible(false);          // Ocultar (elimina visualmente)
            label.setEnabled(false);
        }
    }

    public void deshabilitarTodosLosBotones(vistaPrincipal principal) {
        this.principal = principal;
        habilitarBoton(this.principal.lbConsultas, false);
        habilitarBoton(this.principal.lbVentas, false);
        habilitarBoton(this.principal.lbAltas, false);
        habilitarBoton(this.principal.cerrarSesion, false);
    }

    private String autenticarUsuario(String usuario, String contraseña, vistaVentas ventas) {
        this.ventas = ventas;

        // 🔗 URL de conexión
        String url = "jdbc:mysql://localhost:3306/tienda_vargas?useSSL=false&serverTimezone=UTC";

        // 🛠️ Credenciales de la base de datos
        String usuarioBD = "root";
        String contrasenaBD = "cisco1234";

        // 🔍 Consulta SQL: ahora incluye el ID
        String sql = "SELECT id_empleado, tipo FROM empleado WHERE usuario_empleado = ? AND contraseña_empleado = ?";

        String tipoUsuario = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, usuarioBD, contrasenaBD);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // ✅ Obtener el ID y el tipo
                int idUsuario = rs.getInt("id_empleado");
                tipoUsuario = rs.getString("tipo");

                // ✅ Guardar el ID si necesitas usarlo después
                // ✅ Mostrar el ID en una etiqueta (JLabel)
                this.ventas.lblIiUsuario.setText("" + idUsuario); // Cambia 'lblIdUsuario' por el nombre real
                this.ventas.lblIiUsuario.setVisible(true); // Asegúrate de que sea visible

                // Navegar a la vista de ventas
                Navegar(principal, this.ventas);
            }

            // Cerrar recursos
            rs.close();
            ps.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: No se encontró el controlador MySQL.\n"
                    + "Asegúrate de agregar el archivo mysql-connector-java-x.x.x.jar",
                    "Error de Driver",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return tipoUsuario;
    }
}
