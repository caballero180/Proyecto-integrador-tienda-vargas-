<<<<<<< HEAD
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

    public void IniciarSesion(vistaPrincipal principal) {
        this.principal = principal;
        String usuario = this.principal.Usuario.getText().trim();
        String contrasena = new String(this.principal.Contraseña.getPassword()).trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticar (usa tu clase Database)
        String tipo = autenticarUsuario(usuario, contrasena); // Debe devolver "Administrador", "Vendedor", o null

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

    public void CerrarSesion(vistaPrincipal principal) {
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

            // Opcional: mostrar mensaje de cierre
            JOptionPane.showMessageDialog(null, "Sesión cerrada. Puedes iniciar con otro usuario.", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
        }
        
        

        
    }
    

    private void aplicarPrivilegios(vistaPrincipal principal) {
        this.principal = principal;
        habilitarBoton(this.principal.lbVentas, true);
        habilitarBoton(this.principal.cerrarSesion, true);

        if ("Administrador".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbConsultas, true);
            habilitarBoton(this.principal.lbAltas, true);
        } else if ("Vendedor".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbConsultas, false);
            habilitarBoton(this.principal.lbAltas, false);
        } else {
            // Otro tipo no permitido
            deshabilitarTodosLosBotones(this.principal);
        }
    }

    private void habilitarBoton(JLabel label, boolean habilitado) {
        if (label == null) {
            return;
        }

        if (habilitado) {
            label.setForeground(Color.LIGHT_GRAY);
            label.setEnabled(true);
            // Puedes agregar hover si ya lo tenías
        } else {
            label.setForeground(Color.GRAY);
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

    private String autenticarUsuario(String usuario, String contraseña) {
        // 🔗 URL de conexión: jdbc:mysql://host:puerto/nombre_basedatos
        String url = "jdbc:mysql://localhost:3306/tienda_vargas?useSSL=false&serverTimezone=UTC";

        // 🛠️ Usuario y contraseña de la BASE DE DATOS (no del login)
        String usuarioBD = "root";
        String contrasenaBD = "cisco1234"; // Puedes poner tu contraseña si tiene

        // 🔍 Consulta SQL para verificar al usuario
        String sql = "SELECT tipo FROM empleado WHERE usuario_empleado = ? AND contraseña_empleado = ?";

        // ✅ Variable para guardar el tipo de usuario
        String tipoUsuario = null;
        try {
            // 1. Cargar el driver de MySQL (necesario si no se carga automáticamente)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establecer conexión con la base de datos
            Connection conn = DriverManager.getConnection(url, usuarioBD, contrasenaBD);

            // 3. Preparar la consulta
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);      // usuario del login
            ps.setString(2, contraseña);   // contraseña del login

            // 4. Ejecutar consulta
            ResultSet rs = ps.executeQuery();

            // 5. Si hay resultado, obtener el tipo
            if (rs.next()) {
                tipoUsuario = rs.getString("tipo");
            }

            // 6. Cerrar recursos
            rs.close();
            ps.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            // Este error ocurre si no tienes el JAR de MySQL en el proyecto
            JOptionPane.showMessageDialog(null,
                    "Error: No se encontró el controlador MySQL.\n"
                    + "Asegúrate de agregar el archivo mysql-connector-java-x.x.x.jar",
                    "Error de Driver",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            // Error de conexión, usuario, base de datos, etc.
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return tipoUsuario;
    }
}
=======
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

    public void IniciarSesion(vistaPrincipal principal) {
        this.principal = principal;
        String usuario = this.principal.Usuario.getText().trim();
        String contrasena = new String(this.principal.Contraseña.getPassword()).trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticar (usa tu clase Database)
        String tipo = autenticarUsuario(usuario, contrasena); // Debe devolver "Administrador", "Vendedor", o null

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

    public void CerrarSesion(vistaPrincipal principal) {
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

            // Opcional: mostrar mensaje de cierre
            JOptionPane.showMessageDialog(null, "Sesión cerrada. Puedes iniciar con otro usuario.", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
        }
        
        

        
    }
    

    private void aplicarPrivilegios(vistaPrincipal principal) {
        this.principal = principal;
        habilitarBoton(this.principal.lbVentas, true);
        habilitarBoton(this.principal.cerrarSesion, true);

        if ("Administrador".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbConsultas, true);
            habilitarBoton(this.principal.lbAltas, true);
        } else if ("Vendedor".equals(tipoUsuario)) {
            habilitarBoton(this.principal.lbConsultas, false);
            habilitarBoton(this.principal.lbAltas, false);
        } else {
            // Otro tipo no permitido
            deshabilitarTodosLosBotones(this.principal);
        }
    }

    private void habilitarBoton(JLabel label, boolean habilitado) {
        if (label == null) {
            return;
        }

        if (habilitado) {
            label.setForeground(Color.LIGHT_GRAY);
            label.setEnabled(true);
            // Puedes agregar hover si ya lo tenías
        } else {
            label.setForeground(Color.GRAY);
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

    private String autenticarUsuario(String usuario, String contraseña) {
        // 🔗 URL de conexión: jdbc:mysql://host:puerto/nombre_basedatos
        String url = "jdbc:mysql://localhost:3306/tienda_vargas?useSSL=false&serverTimezone=UTC";

        // 🛠️ Usuario y contraseña de la BASE DE DATOS (no del login)
        String usuarioBD = "root";
        String contrasenaBD = "cisco1234"; // Puedes poner tu contraseña si tiene

        // 🔍 Consulta SQL para verificar al usuario
        String sql = "SELECT tipo FROM empleado WHERE usuario_empleado = ? AND contraseña_empleado = ?";

        // ✅ Variable para guardar el tipo de usuario
        String tipoUsuario = null;
        try {
            // 1. Cargar el driver de MySQL (necesario si no se carga automáticamente)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establecer conexión con la base de datos
            Connection conn = DriverManager.getConnection(url, usuarioBD, contrasenaBD);

            // 3. Preparar la consulta
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);      // usuario del login
            ps.setString(2, contraseña);   // contraseña del login

            // 4. Ejecutar consulta
            ResultSet rs = ps.executeQuery();

            // 5. Si hay resultado, obtener el tipo
            if (rs.next()) {
                tipoUsuario = rs.getString("tipo");
            }

            // 6. Cerrar recursos
            rs.close();
            ps.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            // Este error ocurre si no tienes el JAR de MySQL en el proyecto
            JOptionPane.showMessageDialog(null,
                    "Error: No se encontró el controlador MySQL.\n"
                    + "Asegúrate de agregar el archivo mysql-connector-java-x.x.x.jar",
                    "Error de Driver",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            // Error de conexión, usuario, base de datos, etc.
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return tipoUsuario;
    }
}
>>>>>>> 15f6626696aa1beb6a113caa7c584749c04c275f
