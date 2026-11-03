package restaurante.presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import restaurante.entidades.DetallePedido;
import restaurante.entidades.EstadoPedido;
import restaurante.entidades.Pedido;
import restaurante.entidades.Plato;
import restaurante.datos.CatalogoMenu;

/** Punto de venta editable visualmente desde PanelPedidos.form. */
public class PanelPedidos extends javax.swing.JPanel {
    private final DecimalFormat moneda = new DecimalFormat("$ #,##0", new DecimalFormatSymbols(new Locale("es", "CO")));
    private final List<DetallePedido> carrito = new ArrayList<>();
    private final List<Pedido> pedidosCobrados = new ArrayList<>();
    private DefaultTableModel modeloCarrito;
    private String categoriaActual = "Hamburguesas";
    private int siguientePedido = 1;

    public PanelPedidos() {
        initComponents();
        aplicarEstilo();
        modeloCarrito = (DefaultTableModel) tablaCarrito.getModel();
        mostrarCategoria("Hamburguesas", btnHamburguesas);
        actualizarCarrito();
    }

    private void aplicarEstilo() {
        setOpaque(true);
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        panelContenido.setOpaque(false);
        panelZonaMenu.setOpaque(false);
        panelCategorias.setOpaque(false);
        panelProductos.setOpaque(false);
        panelZonaLineas.setOpaque(false);
        panelResumen.setOpaque(false);
        lineaTotal.setOpaque(false);
        panelAcciones.setOpaque(false);
        panelPedido.setOpaque(true);
        panelPedido.setBackground(Tema.TARJETA);
        panelPedido.setPreferredSize(new Dimension(340, 0));
        panelPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE.brighter(), 2),
                BorderFactory.createEmptyBorder(16, 16, 14, 16)));

        lblPedidoActual.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        lblPedidoActual.setForeground(Tema.TEXTO);
        lblMesa.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
        lblMesa.setForeground(Tema.TEXTO_SEC);
        lblTotalTitulo.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        lblTotalTitulo.setForeground(Tema.TEXTO);
        lblTotal.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        lblTotal.setForeground(Tema.TEXTO);
        lblVacio.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        lblVacio.setForeground(Tema.TEXTO_SEC);
        lblVacio.setHorizontalAlignment(SwingConstants.CENTER);

        for (JButton boton : new JButton[]{btnHamburguesas, btnCombos, btnBebidas, btnExtras}) {
            boton.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.putClientProperty("JButton.buttonType", "roundRect");
            boton.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        }
        estilizarBoton(btnQuitar, Tema.TARJETA, Tema.TEXTO, false);
        btnQuitar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE.brighter(), 2), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        estilizarBoton(btnCobrar, Tema.TEXTO, Tema.FONDO, true);

        tablaCarrito.setBackground(Tema.TARJETA);
        tablaCarrito.setForeground(Tema.TEXTO);
        tablaCarrito.setSelectionBackground(new Color(0x49306F));
        tablaCarrito.setSelectionForeground(Tema.TEXTO);
        tablaCarrito.setGridColor(Tema.BORDE);
        tablaCarrito.setRowHeight(38);
        tablaCarrito.setShowVerticalLines(false);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.getTableHeader().setBackground(Tema.TARJETA);
        tablaCarrito.getTableHeader().setForeground(Tema.TEXTO_SEC);
        tablaCarrito.getTableHeader().setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        tablaCarrito.getTableHeader().setReorderingAllowed(false);
        tablaCarrito.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaCarrito.getColumnModel().getColumn(1).setPreferredWidth(42);
        tablaCarrito.getColumnModel().getColumn(2).setPreferredWidth(100);
        scrollCarrito.setBorder(BorderFactory.createEmptyBorder());
        scrollCarrito.getViewport().setBackground(Tema.TARJETA);
        scrollProductos.setBorder(BorderFactory.createEmptyBorder());
        scrollProductos.getViewport().setOpaque(false);
        scrollProductos.setOpaque(false);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto, boolean principal) {
        boton.setFont(new Font("SF Pro Display", principal ? Font.BOLD : Font.PLAIN, 14));
        boton.setForeground(texto);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.putClientProperty("JButton.buttonType", "roundRect");
    }

    private void mostrarCategoria(String categoria, JButton botonActivo) {
        categoriaActual = categoria;
        for (JButton boton : new JButton[]{btnHamburguesas, btnCombos, btnBebidas, btnExtras}) {
            boolean activo = boton == botonActivo;
            boton.setBackground(activo ? new Color(0x163A68) : Tema.TARJETA);
            boton.setForeground(activo ? new Color(0x8FC5FF) : Tema.TEXTO_SEC);
            boton.setBorder(BorderFactory.createLineBorder(activo ? new Color(0x3977B8) : Tema.BORDE, activo ? 2 : 1));
        }
        panelProductos.removeAll();
        for (Plato plato : CatalogoMenu.porCategoria(categoria)) {
            JButton tarjeta = new JButton("<html><div style='text-align:left'><b>" + plato.getNombre()
                    + "</b><br><span style='color:#B8A9D9'>" + moneda.format(plato.getPrecio()) + "</span></div></html>");
            tarjeta.setHorizontalAlignment(SwingConstants.LEFT);
            tarjeta.setBackground(Tema.TARJETA);
            tarjeta.setForeground(Tema.TEXTO);
            tarjeta.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Tema.BORDE.brighter(), 2),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)));
            tarjeta.setFocusPainted(false);
            tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tarjeta.setPreferredSize(new Dimension(170, 76));
            tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    tarjeta.setBackground(Tema.TARJETA.brighter());
                    tarjeta.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Tema.ACENTO.brighter(), 2), BorderFactory.createEmptyBorder(14, 14, 14, 14)));
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    tarjeta.setBackground(Tema.TARJETA);
                    tarjeta.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Tema.BORDE.brighter(), 2), BorderFactory.createEmptyBorder(14, 14, 14, 14)));
                }
            });
            tarjeta.addActionListener(e -> agregarProducto(plato));
            panelProductos.add(tarjeta);
        }
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    /** Recarga los platos compartidos después de volver desde la pantalla Menú. */
    public void refrescarCatalogo() {
        JButton boton = btnHamburguesas;
        if ("Combos".equals(categoriaActual)) boton = btnCombos;
        else if ("Bebidas".equals(categoriaActual)) boton = btnBebidas;
        else if ("Extras".equals(categoriaActual)) boton = btnExtras;
        mostrarCategoria(categoriaActual, boton);
    }

    private void agregarProducto(Plato plato) {
        for (DetallePedido linea : carrito) {
            if (linea.getPlato().getCodigo().equals(plato.getCodigo())) {
                linea.setCantidad(linea.getCantidad() + 1);
                actualizarCarrito();
                return;
            }
        }
        carrito.add(new DetallePedido(plato, 1, plato.getPrecio()));
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (DetallePedido detalle : carrito) {
            modeloCarrito.addRow(new Object[]{detalle.getPlato().getNombre(), detalle.getCantidad(), moneda.format(detalle.calcularSubtotal())});
            total += detalle.calcularSubtotal();
        }
        lblTotal.setText(moneda.format(total));
        lblVacio.setVisible(carrito.isEmpty());
    }

    private void quitarSeleccion() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) {
            if (!carrito.isEmpty()) JOptionPane.showMessageDialog(this, "Selecciona un producto del pedido para quitarlo.", "Pedido actual", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        carrito.remove(fila);
        actualizarCarrito();
    }

    private void cobrarPedido() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto antes de cobrar.", "Pedido vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        double total = carrito.stream().mapToDouble(DetallePedido::calcularSubtotal).sum();
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Cobrar " + moneda.format(total) + " y registrar el pedido?",
                "Confirmar cobro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) return;
        Pedido pedido = new Pedido(siguientePedido++, LocalDate.now(), null, null, EstadoPedido.PENDIENTE, new ArrayList<>(carrito));
        pedidosCobrados.add(pedido);
        carrito.clear();
        actualizarCarrito();
        JOptionPane.showMessageDialog(this, "Pedido #" + pedido.getNumero() + " cobrado correctamente.", "Cobro completado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnHamburguesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHamburguesasActionPerformed
        mostrarCategoria("Hamburguesas", btnHamburguesas);
    }//GEN-LAST:event_btnHamburguesasActionPerformed

    private void btnCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCombosActionPerformed
        mostrarCategoria("Combos", btnCombos);
    }//GEN-LAST:event_btnCombosActionPerformed

    private void btnBebidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBebidasActionPerformed
        mostrarCategoria("Bebidas", btnBebidas);
    }//GEN-LAST:event_btnBebidasActionPerformed

    private void btnExtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtrasActionPerformed
        mostrarCategoria("Extras", btnExtras);
    }//GEN-LAST:event_btnExtrasActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        quitarSeleccion();
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        cobrarPedido();
    }//GEN-LAST:event_btnCobrarActionPerformed

    /** This method is called from within the constructor to initialize the form. */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        panelContenido = new javax.swing.JPanel();
        panelZonaMenu = new javax.swing.JPanel();
        panelCategorias = new javax.swing.JPanel();
        btnHamburguesas = new javax.swing.JButton();
        btnCombos = new javax.swing.JButton();
        btnBebidas = new javax.swing.JButton();
        btnExtras = new javax.swing.JButton();
        scrollProductos = new javax.swing.JScrollPane();
        panelProductos = new javax.swing.JPanel();
        panelPedido = new javax.swing.JPanel();
        panelEncabezadoPedido = new javax.swing.JPanel();
        lblPedidoActual = new javax.swing.JLabel();
        lblMesa = new javax.swing.JLabel();
        panelZonaLineas = new javax.swing.JPanel();
        scrollCarrito = new javax.swing.JScrollPane();
        tablaCarrito = new javax.swing.JTable();
        lblVacio = new javax.swing.JLabel();
        panelResumen = new javax.swing.JPanel();
        lineaTotal = new javax.swing.JPanel();
        lblTotalTitulo = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnQuitar = new javax.swing.JButton();
        btnCobrar = new javax.swing.JButton();

        setBackground(Tema.FONDO);
        setLayout(new java.awt.BorderLayout());
        panelContenido.setLayout(new java.awt.BorderLayout(16, 0));
        panelZonaMenu.setLayout(new java.awt.BorderLayout(0, 14));
        panelCategorias.setLayout(new java.awt.GridLayout(1, 4, 8, 0));
        btnHamburguesas.setText("Hamburguesas");
        btnHamburguesas.addActionListener(this::btnHamburguesasActionPerformed);
        panelCategorias.add(btnHamburguesas);
        btnCombos.setText("Combos");
        btnCombos.addActionListener(this::btnCombosActionPerformed);
        panelCategorias.add(btnCombos);
        btnBebidas.setText("Bebidas");
        btnBebidas.addActionListener(this::btnBebidasActionPerformed);
        panelCategorias.add(btnBebidas);
        btnExtras.setText("Extras");
        btnExtras.addActionListener(this::btnExtrasActionPerformed);
        panelCategorias.add(btnExtras);
        panelZonaMenu.add(panelCategorias, java.awt.BorderLayout.PAGE_START);
        panelProductos.setLayout(new java.awt.GridLayout(0, 2, 12, 12));
        scrollProductos.setViewportView(panelProductos);
        panelZonaMenu.add(scrollProductos, java.awt.BorderLayout.CENTER);
        panelContenido.add(panelZonaMenu, java.awt.BorderLayout.CENTER);

        panelPedido.setLayout(new java.awt.BorderLayout(0, 12));
        panelEncabezadoPedido.setLayout(new java.awt.BorderLayout());
        lblPedidoActual.setText("Pedido actual");
        panelEncabezadoPedido.add(lblPedidoActual, java.awt.BorderLayout.LINE_START);
        lblMesa.setText("Mesa 4");
        panelEncabezadoPedido.add(lblMesa, java.awt.BorderLayout.LINE_END);
        panelPedido.add(panelEncabezadoPedido, java.awt.BorderLayout.PAGE_START);
        panelZonaLineas.setLayout(new java.awt.BorderLayout());
        tablaCarrito.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Producto", "Cant.", "Subtotal"}) {
            public boolean isCellEditable(int row, int column) { return false; }
        });
        scrollCarrito.setViewportView(tablaCarrito);
        panelZonaLineas.add(scrollCarrito, java.awt.BorderLayout.CENTER);
        lblVacio.setText("Elige productos para comenzar");
        panelZonaLineas.add(lblVacio, java.awt.BorderLayout.PAGE_END);
        panelPedido.add(panelZonaLineas, java.awt.BorderLayout.CENTER);
        panelResumen.setLayout(new java.awt.BorderLayout(0, 10));
        panelResumen.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDE));
        lineaTotal.setLayout(new java.awt.BorderLayout());
        lblTotalTitulo.setText("Total");
        lineaTotal.add(lblTotalTitulo, java.awt.BorderLayout.LINE_START);
        lblTotal.setText("$ 0");
        lineaTotal.add(lblTotal, java.awt.BorderLayout.LINE_END);
        panelResumen.add(lineaTotal, java.awt.BorderLayout.PAGE_START);
        panelAcciones.setLayout(new java.awt.GridLayout(1, 2, 8, 0));
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(this::btnQuitarActionPerformed);
        panelAcciones.add(btnQuitar);
        btnCobrar.setText("Cobrar");
        btnCobrar.addActionListener(this::btnCobrarActionPerformed);
        panelAcciones.add(btnCobrar);
        panelResumen.add(panelAcciones, java.awt.BorderLayout.PAGE_END);
        panelPedido.add(panelResumen, java.awt.BorderLayout.PAGE_END);
        panelContenido.add(panelPedido, java.awt.BorderLayout.LINE_END);
        add(panelContenido, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBebidas;
    private javax.swing.JButton btnCobrar;
    private javax.swing.JButton btnCombos;
    private javax.swing.JButton btnExtras;
    private javax.swing.JButton btnHamburguesas;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JLabel lblMesa;
    private javax.swing.JLabel lblPedidoActual;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTitulo;
    private javax.swing.JLabel lblVacio;
    private javax.swing.JPanel lineaTotal;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelCategorias;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelEncabezadoPedido;
    private javax.swing.JPanel panelPedido;
    private javax.swing.JPanel panelProductos;
    private javax.swing.JPanel panelResumen;
    private javax.swing.JPanel panelZonaLineas;
    private javax.swing.JPanel panelZonaMenu;
    private javax.swing.JScrollPane scrollCarrito;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JTable tablaCarrito;
    // End of variables declaration//GEN-END:variables
}
